package org.example.model.dto.user;

import java.time.LocalDate;

public record UserDTO(Long id,
                      String userName,
                      String email,
                      String realName,
                      String country,
                      LocalDate birthDate,
                      LocalDate registrationDate,
                      String avatar,
                      float portfolioBalance,
                      AccountState accountState) {

}
