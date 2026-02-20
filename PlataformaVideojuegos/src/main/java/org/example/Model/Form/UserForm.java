package org.example.Model.Form;

import org.example.Controller.Util;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Errors.UserErrors;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class UserForm {

    private String userName;
    private String email;
    private String password;
    private String realName;
    private String country;
    private LocalDate birthDate;
    public String avatar;



    //Getters



    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRealName() {
        return realName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getCountry() {
        return country;
    }

    public String getAvatar() {
        return avatar;
    }

    //Constructor
    public UserForm(String userName, String email, String password, String realName, String country, LocalDate birthDate, String avatar) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.country = country;
        this.birthDate = birthDate;
        this.avatar = avatar;

    }



    //Validaciones
    /**
     * Valida que los datos del juego se hayan introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String>  validate() {

        List<String> errores = new ArrayList<>();


        //Nombre Usuario
        errores.addAll(validateUserName());

        //Email
        errores.addAll(validateEmail());

        //Contraseña
        errores.addAll(validatePassword());

        //NombreReal
        errores.addAll(validateRealName());

        //Pais
        errores.addAll(validateCountry());

        //Fecha de nacimiento
        errores.addAll(validateBirthDate());

        //Avatar
        errores.addAll(validateAvatar());


        return errores;

    }

    /**
     * Valida que el nombre de usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateUserName() {
        List<String> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(userName)) {
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if (userName.length() < 3) {
            errores.add(GenericErrors.TOO_SHORT.getMessage());
        }
        if (userName.length() > 20) {
            errores.add(GenericErrors.TOO_LONG.getMessage());
        }
        if (!userName.matches("^[a-zA-Z0-9_-]+$")){
            errores.add(UserErrors.USERNAME_INVALID_FORMAT.getMessage());
        }
        if (userName.matches("^[0-9].*")) {
            errores.add(UserErrors.USERNAME_STARTS_WITH_NUMBER.getMessage());
        }

        return errores;
    }

    /**
     * Valida que el email de usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateEmail() {
        List<String> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(email)) {
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errores.add(GenericErrors.INVALID_FORMAT.getMessage());
        }
        return errores;
    }

    /**
     * Valida que la contraseña se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validatePassword() {
        List<String> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(password)) {
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if (password.length() < 8) {
            errores.add(GenericErrors.TOO_SHORT.getMessage());
        }
        if (!password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[0-9].*")) {
            errores.add(UserErrors.PASSWORD_WEAK.getMessage());
        }

        return errores;
    }

    /**
     * Valida que el nombre real del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateRealName() {
        List<String> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(realName)) {
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if (realName.length() < 2) {
            errores.add(GenericErrors.TOO_SHORT.getMessage());
        }
        if (realName.length() > 50) {
            errores.add(GenericErrors.TOO_LONG.getMessage());
        }
        return errores;
    }

    /**
     * Valida que el pais del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateCountry(){
        List<String> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(country)) {
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        return errores;
    }

    /**
     * Valida que la feha de nacimiento del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateBirthDate(){
        List<String> errores = new ArrayList<>();

        if(birthDate == null){
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if(Period.between(birthDate, LocalDate.now()).getYears() < 13){
            errores.add(UserErrors.BIRTHDATE_UNDERAGE.getMessage());
        }
        if(birthDate.isAfter(LocalDate.now())){
            errores.add(UserErrors.BIRTHDATE_FUTURE.getMessage());
        }
        return errores;
    }




    /**
     * Valida que el avatar del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateAvatar(){
        List<String> errores = new ArrayList<>();

        if (avatar != null){
            if(avatar.length() > 100){
                errores.add(UserErrors.AVATAR_TOO_LONG.getMessage());
            }
        }
        return errores;
    }

}



