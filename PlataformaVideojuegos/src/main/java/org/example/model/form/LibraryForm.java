package org.example.model.form;

import java.time.LocalDate;

public record LibraryForm(Long idUser,
                          Long idGame,
                          LocalDate acquisitionDate) {

}
