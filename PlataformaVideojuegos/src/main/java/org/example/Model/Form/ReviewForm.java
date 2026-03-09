package org.example.Model.Form;

import org.example.Model.DTO.Review.ReviewState;

import java.time.Duration;
import java.util.Date;

public record ReviewForm (Long id,
                          int idUser,
                          int idGame,
                          boolean recommended,
                          String reviwText,
                          Duration hoursPlayed,
                          Date publicationDate,
                          Date lastEditionDate,
                          ReviewState state){


}
