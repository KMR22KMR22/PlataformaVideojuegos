package org.example.model.dto.library;

import org.example.model.dto.game.GameDTO;
import org.example.model.dto.user.UserDTO;

import java.time.LocalDate;
import java.util.Date;

public record LibraryDTO(
        Long id,
        Long idUser,
        UserDTO User,
        Long idGame,
        GameDTO game,
        LocalDate acquisitionDate,
        Long timePlaying,
        Date lastPlayed,
        InstalationState instalationState) {
}
