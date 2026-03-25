package org.example.controller;

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
}
