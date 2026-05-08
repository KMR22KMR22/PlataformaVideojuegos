package org.example.model.paymentMethod;

import org.example.controller.Util;
import org.example.exeptions.ValidationException;
import org.example.model.entidad.UserEntity;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;
import org.example.model.form.updates.UserUpdate;
import org.example.repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;

public class SteamWallet implements IPaymentMethod {
    private IUserRepo userRepo;

    public SteamWallet(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void makePayment(float gameCost, Long userId) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();
        UserEntity user = null;

        //Compruebo que el gameCost y el userId no sean null
        if (gameCost <= 0){
            errors.add(new ErrorDto("GameCost", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (userId == null){
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }

        if (gameCost > 0 && userId != null){
            //compruebo que el usuario exista
            user = userRepo.getById(userId).orElse(null);
            if (user == null) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }else {
                //Compruebo que el usuario tenga saldo suficiente
                if (user.getPortfolioBalance() < gameCost) {
                    errors.add(new ErrorDto("Balance", ErrorType.VALOR_DEMASIADO_BAJO));
                }
            }
        }


        Util.throwException(errors);

        UserUpdate form = new UserUpdate(
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getRealName(),
                user.getCountry(),
                user.getBirthDate(),
                user.getRegistrationDate(),
                user.getAvatar(),
                user.getPortfolioBalance() - gameCost,
                user.getAccountState()
        );

        userRepo.update(user.getId(), form).orElseThrow(()-> new ValidationException(List.of(new ErrorDto("PortafolioBalance", ErrorType.NO_ACTUALIZADO))));
    }
}
