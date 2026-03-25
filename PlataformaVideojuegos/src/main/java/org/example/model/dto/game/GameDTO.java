package org.example.model.dto.game;

import java.time.LocalDate;
import java.util.List;

public record GameDTO(
        Long id,
        String title,
        String description,
        String developer,
        LocalDate launchDate,
        float basePrice,
        float currentDescount,
        String category,
        GameAgeClasification gameAgeClasification,
        List<String> availabeLanguages,
        GameState State) {
}
