package org.example.Controller;

import org.example.Exeptions.ValidationException;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.DTO.Purchase.PaymentMethods;
import org.example.Model.DTO.Purchase.PurchaseDTO;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.PurchaseForm;
import org.example.Model.Form.UserForm;
import org.example.Repository.InMemory.GameRepoInMemory;
import org.example.Repository.InMemory.PurchaseRepoMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PurchaseController {

    private PurchaseRepoMemory purchaseRepo = new PurchaseRepoMemory();
    private GameRepoInMemory gameRepo = new GameRepoInMemory();
    private UserRepoInMemory userRepo = new UserRepoInMemory();

    //public PurchaseDTO makePurchase(UserEntity user, GameEntity game, PaymentMethods paymentMethod) throws ValidationException {
    //    List <ErrorDto> errores = new ArrayList<>();
//
    //    errores.addAll(validate(user, game, paymentMethod));
//
    //    if(!errores.isEmpty()){
    //        throw new ValidationException(errores);
    //    }
//
    //    float discountApplicated = game.getBasePrice()*game.getCurrentDescount()/100;
//
    //    PurchaseForm form = new PurchaseForm(user.getId(), game.getId(), paymentMethod, game.getBasePrice(), discountApplicated);
//
    //    var purchaseOpt = purchaseRepo.crear(form);
    //    var purchase = purchaseOpt.orElse(null);
//
    //    var UserDTo =
//
    //    return new PurchaseDTO(purchase.getId(), user.getId(), user, game.getId(), game, purchase.getPurchaseDate(), purchase.getPaymentMethod(), purchase.getPriceWithoutDiscount(), purchase.getDiscountApplicated(), purchase.getSatate());
    //}


    public List<ErrorDto> validate(UserEntity user, GameEntity game, PaymentMethods paymentMethod) {

        List<ErrorDto> errores = new ArrayList<>();

        //Compuebo que exista el usuario, el juego y el metodo de pago
        if (game == null || user == null || paymentMethod == null) {throw new IllegalArgumentException("Faltan parametros");}

        //Compruebo que la cuenta del usuario este activa
        if(Arrays.stream(AccountState.values()).noneMatch(s -> s.equals(user.getAccountState()))){
            errores.add(new ErrorDto("AccountState", ErrorType.FORMATO_INVALIDO));
        }

        //Compruebo que el usuario exista en el repositorio
        if(userRepo.obtenerPorId(user.getId()).isEmpty()){
            errores.add(new ErrorDto("IdUser", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que el juego exista en el sistema
        if(gameRepo.obtenerPorId(game.getId()).isEmpty()){
            errores.add(new ErrorDto("IdGame", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que le juego este en estado DISPONIBLE, PREVENTA o ACCESO_ANTICIPADO
        if(game.getState().equals(GameState.NO_DISPONIBLE) || Arrays.stream(GameState.values()).noneMatch(s -> s.equals(game.getState()))){
            errores.add(new ErrorDto("State", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que el metodo de pago coincida con los permitidos
        if(Arrays.stream(PaymentMethods.values()).noneMatch(p -> p.equals(paymentMethod))) {
            errores.add(new ErrorDto("PaimentMethod", ErrorType.NO_ENCONTRADO));
        }
        return errores;
    }

}
