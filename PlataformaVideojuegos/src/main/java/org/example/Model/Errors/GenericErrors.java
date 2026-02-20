package org.example.Model.Errors;

public enum GenericErrors {

    //Errores de creacion
    ALREADY_EXISTS("Ya existe. Introduce otro"),
    NOT_EXISTS("No existe. Introduce otro"),
    REQUIRED_FIELD("Campo obligatorio"),
    TOO_SHORT("Longitud mínima no alcanzada"),
    TOO_LONG("Longitud máxima superada"),
    INVALID_FORMAT("Formato inválido"),
    FUTURE_DATE("La fecha no puede ser futura"),


    //Errores de Busqueda
    NOT_FOUND("No encontrado");


    private final String message;

    GenericErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
