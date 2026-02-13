package org.example.Model.Form;

import org.example.Controller.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public List<String>  validateUser(UserForm user) {

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

        //Saldo de Cartera
        errores.addAll(validateMoney());

        //Estado de la cuenta
        errores.addAll(validateAccountState());



        return errores;

    }

    /**
     * Valida que el nombre de usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateUserName() {
        List<String> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(userName)) {
            errores.add("Nombre obligatorio");
        }
        if (userName.length() < 3) {
            errores.add("El nombre de usuario debe tener minimo 3 caracteres");
        }
        if (userName.length() > 20) {
            errores.add("El nombre de usuario debe tener maximo 20 caracteres");
        }
        if (!userName.matches("^[a-zA-Z0-9_-]+$")){
            errores.add("Solo se admiten caracteres alfanuméricos, guiones y guiones bajos");
        }
        if (userName.matches("^[0-9].*")) {
            errores.add("El nombre de usuario no puede empezar por numero");
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
            errores.add("Email obligatorio");
        }
        else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errores.add("Debe ingresar un formato valido de email");
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
            errores.add("Contraseña obligatoria");
        }
        else if (password.length() < 8) {
            errores.add("La contraseña debe tener minimo 8 caracteres");
        }
        else if (!password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[0-9].*")) {
            errores.add("La contraseña debe contener al menos una mayúscula, una minúscula y un número");
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
            errores.add("Nombre obligatorio");
        }
        else if (realName.length() < 2) {
            errores.add("El nombre debe tener mas de 2 caracteres");
        }
        else if (realName.length() > 50) {
            errores.add("El nombre no puede tener mas de 50 caracteres");
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
            errores.add("Pais obligatorio");
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
            errores.add("Debe ingresar su fecha de nacimiento");
        }
        if(Period.between(birthDate, LocalDate.now()).getYears() < 13){
            errores.add("Debe tener al menos 13 años");
        }
        if(birthDate.isAfter(LocalDate.now())){
            errores.add("Fecha de nacimiento incorrecta");
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
                errores.add("La longitud del avatar debe ser mas pequeña");
            }
        }
        return errores;
    }

    /**
     * Valida que el saldo de la cartera del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> validateMoney(){
        List<String> errores = new ArrayList<>();

        return errores;
    }

    /**
     * Valida que el estado de la cuenta del usuario se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<String> validateAccountState() {
        List<String> errores = new ArrayList<>();

        return errores;
    }

}



