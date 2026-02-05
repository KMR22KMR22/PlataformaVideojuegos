package main.java.Controller;

import java.util.List;

public class Util {

    /**
     * Comprueba si la cadena no es nula o vacia
     *
     * @param cadena cadena a comprobar
     * @return true si no es nula o vacia, false en cualquier otro caso
     */
    public static boolean checkCadena(String cadena){

        return cadena == null || cadena.isBlank();
    }

    public static boolean checkNotUnique(String cadena, List<String> list){

        return list.stream()
                .anyMatch(s -> s.equals(cadena));
    }
}
