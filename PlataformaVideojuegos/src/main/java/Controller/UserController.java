package main.java.Controller;
import main.java.Model.DTO.User.UserForm;
import main.java.Repository.InMemory.UserRepoInMemory;

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
        //validateCountry(userDTO);

        //Fecha de nacimiento
        validateBirthDate(user);



        return errores;

    }

    /**
     * Valida que el nombre de usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateUserName(UserForm user) {

        if (Util.checkCadena(user.getUserName())) {
            errores.add("Nombre obligatorio");
        }
        else if (repository.obtenerTodos().stream().filter(u -> u.getUserName().equals(user.getUserName())).findFirst().isPresent()) {
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
        return errores;
    }

    /**
     * Valida que el email de usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateEmail(UserForm user) {

       if (Util.checkCadena(user.getEmail())) {
            errores.add("Email obligatorio");
        }
        else if (repository.obtenerTodos().stream().filter(u -> u.getEmail().equals(user.getEmail())).findFirst().isPresent()) {

        } else if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errores.add("Debe ingresar un formato valido de email");
        }
        return errores;
    }

    /**
     * Valida que la contraseña se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validatePassword(UserForm user) {

        if (Util.checkCadena(user.getPassword())) {
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
        return errores;
    }

    /**
     * Valida que el nombre real del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateRealName(UserForm user) {

        if (Util.checkCadena(user.getRealName())) {
            errores.add("Nombre obligatorio");
        }
        else if (user.getRealName().length() < 2) {
            errores.add("El nombre debe tener mas de 2 caracteres");
        }
        else if (user.getRealName().length() > 50) {
            errores.add("El nombre no puede tener mas de 50 caracteres");
        }
        return errores;
    }

    /**
     * Valida que el pais del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
   // public List<String> validateCountry(UserDTO user){

//        if (Util.sheckCadena(user.getCountry())) {
  //          errores.add("nombre obligatorio");
    //    }
    //}

    /**
     * Valida que la feha de nacimiento del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateBirthDate(UserForm user){

        if(user.getBirthDate() == null){
            errores.add("Debe ingresar su fecha de nacimiento");
        }
        if(Period.between(user.getBirthDate(), LocalDate.now()).getYears() < 13){
            errores.add("Debe tener al menos 13 años");
        }
        if(user.getBirthDate().isAfter(LocalDate.now())){
            errores.add("Fecha de nacimiento incorrecta");
        }
        return errores;
    }

    /**
     * Valida que la fecha de registro del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    //public List<String> validateRegistrationDate(UserDTO user){


    //}

    /**
     * Valida que el avatar del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    //public List<String> validateAvatar(UserDTO user){errores.clear();}

    /**
     * Valida que el saldo de la cartera del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateMoney(UserForm user){

        if(user.getPortfolioBalance() != 0){
            errores.add("Debe ingresar su monto");
        }
    }

}
