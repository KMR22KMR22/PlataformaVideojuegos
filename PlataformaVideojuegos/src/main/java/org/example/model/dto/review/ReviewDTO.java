package org.example.model.dto.review;

import org.example.model.dto.game.GameDTO;
import org.example.model.dto.user.UserDTO;

import java.time.LocalDate;

public record ReviewDTO(
        Long id,
        Long idUser,
        UserDTO user,
        Long idGame,
        GameDTO game,
        boolean recommended,
        String reviwText,
        Long hoursPlayed,
        LocalDate publicationDate,
        LocalDate lastEditionDate,
        ReviewState state) {
}
