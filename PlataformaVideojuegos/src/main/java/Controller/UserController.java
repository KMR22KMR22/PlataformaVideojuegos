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


        //Nombre Usuario
        validateUserName(user);

        //Email
        validateEmail(user);

        //Contraseña
        validatePassword(user);

        //NombreReal
        validateRealName(user);

        //Pais
        validateCountry(user);

        //Fecha de nacimiento
        validateBirthDate(user);

        //Fecha de registro
        validateRegistrationDate(user);

        //Avatar
        validateAvatar(user);



        return errores;

    }

    /**
     * Valida que el nombre de usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateUserName(UserForm user) {

        if (Util.checkCadenaBlankOrEmpty(user.getUserName())) {
            errores.add("Nombre obligatorio");
        }
        else if (repository.obtenerTodos().stream().anyMatch(e -> e.getUserName().equals(user.getUserName()))) {
            errores.add("El nombre de usuario ya existe");
        }
        else if (user.getUserName().length() < 3) {
            errores.add("El nombre de usuario debe tener minimo 3 caracteres");
        }
        else if (user.getUserName().length() > 20) {
            errores.add("El nombre de usuario debe tener maximo 20 caracteres");
        }
        else if (!user.getUserName().matches("^[a-zA-Z0-9_-]+$")){
            errores.add("Solo se admiten caracteres alfanuméricos, guiones y guiones bajos");
        }
        else if (user.getUserName().matches("^[0-9].*")) {
            errores.add("El nombre de usuario no puede empezar por numero");
        }
    }

    /**
     * Valida que el email de usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateEmail(UserForm user) {

       if (Util.checkCadenaBlankOrEmpty(user.getEmail())) {
            errores.add("Email obligatorio");
        }
        else if (repository.obtenerTodos().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {

        } else if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errores.add("Debe ingresar un formato valido de email");
        }
    }

    /**
     * Valida que la contraseña se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validatePassword(UserForm user) {

        if (Util.checkCadenaBlankOrEmpty(user.getPassword())) {
            errores.add("Contraseña obligatoria");
        }
        else if (user.getPassword().length() < 8) {
            errores.add("La contraseña debe tener minimo 8 caracteres");
        }
        else if (!user.getPassword().matches(".*[A-Z].*") ||
                 !user.getPassword().matches(".*[a-z].*") ||
                 !user.getPassword().matches(".*[0-9].*")) {
            errores.add("La contraseña debe contener al menos una mayúscula, una minúscula y un número");
        }
    }

    /**
     * Valida que el nombre real del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateRealName(UserForm user) {

        if (Util.checkCadenaBlankOrEmpty(user.getRealName())) {
            errores.add("Nombre obligatorio");
        }
        else if (user.getRealName().length() < 2) {
            errores.add("El nombre debe tener mas de 2 caracteres");
        }
        else if (user.getRealName().length() > 50) {
            errores.add("El nombre no puede tener mas de 50 caracteres");
        }
    }

    /**
     * Valida que el pais del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateCountry(UserForm user){

        if (Util.checkCadenaBlankOrEmpty(user.getCountry())) {
            errores.add("Pais obligatorio");
        }
        if (!repository.getCountries().contains(user.getCountry())) {
            errores.add("Debe ingresar un pais valido");
        }

    }

    /**
     * Valida que la feha de nacimiento del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateBirthDate(UserForm user){

        if(user.getBirthDate() == null){
            errores.add("Debe ingresar su fecha de nacimiento");
        }
        if(Period.between(user.getBirthDate(), LocalDate.now()).getYears() < 13){
            errores.add("Debe tener al menos 13 años");
        }
        if(user.getBirthDate().isAfter(LocalDate.now())){
            errores.add("Fecha de nacimiento incorrecta");
        }
    }

    /**
     * Valida que la fecha de registro del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateRegistrationDate(UserForm user){

    }


    /**
     * Valida que el avatar del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateAvatar(UserForm user){
        if (user.getAvatar() != null & user.getAvatar().length() > 100) {
            errores.add("La longitud del avatar debe ser mas pequeña");
        }
    }

    /**
     * Valida que el saldo de la cartera del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateMoney(UserForm user){

        if(user.getPortfolioBalance() != 0){
            errores.add("Debe ingresar su monto");
        }
    }

}
