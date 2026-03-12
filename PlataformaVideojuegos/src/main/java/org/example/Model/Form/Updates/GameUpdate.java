package org.example.Model.Form.Updates;

import org.example.Model.DTO.Game.GameAgeClasification;
import org.example.Model.DTO.Game.GameState;

import java.time.LocalDate;
import java.util.List;

public record GameUpdate(
        Long id,
        String tittle,
        String description,
        String developer,
        LocalDate launchDate,
        float basePrice,
        float currentDescount,
        String category,
        GameAgeClasification gameAgeClasification,
        List<String>availabeLanguages,
        GameState State){
}
