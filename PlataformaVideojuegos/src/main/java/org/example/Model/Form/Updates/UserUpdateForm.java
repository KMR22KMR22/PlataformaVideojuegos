package org.example.Model.Form.Updates;

import org.example.Model.DTO.User.AccountState;

import java.time.LocalDate;

public record UserUpdateForm (
        String userName,
        String email,
        String password,
        String realName,
        String country,
        LocalDate birthDate,
        LocalDate registrationDate,
        String avatar,
        float portfolioBalance,
        AccountState accountState){
}
