package org.example.Model.DTO.User;

import java.time.LocalDate;

public class UserDTO {

    public Long id;
    public String userName;
    public String email;
    public String realName;
    public String country;
    public LocalDate birthDate;
    public LocalDate registrationDate;
    public String avatar;
    public float portfolioBalance;
    public AccountState accountState;




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

    public void setPortfolioBalance(float portfolioBalance) {
        this.portfolioBalance = portfolioBalance;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }


    //Constructor

    public UserDTO(Long id, String userName, String email, String realName, String country, LocalDate birthDate, LocalDate registrationDate, String avatar, float portfolioBalance, AccountState accountState) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.realName = realName;
        this.country = country;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.avatar = avatar;
        this.portfolioBalance = portfolioBalance;
        this.accountState = accountState;
    }
}
