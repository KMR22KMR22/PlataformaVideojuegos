package org.example.Model.DTO.Review;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.User.UserDTO;

import java.time.LocalDate;

public record ReviewDTO (
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
        ReviewState state){
}
