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
import org.example.model.form.updates.UserUpdate;
import org.example.model.paymentMethods.IPaymentMethod;
import org.example.repository.Interface.IGameRepo;
import org.example.repository.Interface.ILibraryRepo;
import org.example.repository.Interface.IPurchaseRepo;
import org.example.repository.Interface.IUserRepo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PurchaseController {

    public static final int REFUND_DAYS_LIMIT = 14;
    public static final int HOURS_PERMITED = 2;
    private IPurchaseRepo purchaseRepo;
    private IGameRepo gameRepo;
    private IUserRepo userRepo;
    private ILibraryRepo libraryRepo;


    //Constructor


    public PurchaseController(IPurchaseRepo purchaseRepo, IGameRepo gameRepo, IUserRepo userRepo, ILibraryRepo libraryRepo) {
        this.purchaseRepo = purchaseRepo;
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
        this.libraryRepo = libraryRepo;
    }


    /**
     * Crear una nueva transacción para adquirir un juego
     *
     * @param user          Usuario que intenta comprar
     * @param game          juego que se intenta comprar
     * @param paymentMethod metodo mediante el cual el usuario va a pagar
     * @return PurchaseDTO creada
     *
     */
    public PurchaseDTO makePurchase(UserEntity user, GameEntity game, IPaymentMethod paymentMethod) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        if (paymentMethod == null){
            errors.add(new ErrorDto("PaymentMethod", ErrorType.REQUERIDO));
        }
        if (game.getBasePrice() < 0){
            errors.add(new ErrorDto("BasePrice", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (game.getCurrentDescount() < 0){
            errors.add(new ErrorDto("CurrentDescunt", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        errors.addAll(validate(user, game));

        Util.exeptionThrower(errors);

        PurchaseForm purchaseForm = new PurchaseForm(user.getId(), game.getId(), paymentMethod, game.getBasePrice(), game.getCurrentDescount());

        var purchaseOpt = purchaseRepo.create(purchaseForm);
        var purchase = purchaseOpt.orElse(null);

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
        List<ErrorDto> errores = new ArrayList<>();

        //Compruebo que la compra exista
        PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElse(null);
        if (purchase == null) {
            errores.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
        }

        Util.exeptionThrower(errores);

        return paymentMethod.makePayment();
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

        //Compruebo que el usuario exista
        UserEntity user = userRepo.getById(idUser).orElse(null);
        if(user == null){errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));}

        Util.exeptionThrower(errors);

        List<PurchaseEntity> purchases = purchaseRepo.getAll().stream()
                .filter(p -> p.getIdUser() == idUser)
                .toList();

        if (state.isPresent()) {
            purchases = purchases.stream()
                    .filter(p -> p.getSatate() == state.get())
                    .toList();
        }

        if (minDate.isPresent() && maxDate.isPresent()) {
            purchases = purchases.stream()
                    .filter(p -> p.getPurchaseDate().isAfter(minDate.get()) && p.getPurchaseDate().isBefore(maxDate.get()))
                    .toList();
        }
        return purchases;
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
        PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElse(null);

        if (purchase == null) {
            errors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
        }

        if (purchase.getIdUser() != idUser) {
            errors.add(new ErrorDto("UserId, PurchaseId", ErrorType.NO_ENCONTRADO));
        }

        Util.exeptionThrower(errors);

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
    public boolean requestRefound(Long idPurchase, String reason) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElse(null);
        if (purchase == null) {
            errors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
        }
        LibraryEntity library = libraryRepo.getByUserGameId(purchase.getIdUser(), purchase.getIdGame()).orElse(null);
        if (library == null) {
            errors.add(new ErrorDto("LibraryIdUser, LibraryIdGame", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que no se exedan los 14 dias luego de la compra del juego o que el usuario no haya jugado mas de 2 horas
        long days = ChronoUnit.DAYS.between(library.getAcquisitionDate(), LocalDate.now());
        if (days > REFUND_DAYS_LIMIT) {
            errors.add(new ErrorDto("PurchaseDate", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        if (library.getTimePlaying() > HOURS_PERMITED) {
            errors.add(new ErrorDto("HoursPlayed", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        UserEntity user = userRepo.getById(purchase.getIdUser()).orElse(null);
        if (user == null) {
            errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
        }

        Util.exeptionThrower(errors);

        float amount = purchase.getDiscountApplicated();

        UserUpdate userForm = new UserUpdate(user.getUserName(), user.getEmail(), user.getPassword(), user.getRealName(), user.getCountry(), user.getBirthDate(), user.getRegistrationDate(), user.getAvatar(), user.getPortfolioBalance() + amount, user.getAccountState());

        userRepo.update(user.getId(), userForm).orElseThrow(() -> new IllegalArgumentException("No se pudo realizar el reembolso"));

        return purchaseRepo.delete(idPurchase);
    }


    /**
     * Devolver una compra y reintegrar el dinero a la cartera
     *
     * @param idPurchase id de la compra
     * @return PurchaseDTO con los datos de la compra
     *
     */
    public PurchaseDTO generateBill(Long idPurchase) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElse(null);
        if (purchase == null) {
            errors.add(new ErrorDto("PurchaseId", ErrorType.NO_ENCONTRADO));
        }
        Util.exeptionThrower(errors);

        return Mapper.mapFrom(purchase);
    }

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
