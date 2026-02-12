package Controller;
import Model.Form.UserForm;
import Repository.InMemory.UserRepoInMemory;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private List<String> errores = new ArrayList<>();
    private UserRepoInMemory repository = new UserRepoInMemory();

    public List<String>  validateUser(UserForm user) {

        errores.clear();

        if (repository.obtenerTodos().stream().anyMatch(e -> e.getUserName().equals(user.getUserName()))) {
            errores.add("El nombre de usuario ya existe");
        }

        else if (repository.obtenerTodos().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            errores.add("El email ya existe");
        }

        if (!repository.getCountries().contains(user.getCountry())) {
            errores.add("Debe ingresar un pais valido");
        }

        return errores;

    }



}
