package org.example.Controller;

import org.example.Exeptions.GenericExeption;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.DTO.Purchase.PaymentMethods;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Errors.GameErrors;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Errors.UserErrors;
import org.example.Repository.InMemory.GameRepoInMemory;
import org.example.Repository.InMemory.PurchaseRepoMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

import java.util.Arrays;

public class PurchaseController {

    private PurchaseRepoMemory purchaseRepo = new PurchaseRepoMemory();
    private GameRepoInMemory gameRepo = new GameRepoInMemory();
    private UserRepoInMemory userRepo = new UserRepoInMemory();

    public void makePurchase(UserEntity user, GameEntity game, PaymentMethods paymentMethod) {

        try {
            validate(user, game, paymentMethod);
        }catch (GenericExeption e){
            throw e;
        }
    }


    public void validate(UserEntity user, GameEntity game, PaymentMethods paymentMethod) {

        //Compuebo que exista el usuario, el juego y el metodo de pago
        if (game == null || user == null || paymentMethod == null) {throw new GenericExeption(GenericErrors.NOT_VALUE.getMessage());}

        //Compruebo que la cuenta del usuario este activa
        if(Arrays.stream(AccountState.values()).noneMatch(s -> s.equals(user.getAccountState()))){
            throw new GenericExeption(UserErrors.ACCOUNT_NOT_ACTIVE.getMessage());
        }

        //Compruebo que el usuario exista en el repositorio
        userRepo.obtenerPorId(user.getId()).orElseThrow(()  -> new GenericExeption(GenericErrors.NOT_FOUND.getMessage()));

        //Compruebo que el juego exista en el sistema
        if(gameRepo.obtenerPorId(game.getId()).isEmpty()){throw new GenericExeption(GenericErrors.NOT_FOUND.getMessage());}

        //Compruebo que le juego este en estado DISPONIBLE, PREVENTA o ACCESO_ANTICIPADO
        if(game.getState().equals(GameState.NO_DISPONIBLE) || Arrays.stream(GameState.values()).noneMatch(s -> s.equals(game.getState()))){
            throw new GenericExeption(GameErrors.INVALID_STATE.getMessage());
        }

        //Compruebo que el metodo de pago coincida con los permitidos
        if(Arrays.stream(PaymentMethods.values()).noneMatch(p -> p.equals(paymentMethod))) {
            throw new GenericExeption(GenericErrors.NOT_EXISTS.getMessage());
        }


    }

}
