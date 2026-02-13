package Controller;
import Model.Form.UserForm;
import Repository.InMemory.CountryRepoInMemory;
import Repository.InMemory.UserRepoInMemory;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    private List<String> errores = new ArrayList<>();
    private UserRepoInMemory userRepository = new UserRepoInMemory();
    private CountryRepoInMemory countryRepository = new CountryRepoInMemory();


    //Validaciones que dependen del usuario, pero requieren acceso a datos
    public List<String>  validateUserCreation(UserForm user) {

        errores.clear();

        if (userRepository.obtenerTodos().stream().anyMatch(e -> e.getUserName().equals(user.getUserName()))) {
            errores.add("El nombre de usuario ya existe");
        }

        else if (userRepository.obtenerTodos().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            errores.add("El email ya existe");
        }

        if (!countryRepository.getCountries().contains(user.getCountry())) {
            errores.add("Debe ingresar un pais valido");
        }

        return errores;

    }

    //Validaciones para modificaiones del usuario

    public List<String> validateUserUpdate(UserForm user) {
        errores.clear();

        //La fecha de registro no puede ser modificada por el usuario
        //El saldo debe ser positivo o cero
        //El estado de la cuenta debe ser uno entre los valores del enum

        return errores;

    }

    //Validaciones que no dependen del usuario (Reglas de inicializacion)

    //Fecha de registro automatica
    //Saldo valor por defecto 0 y maximo 2 decimales
    //Estado de la cuenta por defecto activa









}
