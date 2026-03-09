package org.example.Model.DTO.Review;

import java.time.Duration;
import java.util.Date;

public record ReviewDTO (
        Long id,
        int idUser,
        int idGame,
        boolean recommended,
        String reviwText,
        Duration hoursPlayed,
        Date publicationDate,
        Date lastEditionDate,
        ReviewState state){
}
