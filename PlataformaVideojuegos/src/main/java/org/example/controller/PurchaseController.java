package org.example.controller;

import org.example.exeptions.ValidationException;
import org.example.mapper.Mapper;
import org.example.model.dto.game.GameState;
import org.example.model.dto.purchase.PurchaseDTO;
import org.example.model.dto.purchase.PurchaseState;
import org.example.model.dto.user.AccountState;
import org.example.model.entidad.GameEntity;
import org.example.model.entidad.LibraryEntity;
import org.example.model.entidad.PurchaseEntity;
import org.example.model.entidad.UserEntity;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;
import org.example.model.form.PurchaseForm;
import org.example.model.form.updates.PurchaseUpdate;
import org.example.model.form.updates.UserUpdate;
import org.example.model.paymentMethod.IPaymentMethod;
import org.example.model.paymentMethod.PaymentMethod;
import org.example.repository.Interface.IGameRepo;
import org.example.repository.Interface.ILibraryRepo;
import org.example.repository.Interface.IPurchaseRepo;
import org.example.repository.Interface.IUserRepo;
import org.example.transaction.ITransactionManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PurchaseController {

    public static final int REFUND_DAYS_LIMIT = 14;
    public static final int HOURS_PERMITED = 2;
    public static final int MIN_BASE_PRICE = 0;
    public static final int MIN_CURRENT_DISCOUNT = 0;
    public static final int MAX_CURRENT_DISCOUNT = 100;
    private IPurchaseRepo purchaseRepo;
    private IGameRepo gameRepo;
    private IUserRepo userRepo;
    private ILibraryRepo libraryRepo;
    public ITransactionManager tm;



    //Constructor


    public PurchaseController(IPurchaseRepo purchaseRepo, IGameRepo gameRepo, IUserRepo userRepo, ILibraryRepo libraryRepo, ITransactionManager tm) {
        this.purchaseRepo = purchaseRepo;
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
        this.libraryRepo = libraryRepo;
        this.tm = tm;
    }


    /**
     * Crear una nueva transacción para adquirir un juego
     *
     * @param userId          Id del usuario que intenta comprar
     * @param gameId          Id del juego que se intenta comprar
     * @param paymentMethod metodo mediante el cual el usuario va a pagar
     * @return PurchaseDTO creada
     *
     */
    public PurchaseDTO makePurchase(Long userId, Long gameId, PaymentMethod paymentMethod) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        PurchaseEntity purchase = tm.inTransaction(() -> {

            GameEntity game = gameRepo.getById(gameId).orElse(null);
            UserEntity user = userRepo.getById(userId).orElse(null);

            // Compruebo que el usuario exista
            if (user == null) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }

            // Compruebo que el juego exista
            if (game == null) {
                errors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
            }

            // Compruebo método de pago
            if (paymentMethod == null) {
                errors.add(new ErrorDto("PaymentMethod", ErrorType.REQUERIDO));
            }

            // Si usuario y juego existen, hago validaciones extra
            if (user != null && game != null) {

                // Compruebo que el precio base sea mayor o igual a cero
                if (game.getBasePrice() < MIN_BASE_PRICE) {
                    errors.add(new ErrorDto("BasePrice", ErrorType.VALOR_DEMASIADO_BAJO));
                }

                // Compruebo descuento mínimo
                if (game.getCurrentDescount() < MIN_CURRENT_DISCOUNT) {
                    errors.add(new ErrorDto("CurrentDiscount", ErrorType.VALOR_DEMASIADO_BAJO));
                }

                // Compruebo descuento máximo
                if (game.getCurrentDescount() > MAX_CURRENT_DISCOUNT) {
                    errors.add(new ErrorDto("CurrentDiscount", ErrorType.VALOR_DEMASIADO_ALTO));
                }

                // Busco si el usuario ya compró ese juego
                PurchaseEntity previousPurchase = purchaseRepo.getAll().stream()
                        .filter(p ->
                                Objects.equals(p.getIdUser(), user.getId()) &&
                                        Objects.equals(p.getIdGame(), game.getId()))
                        .findFirst()
                        .orElse(null);

                // Compruebo duplicado
                if (previousPurchase != null &&
                        previousPurchase.getSatate() == PurchaseState.COMPLETADA) {

                    errors.add(new ErrorDto("PurchaseState", ErrorType.DUPLICADO));
                }

                errors.addAll(validate(user, game));
            }

            // Si hay errores hago rollback
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            // Calculo descuento aplicado
            float discount = game.getBasePrice() * game.getCurrentDescount() / 100;
            float discountApplied = game.getBasePrice() - discount;

            PurchaseForm purchaseForm = new PurchaseForm(
                    user.getId(),
                    game.getId(),
                    paymentMethod,
                    game.getBasePrice(),
                    discountApplied
            );

            return purchaseRepo.create(purchaseForm).orElse(null);
        });

        Util.thowException(errors);

        return Mapper.mapFrom(purchase);
    }


    /**
     * Crear una nueva transacción para adquirir un juego
     *
     * @param idPurchase    Id de la compra que se intenta realizar
     * @param paymentMethod metodo mediante el cual el usuario va a pagar
     * @return Exito en el pago o no
     *
     */
    public boolean processPayment(Long idPurchase, IPaymentMethod paymentMethod) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion
        boolean payed = tm.inTransaction(()->{

            //Compruebo que el paymentMethod no sea null
            if (paymentMethod == null) {
                errors.add(new ErrorDto("PaymentMethod", ErrorType.REQUERIDO));
            }
            //Compruebo que la compra exista
            PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElse(null);
            if (purchase == null) {
                errors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
            }else {
                //Compruebo que la compra este en estado pendiente de pagar
                if (purchase.getSatate() != PurchaseState.PENDIENTE) {
                    errors.add(new ErrorDto("PurchaseState", ErrorType.DUPLICADO));
                }
            }
            //Si hay errores mando una ilegalArgumentExeption
            if (!errors.isEmpty()){
                //Lanzo exepcion si hay errores
                Util.thowException(errors);
            }

            //Realizo el pago y la compra pasa a estado completada. Si no se puede realizar pasa a estado cancelada.
            //Si la funcion de makePayment() de los paymentMethod manda un validation exeption, quiere decir que hubo un error en el pago, asi que lo capturo en el catch
            try {
                paymentMethod.makePayment(purchase.getDiscountApplicated());

                var updatedPurchase = new PurchaseUpdate(purchase.getId(), purchase.getIdUser(), purchase.getIdGame(), purchase.getPurchaseDate()
                        , purchase.getPaymentMethod(), purchase.getPriceWithoutDiscount(), purchase.getDiscountApplicated(), PurchaseState.COMPLETADA);

                purchaseRepo.update(idPurchase, updatedPurchase);

            }catch (ValidationException e){
                var updatedPurchase = new PurchaseUpdate(purchase.getId(), purchase.getIdUser(), purchase.getIdGame(), purchase.getPurchaseDate()
                        , purchase.getPaymentMethod(), purchase.getPriceWithoutDiscount(), purchase.getDiscountApplicated(), PurchaseState.CANCELADA);

                purchaseRepo.update(idPurchase, updatedPurchase);
            }

            return purchaseRepo.getById(idPurchase)
                    .map(p -> p.getSatate() == PurchaseState.COMPLETADA)
                    .orElse(false);
        });



        return payed;
    }


    /**
     * Ver todas las compras realizadas por un usuario
     *
     * @param idUser  Id del usuario
     * @param state   estado de las compras
     * @param minDate fecha minima por la cual se va a filtrar
     * @param maxDate fecha maxima por la cual se va a filtrar
     * @return Lista de compras
     *
     */
    public List<PurchaseEntity> consultPurchasesRecord(Long idUser, Optional<PurchaseState> state, Optional<LocalDate> minDate, Optional<LocalDate> maxDate) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transacion
        List<PurchaseEntity> ordenatedPurchases = tm.inTransaction(()->{

            //Compruebo que el usuario exista
            UserEntity user = userRepo.getById(idUser).orElse(null);
            if(user == null){errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));}

            //Si hay errores mando una ilegalArgumentExeption para que la funcion inTransaction la capture en el catch y haga un rollback de la transaccion
            if (!errors.isEmpty()){
                throw new IllegalArgumentException();
            }
            //Encuentro todas las compras que haya realizado el usuario con el id pasado por parametro
            List<PurchaseEntity> foundPurchases = purchaseRepo.getAll().stream()
                    .filter(p -> Objects.equals(p.getIdUser(), idUser))
                    .toList();
            //Si se paso por parametro un estado se filtra por estado
            if (state.isPresent()) {
                foundPurchases = foundPurchases.stream()
                        .filter(p -> p.getSatate() == state.get())
                        .toList();
            }
            //Si se paso por parametro un rango de fecha se filtra
            if (minDate.isPresent() && maxDate.isPresent()) {
                foundPurchases = foundPurchases.stream()
                        .filter(p -> p.getPurchaseDate().isAfter(minDate.get()) && p.getPurchaseDate().isBefore(maxDate.get()))
                        .toList();
            }

            return foundPurchases;
        });

        //Vuelvo a comprobar si hay errores para mandar la validationExeption
        Util.thowException(errors);

        return ordenatedPurchases;
    }


    /**
     * Ver información completa de una transacción específica
     *
     * @param idPurchase id de la compra
     * @param idUser     id del usuario que realizó la compra
     * @return PurchaseDTO con todos los detalles de la compra
     *
     */
    public PurchaseDTO consultPurchaseDetails(Long idPurchase, Long idUser) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion

        PurchaseEntity purchase = tm.inTransaction(()->{
            //busco la compra y la guardo
            PurchaseEntity pur = purchaseRepo.getById(idPurchase).orElse(null);

            //Compruebo que la compra exista
            if (pur == null) {
                errors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
            }else {
                //Compruebo que el idUser coincida con el del usuario que realizo la compra
                if (!Objects.equals(pur.getIdUser(), idUser)) {
                    errors.add(new ErrorDto("UserId, PurchaseId", ErrorType.NO_ENCONTRADO));
                }
            }
            return pur;
            //En este caso no es necesario hacer rollback porque solo se hizo una consulta a la base de datos, no se modifico nada
        });

        //Compruebo si hay errores para mandar la validationExeption
        Util.thowException(errors);

        return Mapper.mapFrom(purchase);
    }


    /**
     * Devolver una compra y reintegrar el dinero a la cartera
     *
     * @param idPurchase id de la compra
     * @param reason     Rason por la cual el usuario quiere el reembolso
     * @return Confirmacion del reembolso
     *
     */
    public  void requestRefund(Long idPurchase, String reason) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion
        tm.inTransaction(()->{

            //busco la compra y la guardo
            PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElse(null);
            //Compruebo que la compra exista
            if (purchase == null) {
                errors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
            }else {
                //Compruebo que el estado de la compra este en completada
                if (purchase.getSatate() != PurchaseState.COMPLETADA) {
                    errors.add(new ErrorDto("PurchaseState", ErrorType.ESTADO_INCORRECTO));
                }
                //Busco una biblioteca la cual tenga la relacion entre el usuario que compro el juego y el jugo
                LibraryEntity library = libraryRepo.getByUserGameId(purchase.getIdUser(), purchase.getIdGame()).orElse(null);
                //Compruebo que la biblioteca exista
                if (library == null) {
                    errors.add(new ErrorDto("LibraryIdUser, LibraryIdGame", ErrorType.NO_ENCONTRADO));
                }else {
                    //Compruebo que no se exedan los 14 dias luego de la compra del juego o que el usuario no haya jugado mas de 2 horas
                    long days = ChronoUnit.DAYS.between(library.getAcquisitionDate(), LocalDate.now());
                    if (days > REFUND_DAYS_LIMIT) {
                        errors.add(new ErrorDto("PurchaseDate", ErrorType.VALOR_DEMASIADO_ALTO));
                    }
                    if (library.getTimePlaying() > HOURS_PERMITED) {
                        errors.add(new ErrorDto("HoursPlayed", ErrorType.VALOR_DEMASIADO_ALTO));
                    }
                }
            }

            //Busco al usuario que aparece en la compra
            UserEntity user = userRepo.getById(purchase.getIdUser()).orElse(null);
            //Compruebo que el usuario que aparece en la compra exista
            if (user == null) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }

            //compruebo si hay errores
            if (!errors.isEmpty()){
                throw new IllegalArgumentException();
            }

            //obtengo cuanto le costo el juego al usuario y lo guardo
            float amount = purchase.getDiscountApplicated();
            //Creo al usuario actiualizado reponiendole el dinero
            UserUpdate userForm = new UserUpdate(user.getUserName(), user.getEmail(), user.getPassword(), user.getRealName(), user.getCountry(), user.getBirthDate(), user.getRegistrationDate(), user.getAvatar(), user.getPortfolioBalance() + amount, user.getAccountState());
            //Actualizo al usuario
            UserEntity updatedUser = userRepo.update(user.getId(), userForm).orElse(null);

            //Creo la compra actualizada con el estado en reembolsada
            PurchaseUpdate purchaseForm = new PurchaseUpdate(purchase.getId(), purchase.getIdUser(), purchase.getIdGame(), purchase.getPurchaseDate(), purchase.getPaymentMethod(), purchase.getPriceWithoutDiscount(), purchase.getDiscountApplicated(), PurchaseState.REEMBOLSADA);
            //Actualizo la compra
            PurchaseEntity updatedPurchase = purchaseRepo.update(idPurchase, purchaseForm).orElse(null);

            //Compruebo que se haya actualizado el usuario
            if (updatedUser == null) {
                errors.add(new ErrorDto("User", ErrorType.NO_ACTUALIZADO));
            }
            //Compruebo si se ha actualizado la compra
            if (updatedPurchase != null) {
                errors.add(new ErrorDto("Purchase", ErrorType.NO_ACTUALIZADO));
            }

            //compruebo si hay errores en las actualizaciones y lanzo las exepcion para hacer rollback en caso de algun error
            if (!errors.isEmpty()){
                throw new IllegalArgumentException();
            }

            //Aqui devuelvo un booleano porque la lambda me obliga a devolver algo, pero en verdad todo lo estoy controlando con las exepciones
            return true;
        });

        //Compruebo si hay errores para mandar la exepcion
        Util.thowException(errors);

        //Si no salta ninguna exepcion es que la funcion fue exitosa
    }


    /**
     * Crea un comprobante de compra en formato imprimible
     *
     * @param idPurchase id de la compra
     * @return PurchaseDTO con los datos de la compra
     *
     */
    //public PurchaseDTO generateBill(Long idPurchase) throws ValidationException {
    //    List<ErrorDto> errors = new ArrayList<>();
//
    //    PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElse(null);
    //    if (purchase == null) {
    //        errors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
    //    }
    //    Util.thowException(errors);
//
    //    return Mapper.mapFrom(purchase);
    //}

    /**
     * Realiza las validaciones del PurchaseForm que necesitan acceso a datos
     *
     * @param user Usuario que intenta comprar
     * @param game juego que se intenta comprar
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     *
     */
    public List<ErrorDto> validate(UserEntity user, GameEntity game) {

        List<ErrorDto> errors = new ArrayList<>();

        if (game == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }
        if (user == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }

        //Compruebo que el usuario exista en el repositorio
        if (userRepo.getById(user.getId()).isEmpty()) {
            errors.add(new ErrorDto("IdUser", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que la cuenta del usuario este activa
        if (Arrays.stream(AccountState.values()).noneMatch(s -> s.equals(user.getAccountState()))) {
            errors.add(new ErrorDto("AccountState", ErrorType.FORMATO_INVALIDO));
        }

        //Compruebo que el juego exista en el sistema
        if (gameRepo.getById(game.getId()).isEmpty()) {
            errors.add(new ErrorDto("IdGame", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que le juego este en estado DISPONIBLE, PREVENTA o ACCESO_ANTICIPADO
        if (game.getState().equals(GameState.NO_DISPONIBLE) || Arrays.stream(GameState.values()).noneMatch(s -> s.equals(game.getState()))) {
            errors.add(new ErrorDto("State", ErrorType.NO_ENCONTRADO));
        }

        return errors;
    }

}
