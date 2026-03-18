package org.example.Controller;

import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.DTO.Purchase.PurchaseDTO;
import org.example.Model.DTO.Purchase.PurchaseState;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.PurchaseEntity;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.PurchaseForm;
import org.example.Model.Form.Updates.UserUpdate;
import org.example.Model.PaymentMethods.IPaymentMethod;
import org.example.Repository.Interface.IGameRepo;
import org.example.Repository.Interface.IPurchaseRepo;
import org.example.Repository.Interface.IUserRepo;

import java.time.LocalDate;
import java.util.*;

public class PurchaseController {

    private IPurchaseRepo purchaseRepo;
    private IGameRepo gameRepo;
    private IUserRepo userRepo;


    //Constructor


    public PurchaseController(IPurchaseRepo purchaseRepo, IGameRepo gameRepo, IUserRepo userRepo) {
        this.purchaseRepo = purchaseRepo;
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
    }

    /**Crear una nueva transacción para adquirir un juego
     * @param user Usuario que intenta comprar
     * @param game juego que se intenta comprar
     * @param paymentMethod metodo mediante el cual el usuario va a pagar
     * @return PurchaseDTO creada
     * */
    public PurchaseDTO makePurchase(UserEntity user, GameEntity game, IPaymentMethod paymentMethod) throws ValidationException {
        List <ErrorDto> errores = new ArrayList<>();

        errores.addAll(validate(user, game));

        if(!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        PurchaseForm purchaseForm = new PurchaseForm(user.getId(), game.getId(), paymentMethod, game.getBasePrice(), game.getCurrentDescount());

        var purchaseOpt = purchaseRepo.create(purchaseForm);
        var purchase = purchaseOpt.orElse(null);

        return Mapper.mapFrom(purchase);
    }


    /**Crear una nueva transacción para adquirir un juego
     * @param idPurchase Id de la compra que se intenta realizar
     * @param paymentMethod metodo mediante el cual el usuario va a pagar
     * @return Exito en el pago o no
     * */
    public boolean processPayment(Long idPurchase, IPaymentMethod paymentMethod){
        //Compruebo que la compra exista
        purchaseRepo.getById(idPurchase).orElseThrow(() -> new IllegalArgumentException("La compra no existe"));

        return paymentMethod.makePayment();
    }


    /**Ver todas las compras realizadas por un usuario
     * @param idUser Id del usuario
     * @param state estado de las compras
     * @param minDate fecha minima por la cual se va a filtrar
     * @param maxDate fecha maxima por la cual se va a filtrar
     * @return Lista de compras
     * */
    public List<PurchaseEntity> consultPurchasesRecord(Long idUser, Optional<PurchaseState> state, Optional<LocalDate> minDate, Optional<LocalDate> maxDate){
        //Compruebo que el usuario exista
        userRepo.getById(idUser).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<PurchaseEntity> purchases = purchaseRepo.getAll().stream()
                .filter(p -> p.getIdUser() == idUser)
                .toList();

        if(state.isPresent()){
            purchases = purchases.stream()
                    .filter(p -> p.getSatate() == state.get())
                    .toList();
        }

        if(minDate.isPresent() && maxDate.isPresent()){
            purchases = purchases.stream()
                    .filter(p -> p.getPurchaseDate().isAfter(minDate.get()) && p.getPurchaseDate().isBefore(maxDate.get()))
                    .toList();
        }
        return purchases;
    }

    
    /** Ver información completa de una transacción específica
     * @param idPurchase id de la compra
     * @param idUser id del usuario que realizó la compra
     * @return PurchaseDTO con todos los detalles de la compra
     * */
    public PurchaseDTO consultPurchaseDetails(Long idPurchase, Long idUser){
        PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElseThrow(() -> new IllegalArgumentException("La compra no existe"));

        if(purchase.getIdUser() != idUser){throw new IllegalArgumentException("El usuario y la compra no coinciden");}

        return Mapper.mapFrom(purchase);
    }


    /** Devolver una compra y reintegrar el dinero a la cartera
     * @param idPurchase id de la compra
     * @param reason Rason por la cual el usuario quiere el reembolso
     * @return Confirmacion del reembolso
     * */
    public boolean requestRefound(Long idPurchase, String reason){

        PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElseThrow(() -> new IllegalArgumentException("La compra no existe"));

        //Compruebo que no se haya vencido la fecha de reembolso
        if(purchase.getPurchaseDate().isBefore){throw new IllegalArgumentException("Plazo para reembolso expiró");}

        UserEntity user = userRepo.getById(purchase.getIdUser()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        float amount = purchase.getDiscountApplicated();

        UserUpdate userForm = new UserUpdate(user.getUserName(), user.getEmail(), user.getPassword(), user.getRealName(), user.getCountry(), user.getBirthDate(), user.getRegistrationDate(), user.getAvatar(), user.getPortfolioBalance()+amount, user.getAccountState());

        userRepo.update(user.getId(), userForm).orElseThrow(() -> new IllegalArgumentException("No se pudo realizar el reembolso"));

        return purchaseRepo.delete(idPurchase);
    }


    /** Devolver una compra y reintegrar el dinero a la cartera
     * @param idPurchase id de la compra
     * @return PurchaseDTO con los datos de la compra
     * */
    public PurchaseDTO generateBill(Long idPurchase){
        PurchaseEntity purchase = purchaseRepo.getById(idPurchase).orElseThrow(() -> new IllegalArgumentException("La compra no existe"));

        return Mapper.mapFrom(purchase);
    }

    /**Realiza las validaciones del PurchaseForm que necesitan acceso a datos
     * @param user Usuario que intenta comprar
     * @param game juego que se intenta comprar
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ErrorDto> validate(UserEntity user, GameEntity game) {

        List<ErrorDto> errores = new ArrayList<>();

        //Compuebo que exista el usuario, el juego y el metodo de pago
        if (game == null || user == null) {throw new IllegalArgumentException("Faltan parametros");}

        //Compruebo que la cuenta del usuario este activa
        if(Arrays.stream(AccountState.values()).noneMatch(s -> s.equals(user.getAccountState()))){
            errores.add(new ErrorDto("AccountState", ErrorType.FORMATO_INVALIDO));
        }

        //Compruebo que el usuario exista en el repositorio
        if(userRepo.getById(user.getId()).isEmpty()){
            errores.add(new ErrorDto("IdUser", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que el juego exista en el sistema
        if(gameRepo.getById(game.getId()).isEmpty()){
            errores.add(new ErrorDto("IdGame", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que le juego este en estado DISPONIBLE, PREVENTA o ACCESO_ANTICIPADO
        if(game.getState().equals(GameState.NO_DISPONIBLE) || Arrays.stream(GameState.values()).noneMatch(s -> s.equals(game.getState()))){
            errores.add(new ErrorDto("State", ErrorType.NO_ENCONTRADO));
        }

        return errores;
    }

}
