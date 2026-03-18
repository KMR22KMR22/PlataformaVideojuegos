package org.example.Model.Form.Updates;

import org.example.Model.DTO.Review.ReviewState;

import java.time.LocalDate;

public record ReviewUpdate(
        Long id,
        Long idUser,
        Long idGame,
        boolean recommended,
        String reviwText,
        Long hoursPlayed,
        LocalDate publicationDate,
        LocalDate lastEditionDate,
        ReviewState state){
}
