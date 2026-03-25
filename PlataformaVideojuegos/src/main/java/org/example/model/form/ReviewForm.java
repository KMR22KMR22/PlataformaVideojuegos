package org.example.model.form;

import org.example.controller.Util;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;

import java.util.ArrayList;
import java.util.List;

public record ReviewForm(Long idUser,
                         Long idGame,
                         boolean recommended,
                         String reviwText,
                         Long hoursPlayed) {


    public static final int MIN_VALUE = 50;
    public static final int MAX_VALUE = 8000;

    public List<ErrorDto> validate() {
        List<ErrorDto> errors = new ArrayList<>();

        if (idUser() == null) {
            errors.add(new ErrorDto("IdUser", ErrorType.REQUERIDO));
        }
        if (idGame() == null) {
            errors.add(new ErrorDto("IdGame", ErrorType.REQUERIDO));
        }
        if (Util.checkCadenaBlankOrEmpty(reviwText)) {
            errors.add(new ErrorDto("ReviewText", ErrorType.REQUERIDO));
        }
        if (reviwText.length() < MIN_VALUE) {
            errors.add(new ErrorDto("ReviewText", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (reviwText.length() > MAX_VALUE) {
            errors.add(new ErrorDto("ReviewText", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        if (hoursPlayed == null) {
            errors.add(new ErrorDto("HoursPlayed", ErrorType.REQUERIDO));
        }
        if (hoursPlayed < 0) {
            errors.add(new ErrorDto("HoursPlayed", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        return errors;
    }

}
