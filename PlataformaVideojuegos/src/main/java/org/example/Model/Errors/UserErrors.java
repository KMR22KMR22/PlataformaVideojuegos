package org.example.Model.Errors;

public enum UserErrors {

    // Username
    USERNAME_INVALID_FORMAT("El nombre de usuario solo puede contener caracteres alfanuméricos, guiones y guiones bajos"),
    USERNAME_STARTS_WITH_NUMBER("El nombre de usuario no puede comenzar por un número"),

    // Password
    PASSWORD_WEAK("La contraseña debe contener al menos una mayúscula, una minúscula y un número"),

    // Birth date
    BIRTHDATE_UNDERAGE("El usuario debe tener al menos 13 años"),
    BIRTHDATE_FUTURE("La fecha de nacimiento no puede ser futura"),

    //Money
    MONEY_BELOW_CERO("El saldo debe ser cero o mayor"),

    // Avatar
    AVATAR_TOO_LONG("El avatar no puede superar los 100 caracteres"),

    //Account State
    ACCOUNT_NOT_ACTIVE("La cuenta no esta activa"),
    IMMPORT_NOT_VALID("El importe debe ser entre 5 y 500");


    private final String message;

    UserErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

