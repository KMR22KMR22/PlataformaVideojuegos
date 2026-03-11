package org.example.Model.Form.Updates;

import org.example.Model.DTO.Library.InstalationState;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

public record LibraryUpdateForm (
        Long id,
        Long idUser,
        Long idGame,
        LocalDate adquisitionDate,
        Long timePlaying,
        Date lastPlayed,
        InstalationState instalationState){
}
