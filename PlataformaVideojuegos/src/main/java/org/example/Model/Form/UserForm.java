package org.example.Model.Form;

import org.example.Controller.Util;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;


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
    public List<ErrorDto>  validate() {

        List<ErrorDto> errores = new ArrayList<>();


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
    public List<ErrorDto> validateUserName() {
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(userName)) {
            errores.add(new ErrorDto("Nombre", ErrorType.REQUERIDO));
        }
        if (userName.length() < 3) {
            errores.add(new ErrorDto("Nombre", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (userName.length() > 20) {
            errores.add(new ErrorDto("Nombre", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        if (!userName.matches("^[a-zA-Z0-9_-]+$")){
            errores.add(new ErrorDto("Nombre", ErrorType.FORMATO_INVALIDO));

        }
        if (userName.matches("^[0-9].*")) {
            errores.add(new ErrorDto("Nombre", ErrorType.FORMATO_INVALIDO));
        }

        return errores;
    }

    /**
     * Valida que el email de usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<ErrorDto> validateEmail() {
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(email)) {
            errores.add(new ErrorDto("Email", ErrorType.REQUERIDO));
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errores.add(new ErrorDto("Email", ErrorType.FORMATO_INVALIDO));
        }
        return errores;
    }

    /**
     * Valida que la contraseña se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<ErrorDto> validatePassword() {
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(password)) {
            errores.add(new ErrorDto("Password", ErrorType.REQUERIDO));
        }
        if (password.length() < 8) {
            errores.add(new ErrorDto("Password", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        if (!password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[0-9].*")) {
            errores.add(new ErrorDto("Password", ErrorType.FORMATO_INVALIDO));
        }

        return errores;
    }

    /**
     * Valida que el nombre real del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<ErrorDto> validateRealName() {
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(realName)) {
            errores.add(new ErrorDto("RealName", ErrorType.REQUERIDO));
        }
        if (realName.length() < 2) {
            errores.add(new ErrorDto("RealName", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (realName.length() > 50) {
            errores.add(new ErrorDto("RealName", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        return errores;
    }

    /**
     * Valida que el pais del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<ErrorDto> validateCountry(){
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(country)) {
            errores.add(new ErrorDto("Country", ErrorType.REQUERIDO));
        }
        return errores;
    }

    /**
     * Valida que la feha de nacimiento del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<ErrorDto> validateBirthDate(){
        List<ErrorDto> errores = new ArrayList<>();

        if(birthDate == null){
            errores.add(new ErrorDto("BirthDate", ErrorType.REQUERIDO));
        }
        if(Period.between(birthDate, LocalDate.now()).getYears() < 13){
            errores.add(new ErrorDto("BirthDate", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(birthDate.isAfter(LocalDate.now())){
            errores.add(new ErrorDto("BirthDate", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        return errores;
    }




    /**
     * Valida que el avatar del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<ErrorDto> validateAvatar(){
        List<ErrorDto> errores = new ArrayList<>();

        if (avatar != null){
            if(avatar.length() > 100){
                errores.add(new ErrorDto("Avatar", ErrorType.VALOR_DEMASIADO_ALTO));
            }
        }
        return errores;
    }

}



