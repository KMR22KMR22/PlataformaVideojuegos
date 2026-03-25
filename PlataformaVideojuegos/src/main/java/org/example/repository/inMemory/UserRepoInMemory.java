package org.example.repository.inMemory;

import org.example.model.entidad.UserEntity;
import org.example.model.form.updates.UserUpdate;
import org.example.model.form.UserForm;
import org.example.repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepoInMemory implements IUserRepo {
    private static final List<UserEntity> USERS = new ArrayList<>();
    private static Long NEXT_ID = 1L;



    @Override
    public Optional<UserEntity> create(UserForm form) {
        var user = new UserEntity(NEXT_ID++, form.userName(), form.email(), form.password(), form.realName(), form.country(), form.birthDate(), form.avatar(), 0);
        USERS.add(user);
        return Optional.of(user);
    }

    @Override
    public Optional<UserEntity> getById(Long id) {

        return USERS.stream()
                .filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public List<UserEntity> getAll() {
        return new ArrayList<>(USERS);
    }

    @Override
    public Optional<UserEntity> update(Long id, UserUpdate form) {
        getById(id).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

        var userUpdated = new UserEntity(id, form.userName(), form.email(), form.password(), form.realName(), form.country(), form.birthDate(), form.registrationDate(), form.avatar(), form.portfolioBalance(), form.accountState());
        USERS.removeIf(p -> p.getId().equals(id));
        USERS.add(userUpdated);
        return Optional.of(userUpdated);
    }

    @Override
    public boolean delete(Long id) {

        return USERS.removeIf(u -> u.getId().equals(id));
    }

}
