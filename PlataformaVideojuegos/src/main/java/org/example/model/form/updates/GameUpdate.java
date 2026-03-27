package org.example.model.form.updates;

import org.example.model.dto.game.GameAgeClasification;
import org.example.model.dto.game.GameState;

import java.time.LocalDate;
import java.util.List;

public record GameUpdate(
        Long id,
        String tittle,
        String description,
        String developer,
        LocalDate launchDate,
        float basePrice,
        int currentDescount,
        String category,
        GameAgeClasification gameAgeClasification,
        List<String> availabeLanguages,
        GameState State) {
}
