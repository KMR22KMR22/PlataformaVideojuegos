package org.example.exeptions;

import org.example.model.form.errors.ErrorDto;

import java.util.List;

public class ValidationException extends Exception {


    private List<ErrorDto> errores;

    public ValidationException(List<ErrorDto> errores) {
        super("Errores de validaci�n");
        this.errores = errores;
    }

    public List<ErrorDto> getErrores() {
        return errores;
    }

}
