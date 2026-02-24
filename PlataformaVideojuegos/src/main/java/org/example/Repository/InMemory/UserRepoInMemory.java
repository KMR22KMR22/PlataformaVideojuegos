package org.example.Repository.InMemory;

import org.example.Exeptions.GenericExeption;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Errors.UserErrors;
import org.example.Model.Form.UserForm;
import org.example.Repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepoInMemory implements IUserRepo {
    private final List<UserEntity> users = new ArrayList<>();
    private static Long idCounter = 1L;



    @Override
    public Optional<UserEntity> crear(UserForm form) {
        var user = new UserEntity(idCounter++, form.getUserName(), form.getEmail(), form.getPassword(), form.getRealName(), form.getCountry(), form.getBirthDate(), form.getAvatar(), 0, AccountState.ACTIVE);
        users.add(user);
        return Optional.of(user);
    }

    @Override
    public Optional<UserEntity> obtenerPorId(Long id) {

        return users.stream()
                .filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public List<UserEntity> obtenerTodos() {
        return new ArrayList<>(users);
    }

    @Override
    public UserEntity updatePortafolioBalance(Long id, Optional<Float> money) {
        UserEntity userOpt = obtenerPorId(id).orElseThrow(() -> new GenericExeption(GenericErrors.NOT_EXISTS.getMessage()));

        //Compruebo de la cuenta de el usuario que se encontro este activa
        if(!userOpt.getAccountState().equals(AccountState.ACTIVE)){
            throw new GenericExeption(UserErrors.ACCOUNT_NOT_ACTIVE.getMessage());
        }

        float ammount = money.orElseThrow(() -> new GenericExeption(GenericErrors.NOT_VALUE.getMessage()));

        //Compruebo que la cantidad de saldo que intenta agregar el usuario esta entre 5 y 500
        if(ammount < 5 || ammount > 500){
            throw new GenericExeption(UserErrors.INVALID_IMMPORT.getMessage());
        }

        float newbalance = userOpt.getPortfolioBalance() + ammount;

        var usuarioActualizado = new UserEntity(id, userOpt.getUserName(), userOpt.getEmail(), userOpt.getPassword(), userOpt.getRealName(), userOpt.getCountry(), userOpt.getBirthDate(), userOpt.getAvatar(), newbalance, AccountState.ACTIVE);
        users.removeIf(u -> u.getId().equals(id));
        users.add(usuarioActualizado);

        return usuarioActualizado;
    }

    @Override
    public boolean eliminar(Long id) {

        return users.removeIf(u -> u.getId().equals(id));
    }

}
