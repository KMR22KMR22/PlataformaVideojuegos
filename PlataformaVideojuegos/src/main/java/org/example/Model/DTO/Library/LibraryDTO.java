package org.example.Model.DTO.Library;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.User.UserDTO;

import java.time.LocalDate;
import java.util.Date;

public record LibraryDTO (
        Long id,
        Long idUser,
        UserDTO User,
        Long idGame,
        GameDTO game,
        LocalDate acquisitionDate,
        Long timePlaying,
        Date lastPlayed,
        InstalationState instalationState){
}
