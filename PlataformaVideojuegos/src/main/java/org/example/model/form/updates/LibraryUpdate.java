package org.example.model.form.updates;

import org.example.model.dto.library.InstalationState;

import java.time.LocalDate;
import java.util.Date;

public record LibraryUpdate(
        Long id,
        Long idUser,
        Long idGame,
        LocalDate acquisitionDate,
        Long timePlaying,
        Date lastPlayed,
        InstalationState instalationState) {
}
