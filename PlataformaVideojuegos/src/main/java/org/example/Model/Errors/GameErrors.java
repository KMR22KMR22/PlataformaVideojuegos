package org.example.Model.Errors;

public enum GameErrors {

    //Launch Date
    LAUNCH_DATE_PAST("La fecha de lanzamiento no puede ser pasada"),

    //Priece
    BASE_PRICE_INVALID("El precio base debe estar entre 0 y 999.99"),
    BASE_PRICE_DECIMALS("El precio no puede tener más de dos decimales"),

    //Discount
    DISCOUNT_INVALID("El descuento debe estar entre 0 y 100"),

    //Age Clasification
    AGE_CLASSIFICATION_EMPTY("El juego debe tener una clasificación por edad"),

    //Estado invalido
    INVALID_STATE("Estado invalido");

    private final String message;

    GameErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

