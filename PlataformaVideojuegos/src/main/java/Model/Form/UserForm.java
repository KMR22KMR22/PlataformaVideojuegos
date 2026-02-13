package Model.Form;

import Controller.Util;
import Model.DTO.User.CountState;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class UserForm {

    private Long id;
    private String userName;
    private String email;
    private String password;
    private String realName;
    private String country;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    public String avatar;
    private float portfolioBalance;
    private CountState accountState;

    private List<String> errores = new ArrayList<>();


    //Getters


    public Long getId() {
        return id;
    }

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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public float getPortfolioBalance() {
        return portfolioBalance;
    }

    public CountState getAccountState() {
        return accountState;
    }

    public String getCountry() {
        return country;
    }

    public String getAvatar() {
        return avatar;
    }

    //Constructor
    public UserForm(Long id, String userName, String email, String password, String realName, String country, LocalDate birthDate, LocalDate registrationDate, String avatar, float portfolioBalance, CountState accountState) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.country = country;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.avatar = avatar;
        this.portfolioBalance = portfolioBalance;
        this.accountState = accountState;
    }



    //Validaciones

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

        //Avatar
        validateAvatar(user);

        //Saldo de Cartera
        validateMoney(user);

        //Estado de la cuenta
        validateAccountState(user);



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
        else if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
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
     * Valida que el avatar del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateAvatar(UserForm user){
        if (user.getAvatar() != null){
            if(user.getAvatar().length() > 100){
                errores.add("La longitud del avatar debe ser mas pequeña");
            }
        }
    }

    /**
     * Valida que el saldo de la cartera del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public void validateMoney(UserForm user){


    }

    /**
     * Valida que el estado de la cuenta del usuario se haya introducido correctamente
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private void validateAccountState(UserForm user) {

    }

}



