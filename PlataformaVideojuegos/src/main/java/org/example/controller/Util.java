package org.example.controller;

import org.example.exeptions.ValidationException;
import org.example.model.form.errors.ErrorDto;

import java.util.List;

public class Util {

    /**
     * Comprueba si la cadena no es nula o vacia
     *
     * @param cadena cadena a comprobar
     * @return true si no es nula o vacia, false en cualquier otro caso
     */
    public static boolean checkCadenaBlankOrEmpty(String cadena) {

        return cadena == null || cadena.isBlank();
    }

    public static void thowException(List<ErrorDto> errors) throws ValidationException {
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
