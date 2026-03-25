package org.example.model.form.updates;

import org.example.model.dto.user.AccountState;

import java.time.LocalDate;

public record UserUpdate(
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
