package Model.Form;

import Model.DTO.User.CountState;
import Model.DTO.User.Countries;

import java.time.LocalDate;

public class UserForm {

    public Long id;
    public String userName;
    public String email;
    public String password;
    public String realName;
    public Countries country;
    public LocalDate birthDate;
    public LocalDate registrationDate;
    public String avatar;
    public float portfolioBalance;
    public CountState accountState;

}
