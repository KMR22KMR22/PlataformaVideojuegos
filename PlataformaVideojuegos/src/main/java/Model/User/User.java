package Model.User;

import java.time.LocalDate;

public class User {

    private int id;
    private String userName;
    private String email;
    private String password;
    private String realName;
    private Countries country;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private float portfolioBalance;
    private CountState accountState;




    //Getters


    public int getId() {
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

    public Enum getAccountState() {
        return accountState;
    }

    public Countries getCountry() {
        return country;
    }

    //Setters

    public void setId(int id) {
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

    public void setCountry(Countries country) {
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

    public void setAccountState(CountState accountState) {
        this.accountState = accountState;
    }


    //Constructor


    public User(int id, String userName, String email, String password, String realName, Countries country, LocalDate birthDate, LocalDate registrationDate, float portfolioBalance, CountState accountState) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.country = country;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.portfolioBalance = portfolioBalance;
        this.accountState = accountState;
    }
}
