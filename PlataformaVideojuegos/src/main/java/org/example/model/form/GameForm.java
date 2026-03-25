package org.example.model.form;

import org.example.controller.Util;
import org.example.model.dto.game.GameAgeClasification;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GameForm(
        Long id,
        String tittle,
        String description,
        String developer,
        LocalDate launchDate,
        float basePrice,
        String category,
        GameAgeClasification gameAgeClasification,
        List<String> availabeLanguages) {

    public static final int MAX_TITLE = 100;
    public static final int MAX_DESCRIPTION = 2000;
    public static final int MIN_DEV = 2;
    public static final int MAX_DEV = 100;
    public static final int MIN_BASE_PRICE = 0;
    public static final double MAX_BASE_PRICE = 999.99;
    public static final int DECIMALS = 2;
    public static final int MAX_LANG = 200;


    //Validaciones

    /**
     * Valida que los datos del juego se hayan introducido correctamente
     *
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     *
     */
    public List<ErrorDto> validate() {

        List<ErrorDto> errores = new ArrayList<>();


        //Titulo Game
        errores.addAll(validateGameTittle());

        //Descripcion Game (opcional)
        errores.addAll(validateDescription());

        //Desarrollador
        errores.addAll(validateDeveloper());

        //Fecha de lanzamiento
        errores.addAll(validateLunchDate());

        //Pais
        errores.addAll(validateBasePrice());

        //Clasificacion de edad
        errores.addAll(validateAgeClasification());

        //Idiomas
        errores.addAll(validateLanguages());


        return errores;
    }


    /**
     * Valida que el titulo del juego se haya introducido correctamente
     *
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     *
     */
    private List<ErrorDto> validateGameTittle() {
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(tittle)) {
            errores.add(new ErrorDto("Tittle", ErrorType.REQUERIDO));
        }
        if (tittle.length() > MAX_TITLE) {
            errores.add(new ErrorDto("Tittle", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        return errores;
    }

    /**
     * Valida que la descripcion del juego se haya introducido correctamente
     *
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     *
     */
    private List<ErrorDto> validateDescription() {
        List<ErrorDto> errores = new ArrayList<>();

        if (!Util.checkCadenaBlankOrEmpty(description)) {
            if (description.length() > MAX_DESCRIPTION) {
                errores.add(new ErrorDto("Description", ErrorType.VALOR_DEMASIADO_ALTO));
            }
        }
        return errores;
    }

    /**
     * Valida que el desarrollador del juego se haya introducido correctamente
     *
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     *
     */
    private List<ErrorDto> validateDeveloper() {
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(developer)) {
            errores.add(new ErrorDto("Developer", ErrorType.REQUERIDO));
        }
        if (developer.length() < MIN_DEV) {
            errores.add(new ErrorDto("Developer", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (developer.length() > MAX_DEV) {
            errores.add(new ErrorDto("Developer", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        return errores;
    }

    /**
     * Valida que la fecha de lanzamiento del juego se haya introducido correctamente
     *
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     *
     */
    private List<ErrorDto> validateLunchDate() {
        List<ErrorDto> errores = new ArrayList<>();

        if (launchDate == null) {
            errores.add(new ErrorDto("LaunchDate", ErrorType.REQUERIDO));
        }
        if (launchDate.isBefore(LocalDate.now())) {
            errores.add(new ErrorDto("LaunchDate", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        return errores;
    }

    /**
     * Valida que el precio base del juego se haya introducido correctamente
     *
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     *
     */
    private List<ErrorDto> validateBasePrice() {
        List<ErrorDto> errores = new ArrayList<>();

        if (basePrice < MIN_BASE_PRICE) {
            errores.add(new ErrorDto("BasePrice", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (basePrice > MAX_BASE_PRICE) {
            errores.add(new ErrorDto("BasePrice", ErrorType.VALOR_DEMASIADO_ALTO));

        }
        var value = BigDecimal.valueOf((basePrice));
        if (value.scale() > DECIMALS) {
            errores.add(new ErrorDto("BasePrice", ErrorType.FORMATO_INVALIDO));
        }
        return errores;
    }

    /**
     * Valida que la clasificacion del juego se haya introducido correctamente
     *
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     *
     */
    private List<ErrorDto> validateAgeClasification() {
        List<ErrorDto> errores = new ArrayList<>();

        if (Util.checkCadenaBlankOrEmpty(gameAgeClasification.name())) {
            errores.add(new ErrorDto("AgeClasification", ErrorType.REQUERIDO));
        }
        return errores;
    }

    /**
     * Valida que los idiomas del juego se haya introducido correctamente
     *
     * @return Lista de errores
     *
     */
    private List<ErrorDto> validateLanguages() {
        List<ErrorDto> errores = new ArrayList<>();
        List<String> languages = new ArrayList<>();

        //Si el array no es null en la posicion cero es que el usuario le puso idiomas al juego, y si no es que esta vacio
        if (availabeLanguages.getFirst() != null) {
            languages = availabeLanguages.stream()
                    .filter(l -> l.length() > MAX_LANG)
                    .toList();
            if (!languages.isEmpty()) {
                errores.add(new ErrorDto("Languages", ErrorType.FORMATO_INVALIDO));
            }
        }
        return errores;
    }
}
