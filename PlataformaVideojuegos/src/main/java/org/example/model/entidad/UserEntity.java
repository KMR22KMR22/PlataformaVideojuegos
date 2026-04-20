package org.example.model.entidad;

import jakarta.persistence.*;
import org.example.model.dto.user.AccountState;

import java.time.LocalDate;

@Table(name = "usuarios")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre_usuario")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "contraseña")
    private String password;
    @Column(name = "nombre_real")
    private String realName;
    @Column(name = "pais")
    private String country;
    @Column(name = "fecha_nacimiento")
    private LocalDate birthDate;
    @Column(name = "fecha_registro")
    private LocalDate registrationDate;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "saldo")
    private float portfolioBalance;
    @Column(name = "estado_cuenta")
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

    //Constructor Actualizacion

    public UserEntity(Long id, String userName, String email, String password, String realName, String country, LocalDate birthDate, LocalDate registrationDate, String avatar, float portfolioBalance, AccountState accountState) {
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


    //ConstructorCreacion

    public UserEntity(Long id, String userName, String email, String password, String realName, String country, LocalDate birthDate, String avatar, float portfolioBalance) {
        this(id, userName, email, password, realName, country, birthDate, LocalDate.now(), avatar, portfolioBalance, AccountState.ACTIVE);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", country='" + country + '\'' +
                ", birthDate=" + birthDate +
                ", registrationDate=" + registrationDate +
                ", avatar='" + avatar + '\'' +
                ", portfolioBalance=" + portfolioBalance +
                ", accountState=" + accountState +
                '}';
    }
}


