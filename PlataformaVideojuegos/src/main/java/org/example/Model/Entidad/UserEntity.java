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


    //Constructor

    public UserEntity(Long id, String userName, String email, String password, String realName, String country, LocalDate birthDate, String avatar) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.country = country;
        this.birthDate = birthDate;
        this.registrationDate = LocalDate.now();
        this.avatar = avatar;
        this.portfolioBalance = 0;
        this.accountState = AccountState.ACTIVE;
    }
}


