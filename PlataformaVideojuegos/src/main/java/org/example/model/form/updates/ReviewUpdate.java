package org.example.model.form.updates;

import org.example.model.dto.review.ReviewState;

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
        ReviewState state) {
}
