package org.example.Repository.InMemory;

import org.example.Model.DTO.User.AccountState;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.Updates.UserUpdateForm;
import org.example.Model.Form.UserForm;
import org.example.Repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepoInMemory implements IUserRepo {
    private static final List<UserEntity> USERS = new ArrayList<>();
    private static Long NEXT_ID = 1L;



    @Override
    public Optional<UserEntity> crear(UserForm form) {
        var user = new UserEntity(NEXT_ID++, form.userName(), form.email(), form.password(), form.realName(), form.country(), form.birthDate(), form.avatar(), 0, AccountState.ACTIVE);
        USERS.add(user);
        return Optional.of(user);
    }

    @Override
    public Optional<UserEntity> obtenerPorId(Long id) {

        return USERS.stream()
                .filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public List<UserEntity> obtenerTodos() {
        return new ArrayList<>(USERS);
    }

    @Override
    public Optional<UserEntity> actualizar(Long id, UserUpdateForm form) {
        obtenerPorId(id).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

        var userUpdated = new UserEntity(id, form.userName(), form.email(), form.password(), form.realName(), form.country(), form.birthDate(), form.registrationDate(), form.avatar(), form.portfolioBalance(), form.accountState());
        USERS.removeIf(p -> p.getId().equals(id));
        USERS.add(userUpdated);
        return Optional.of(userUpdated);
    }

    @Override
    public boolean eliminar(Long id) {

        return USERS.removeIf(u -> u.getId().equals(id));
    }

}
