package org.example.Model.Entidad;

import org.example.Model.DTO.User.AccountState;

import java.time.LocalDate;

public class UserEntity {


    private Long id;
    private String userName;
    private String email;
    private String password;
    private String realName;
    private String country;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private String avatar;
    private float portfolioBalance;
    private AccountState accountState;



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

    public AccountState getAccountState() {
        return accountState;
    }

    public String getCountry() {
        return country;
    }

    public String getAvatar() {
        return avatar;
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

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPortfolioBalance(float portfolioBalance) {
        this.portfolioBalance = portfolioBalance;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }


    //Constructor

    public UserEntity(Long id, String userName, String email, String password, String realName, String country, LocalDate birthDate, String avatar, float portfolioBalance, AccountState accountState) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.country = country;
        this.birthDate = birthDate;
        this.registrationDate = LocalDate.now();
        this.avatar = avatar;
        this.portfolioBalance = portfolioBalance;
        this.accountState = accountState;
    }
}


