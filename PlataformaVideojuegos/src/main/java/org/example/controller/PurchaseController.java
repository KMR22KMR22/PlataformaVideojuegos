package org.example.controller;

import org.example.exeptions.ValidationException;
import org.example.mapper.Mapper;
import org.example.model.dto.game.GameDTO;
import org.example.model.dto.game.GameState;
import org.example.model.dto.purchase.PurchaseDTO;
import org.example.model.dto.purchase.PurchaseState;
import org.example.model.dto.user.AccountState;
import org.example.model.dto.user.UserDTO;
import org.example.model.entidad.GameEntity;
import org.example.model.entidad.LibraryEntity;
import org.example.model.entidad.PurchaseEntity;
import org.example.model.entidad.UserEntity;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;
import org.example.model.form.PurchaseForm;
import org.example.model.form.updates.PurchaseUpdate;
import org.example.model.form.updates.UserUpdate;
import org.example.model.paymentMethod.*;
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

        //Compruebo que el userId no sea null
        if (userId == null){
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        //Compruebo que el gameId no sea null
        if (gameId == null){
            errors.add(new ErrorDto("gameId", ErrorType.REQUERIDO));
        }
        //Compruebo que el paymentMethod no sea null
        if (paymentMethod == null){
            errors.add(new ErrorDto("paymentMethod", ErrorType.REQUERIDO));
        }

        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        //Inicio transaccion
        return tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Busco al juego y el usuario
            GameEntity game = gameRepo.getById(gameId).orElse(null);
            UserEntity user = userRepo.getById(userId).orElse(null);

            // Compruebo que el usuario exista
            if (user == null) {
                transactionErrors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }

            // Compruebo que el juego exista
            if (game == null) {
                transactionErrors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
            }

            // Si usuario y juego existen, hago validaciones extra
            if (user != null && game != null) {

                // Compruebo que el precio base sea mayor o igual a cero
                if (game.getBasePrice() < MIN_BASE_PRICE) {
                    transactionErrors.add(new ErrorDto("BasePrice", ErrorType.VALOR_DEMASIADO_BAJO));
                }

                // Compruebo descuento mínimo
                if (game.getCurrentDescount() < MIN_CURRENT_DISCOUNT) {
                    transactionErrors.add(new ErrorDto("CurrentDiscount", ErrorType.VALOR_DEMASIADO_BAJO));
                }

                // Compruebo descuento máximo
                if (game.getCurrentDescount() > MAX_CURRENT_DISCOUNT) {
                    transactionErrors.add(new ErrorDto("CurrentDiscount", ErrorType.VALOR_DEMASIADO_ALTO));
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

                    transactionErrors.add(new ErrorDto("PurchaseState", ErrorType.DUPLICADO));
                }

                transactionErrors.addAll(validate(user, game));
            }

            //Compruebo si hay errores para lanzar exepcion
            Util.throwException(transactionErrors);

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

            PurchaseEntity updatedPurchase = purchaseRepo.create(purchaseForm).orElse(null);

            Optional<UserDTO> userDTO = Optional.of(Mapper.mapFrom(user));
            Optional<GameDTO> gameDTO = Optional.of(Mapper.mapFrom(game));

            return Mapper.mapFrom(updatedPurchase, userDTO, gameDTO);
        });
    }


    /**
     * Crear una nueva transacción para adquirir un juego
     *
     * @param PurchaseId    Id de la compra que se intenta realizar
     * @return Exito en el pago o no
     *
     */
    public PurchaseDTO processPayment(Long PurchaseId) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el PurchaseId no sea null
        if (PurchaseId == null){
            errors.add(new ErrorDto("PurchaseId", ErrorType.REQUERIDO));
        }

        Util.throwException(errors);

        //Inicio Transaccion
        return tm.inTransaction(()->{
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Compruebo que la compra exista
            PurchaseEntity purchase = purchaseRepo.getById(PurchaseId).orElse(null);
            if (purchase == null) {
                transactionErrors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
            }else {
                //Compruebo que la compra este en estado pendiente de pagar
                if (purchase.getSatate() != PurchaseState.PENDIENTE) {
                    transactionErrors.add(new ErrorDto("PurchaseState", ErrorType.ESTADO_INCORRECTO));
                }
            }

            //Busco al juego y el usuario
            GameEntity game = gameRepo.getById(purchase.getIdGame()).orElse(null);
            UserEntity user = userRepo.getById(purchase.getIdUser()).orElse(null);

            // Compruebo que el usuario exista
            if (user == null) {
                transactionErrors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }

            // Compruebo que el juego exista
            if (game == null) {
                transactionErrors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
            }

            //Lanzo exepcion si hay errores
            Util.throwException(transactionErrors);

            //Obtengo el metodo de pago de la compra
            PaymentMethod purchasePM = purchase.getPaymentMethod();

            //Creo el PaymentMethod
            IPaymentMethod paymentMethod = PaymentFactory.getPaymentMethod(purchasePM);


            //Compruebo que se haya creado el PaymentMethod
            if (paymentMethod == null) {
                errors.add(new ErrorDto("PaymentMethod", ErrorType.REQUERIDO));
            }




            //Realizo el pago y la compra pasa a estado completada. Si no se puede realizar pasa a estado cancelada.
            //Si la funcion de makePayment() de los paymentMethod manda un validation exeption, quiere decir que hubo un error en el pago, asi que lo capturo en el catch
            try {
                paymentMethod.makePayment(purchase.getDiscountApplicated(), purchase.getIdUser());

                var updatedPurchase = new PurchaseUpdate(purchase.getId(), purchase.getIdUser(), purchase.getIdGame(), purchase.getPurchaseDate()
                        , purchase.getPaymentMethod(), purchase.getPriceWithoutDiscount(), purchase.getDiscountApplicated(), PurchaseState.COMPLETADA);

                purchaseRepo.update(PurchaseId, updatedPurchase);

            }catch (ValidationException e){
                var updatedPurchase = new PurchaseUpdate(purchase.getId(), purchase.getIdUser(), purchase.getIdGame(), purchase.getPurchaseDate()
                        , purchase.getPaymentMethod(), purchase.getPriceWithoutDiscount(), purchase.getDiscountApplicated(), PurchaseState.CANCELADA);

                purchaseRepo.update(PurchaseId, updatedPurchase);
            }
            //Devuelvo la compra con su PurchaseState cambiado
            PurchaseEntity updatedPurchase = purchaseRepo.getById(PurchaseId).orElse(null);

            Optional<UserDTO> userDTO = Optional.of(Mapper.mapFrom(user));
            Optional<GameDTO> gameDTO = Optional.of(Mapper.mapFrom(game));

            return Mapper.mapFrom(updatedPurchase, userDTO, gameDTO);

        });
    }


    /**
     * Ver todas las compras realizadas por un usuario
     *
     * @param userId  Id del usuario
     * @param state   estado de las compras
     * @param minDate fecha minima por la cual se va a filtrar
     * @param maxDate fecha maxima por la cual se va a filtrar
     * @return Lista de compras
     *
     */
    public List<PurchaseDTO> consultPurchasesRecord(Long userId, Optional<PurchaseState> state, Optional<LocalDate> minDate, Optional<LocalDate> maxDate) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId no sea null
        if (userId == null){
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        Util.throwException(errors);

        //Inicio Transacion
        List<PurchaseDTO> filteredPurchases = tm.inTransaction(()->{
            List<ErrorDto> transacctionErrors = new ArrayList<>();

            //Compruebo que el usuario exista
            UserEntity user = userRepo.getById(userId).orElse(null);
            if(user == null){
                transacctionErrors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }

            //Compruebo que el maxDate sea mayor que el minDate
            if (maxDate.isPresent() && minDate.isPresent()) {
                if (minDate.get().isAfter(maxDate.get())) {
                    transacctionErrors.add(new ErrorDto("PurchaseDate", ErrorType.VALOR_DEMASIADO_BAJO));
                }
            }

            //Compruebo si hay errores para mandar la validationExeption
            Util.throwException(transacctionErrors);

            //Encuentro todas las compras que haya realizado el usuario con el id pasado por parametro
            List<PurchaseEntity> foundPurchases = purchaseRepo.getAll().stream()
                    .filter(p -> Objects.equals(p.getIdUser(), userId))
                    .toList();
            //Si se pasó por parámetro un estado se filtra por estado
            if (state.isPresent()) {
                foundPurchases = foundPurchases.stream()
                        .filter(p -> p.getSatate() == state.get())
                        .toList();
            }
            //Si se pasó solo fecha mínima
            if (minDate.isPresent() && maxDate.isEmpty()) {
                foundPurchases = foundPurchases.stream()
                        .filter(p -> !p.getPurchaseDate().isBefore(minDate.get()))
                        .toList();
            }

            //Si se pasó solo fecha máxima
            if (maxDate.isPresent() && minDate.isEmpty()) {
                foundPurchases = foundPurchases.stream()
                        .filter(p -> !p.getPurchaseDate().isAfter(maxDate.get()))
                        .toList();
            }

            //Si se pasaron ambas fechas
            if (minDate.isPresent() && maxDate.isPresent()) {
                foundPurchases = foundPurchases.stream()
                        .filter(p ->
                                !p.getPurchaseDate().isBefore(minDate.get()) &&
                                        !p.getPurchaseDate().isAfter(maxDate.get()))
                        .toList();
            }


            return foundPurchases.stream()
                    .map(p -> Mapper.mapFrom(p))
                    .toList();
        });

        return filteredPurchases;
    }


    /**
     * Ver información completa de una transacción específica
     *
     * @param purchaseId id de la compra
     * @param userId     id del usuario que realizó la compra
     * @return PurchaseDTO con todos los detalles de la compra
     *
     */
    public PurchaseDTO consultPurchaseDetails(Long purchaseId, Long userId) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el PurchaseId y el userId no sean null
        if (userId == null){
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        if (purchaseId == null){
            errors.add(new ErrorDto("PurchaseId", ErrorType.REQUERIDO));
        }
        Util.throwException(errors);

        //Inicio Transaccion

        PurchaseEntity purchase = tm.inTransaction(()->{
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //busco la compra y la guardo
            PurchaseEntity pur = purchaseRepo.getById(purchaseId).orElse(null);
            //Compruebo que la compra exista
            if (pur == null) {
                transactionErrors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
            }else {
                //Compruebo que el idUser coincida con el del usuario que realizo la compra
                if (!Objects.equals(pur.getIdUser(), userId)) {
                    transactionErrors.add(new ErrorDto("UserId, PurchaseId", ErrorType.NO_ENCONTRADO));
                }
            }
            //Si hay errores lanzo exepcion
            Util.throwException(transactionErrors);
            return pur;
            //En este caso no es necesario hacer rollback porque solo se hizo una consulta a la base de datos, no se modifico nada
        });

        return Mapper.mapFrom(purchase);
    }


    /**
     * Devolver una compra y reintegrar el dinero a la cartera
     *
     * @param purchaseId id de la compra
     * @param reason     Rason por la cual el usuario quiere el reembolso
     * @return Confirmacion del reembolso
     *
     */
    public  void requestRefund(Long purchaseId, String reason) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el purchaseId no sea null
        if (purchaseId == null){
            errors.add(new ErrorDto("PurchaseId", ErrorType.REQUERIDO));
        }
        Util.throwException(errors);

        //Inicio Transaccion
        tm.inTransaction(()->{
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //busco la compra y la guardo
            PurchaseEntity purchase = purchaseRepo.getById(purchaseId).orElse(null);
            //Compruebo que la compra exista
            if (purchase == null) {
                transactionErrors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
            }else {
                //Compruebo que el estado de la compra este en completada
                if (purchase.getSatate() != PurchaseState.COMPLETADA) {
                    transactionErrors.add(new ErrorDto("PurchaseState", ErrorType.ESTADO_INCORRECTO));
                }
                //Busco una biblioteca la cual tenga la relación entre el usuario que compró el juego y el jugo
                LibraryEntity library = libraryRepo.getByUserGameId(purchase.getIdUser(), purchase.getIdGame()).orElse(null);
                //Compruebo que la biblioteca exista
                if (library == null) {
                    transactionErrors.add(new ErrorDto("LibraryIdUser, LibraryIdGame", ErrorType.NO_ENCONTRADO));
                }else {
                    //Compruebo que no se excedan los 14 dias luego de la compra del juego o que el usuario no haya jugado mas de 2 horas
                    long days = ChronoUnit.DAYS.between(library.getAcquisitionDate(), LocalDate.now());
                    if (days > REFUND_DAYS_LIMIT) {
                        transactionErrors.add(new ErrorDto("PurchaseDate", ErrorType.VALOR_DEMASIADO_ALTO));
                    }
                    if (library.getTimePlaying() > HOURS_PERMITED) {
                        transactionErrors.add(new ErrorDto("HoursPlayed", ErrorType.VALOR_DEMASIADO_ALTO));
                    }
                }
            }
            UserEntity user = null;

            if (purchase != null){
                //Busco al usuario que aparece en la compra
                user = userRepo.getById(purchase.getIdUser()).orElse(null);
                //Compruebo que el usuario que aparece en la compra exista
                if (user == null) {
                    transactionErrors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
                }
            }

            //Compruebo si hay errores para mandar la exepcion
            Util.throwException(transactionErrors);

            //obtengo cuanto el costo el juego al usuario y lo guardo
            float amount = purchase.getDiscountApplicated();
            //Creo al usuario actiualizado reponiendole el dinero
            UserUpdate userForm = new UserUpdate(user.getUserName(), user.getEmail(), user.getPassword(), user.getRealName(), user.getCountry(), user.getBirthDate(), user.getRegistrationDate(), user.getAvatar(), user.getPortfolioBalance() + amount, user.getAccountState());
            //Actualizo al usuario
            UserEntity updatedUser = userRepo.update(user.getId(), userForm).orElse(null);

            //Creo la compra actualizada con el estado en reembolsada
            PurchaseUpdate purchaseForm = new PurchaseUpdate(purchase.getId(), purchase.getIdUser(), purchase.getIdGame(), purchase.getPurchaseDate(), purchase.getPaymentMethod(), purchase.getPriceWithoutDiscount(), purchase.getDiscountApplicated(), PurchaseState.REEMBOLSADA);
            //Actualizo la compra
            PurchaseEntity updatedPurchase = purchaseRepo.update(purchaseId, purchaseForm).orElse(null);
            //Elimino la biblioteca que vincula al juego con el usuario
            boolean deleted = false;
            LibraryEntity library = libraryRepo.getByUserGameId(purchase.getIdUser(), purchase.getIdGame()).orElse(null);
            if (library != null) {
                deleted = libraryRepo.delete(library.getId());
            }

            //Compruebo que se haya actualizado el usuario
            if (updatedUser == null) {
                transactionErrors.add(new ErrorDto("User", ErrorType.NO_ACTUALIZADO));
            }
            //Compruebo si se ha actualizado la compra
            if (updatedPurchase == null) {
                transactionErrors.add(new ErrorDto("Purchase", ErrorType.NO_ACTUALIZADO));
            }
            //compruebo que se eliminó la biblioteca
            if (!deleted) {
                transactionErrors.add(new ErrorDto("Library", ErrorType.NO_ELIMINADO));

            }

            //compruebo si hay errores en las actualizaciones y lanzo las exepcion para hacer rollback en caso de algun error
            Util.throwException(transactionErrors);

            //Aqui devuelvo un booleano porque la lambda me obliga a devolver algo, pero en verdad todo lo estoy controlando con las exepciones
            return true;
        });

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

        if (game != null && user != null) {

            //Compruebo que el usuario exista en el repositorio
            if (userRepo.getById(user.getId()).isEmpty()) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }

            //Compruebo que la cuenta del usuario esté activa
            if (user.getAccountState() != AccountState.ACTIVE) {
                errors.add(new ErrorDto("AccountState", ErrorType.ESTADO_INCORRECTO));
            }

            //Compruebo que el juego exista en el sistema
            if (gameRepo.getById(game.getId()).isEmpty()) {
                errors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
            }

            //Compruebo que el juego esté disponible para comprar
            if (game.getState() == null ||
                    game.getState() == GameState.NO_DISPONIBLE) {

                errors.add(new ErrorDto("GameState", ErrorType.ESTADO_INCORRECTO));
            }
        }

        return errors;
    }

}
