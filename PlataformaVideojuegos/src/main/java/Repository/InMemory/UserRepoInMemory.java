package Repository.InMemory;

import Model.Entidad.UserEntity;
import Model.Form.UserForm;
import Repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepoInMemory implements IUserRepo {
    private static final List<UserEntity> users = new ArrayList<>();
    private static Long idCounter = 1L;


    @Override
    public Optional<UserEntity> crear(UserForm form) {
        var user = new UserEntity(idCounter++, form.userName, form.email, form.password, form.realName, form.country, form.birthDate, form.registrationDate, form.avatar, form.portfolioBalance, form.accountState);
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
    public boolean eliminar(Long aLong) {
        return false;
    }
}
