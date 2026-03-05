package org.example.Model.DTO.Game;

import org.example.Model.DTO.Review.ReviewDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record GameStatsDTO (Long id,
        String title,
        String description,
        String developer,
        LocalDate launchDate,
        float basePrice,
        int currentDescount,
        String category,
        GameAgeClasification gameAgeClasificatio,
        List<String>availabeLanguages,
        GameState State,
        List<Integer> IdReview
                            ){
}
