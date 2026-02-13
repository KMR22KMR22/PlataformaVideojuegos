package org.example.Repository.InMemory;

import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.UserForm;
import org.example.Repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepoInMemory implements IUserRepo {
    private static final List<UserEntity> users = new ArrayList<>();
    private static Long idCounter = 1L;



    @Override
    public Optional<UserEntity> crear(UserForm form) {
        var user = new UserEntity(idCounter++, form.getUserName(), form.getEmail(), form.getPassword(), form.getRealName(), form.getCountry(), form.getBirthDate(), form.getAvatar());
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
    public Optional<UserEntity> actualizar(Long aLong, UserForm dto) {
        return Optional.empty();
    }

    @Override
    public boolean eliminar(Long id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

}
