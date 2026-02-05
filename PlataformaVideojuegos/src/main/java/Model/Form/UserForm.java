package main.java.Model.Form;

import main.java.Model.DTO.User.CountState;
import main.java.Model.DTO.User.Countries;
import main.java.Model.DTO.User.CountState;
import main.java.Model.DTO.User.Countries;

import java.time.LocalDate;

public class UserForm {

    private Long id;
    private String userName;
    private String email;
    private String password;
    private String realName;
    private Countries country;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    public String avatar;
    private float portfolioBalance;
    private CountState accountState;


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

    public main.java.Model.DTO.User.Countries getCountry() {
        return country;
    }

    //Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setCountry(main.java.Model.DTO.User.Countries country) {
        this.country = country;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setPortfolioBalance(float portfolioBalance) {
        this.portfolioBalance = portfolioBalance;
    }

    public void setAccountState(main.java.Model.DTO.User.CountState accountState) {
        this.accountState = accountState;
    }


    //Constructor


    public UserForm(Long id, String userName, String email, String password, String realName, Countries country, LocalDate birthDate, LocalDate registrationDate, String avatar, float portfolioBalance, CountState accountState) {
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
}


