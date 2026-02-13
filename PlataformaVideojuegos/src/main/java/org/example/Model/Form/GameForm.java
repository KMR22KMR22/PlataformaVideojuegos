package org.example.Model.Form;

import org.example.Controller.Util;
import org.example.Model.DTO.Game.AgeClasification;
import org.example.Model.DTO.Game.GameState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameForm {

    private Long id;
    private String tittle;
    private String description;
    private String developer;
    private LocalDate launchDate;
    private float basePrice;
    private int currentDescount;
    private String category;
    private AgeClasification ageClasification;
    private List<String> availabeLanguages;
    private GameState State;

    private List<String> errores = new ArrayList<>();


    //Getters


    public Long getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }

    public String getDeveloper() {
        return developer;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public int getCurrentDescount() {
        return currentDescount;
    }

    public String getCategory() {
        return category;
    }

    public AgeClasification getAgeClasification() {
        return ageClasification;
    }

    public List<String> getAvailabeLanguages() {
        return availabeLanguages;
    }

    public GameState getState() {
        return State;
    }


    //Constructor


    public GameForm(Long id, String tittle, String description, String developer, LocalDate launchDate, float basePrice, String category, AgeClasification ageClasification, List<String> availabeLanguages) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.developer = developer;
        this.launchDate = launchDate;
        this.basePrice = basePrice;
        this.currentDescount = 0;
        this.category = category;
        this.ageClasification = ageClasification;
        this.availabeLanguages = availabeLanguages;
        State = GameState.DISPONIBLE;
    }


    //Validaciones
    public List<String>  validateGame(GameForm game) {

        errores.clear();


        //Titulo Game
        validateGameTittle(game);

        //Descripcion Game (opcional)
        validateDescription(game);

        //Desarrollador
        validateDeveloper(game);

        //Fecha de lanzamiento
        validateLunchDate(game);

        //Pais
        validateBasePrice(game);

        //Descuento actual
        validateCurrentDescount(game);

        //Clasificacion de edad
        validateAgeClasification(game);

        //Idiomas
        validateLanguages(game);

        //Estado
        validateState(game);



        return errores;
    }


    /**
     * Valida que el titulo del juego se haya introducido correctamente
     * @param game
     * */
    private void validateGameTittle(GameForm game) {
        if(Util.checkCadenaBlankOrEmpty(game.getTittle())){
            errores.add("El juego debe tener un titulo");
        }
        if(game.getTittle().length() > 100){
            errores.add("El nombre del juego no puede tener mas de 100 caracteres");
        }

    }

    /**
     * Valida que la descripcion del juego se haya introducido correctamente
     * @param game
     * */
    private void validateDescription(GameForm game) {
        if (!Util.checkCadenaBlankOrEmpty(game.getDescription())){
            if(game.getDescription().length() > 2000 ){
                errores.add("La longitud de la descripcion no puede ser mayor a 2000 caracteres");
            }
        }
    }

    /**
     * Valida que el desarrollador del juego se haya introducido correctamente
     * @param game
     * */
    private void validateDeveloper(GameForm game) {
        if(Util.checkCadenaBlankOrEmpty(game.getDeveloper())){
            errores.add("El juego debe tener un desarrollador");
        }
        if(game.getDeveloper().length() < 2 || game.getDeveloper().length() > 100){
            errores.add("El desarrollador debe tener entre 2 y 100 caracteres");
        }
    }

    /**
     * Valida que la fecha de lanzamiento del juego se haya introducido correctamente
     * @param game
     * */
    private void validateLunchDate(GameForm game) {
        if(game.getLaunchDate() == null){
            errores.add("Debe ingresar la fecha de lanzamiento");
        }
        if(game.getLaunchDate().isBefore(LocalDate.now())){
            errores.add("No puede ingresar una fecha pasada");
        }

    }

    /**
     * Valida que el precio base del juego se haya introducido correctamente
     * @param game
     * */
    private void validateBasePrice(GameForm game) {
        if(game.getBasePrice() < 0 ||  game.getBasePrice() > 999.99){
            errores.add("El precio base del juego debe estar entre 0 y 999.99");
        }
        var value = BigDecimal.valueOf((game.getBasePrice()));
        if(value.scale() > 2){
            errores.add("El precio no puede tener mas de dos decimales");
        }
    }

    /**
     * Valida que el descuento actual del juego se haya introducido correctamente
     * @param game
     * */
    private void validateCurrentDescount(GameForm game) {
        if(game.getCurrentDescount() > 0){
            if(game.getBasePrice() < 0 ||  game.getBasePrice() > 100){
                errores.add("El descuento debe estar entre 0 y 100");
            }
            //Falta comprobar que el descuneto sea entero.

        }
    }

    /**
     * Valida que la clasificacion del juego se haya introducido correctamente
     * @param game
     * */
    private void validateAgeClasification(GameForm game) {
        if(Util.checkCadenaBlankOrEmpty(game.getAgeClasification().name())){
            errores.add("El juego debe llevar una clasificacion por edad");
        }
        //Falta comprobar que sea uno de los valores del enum
    }

    /**
     * Valida que los idiomas del juego se haya introducido correctamente
     * @param game
     * */
    private void validateLanguages(GameForm game) {
        //Si el array no es null en la posicion cero es que el usuario le puso idiomas al juego, y si no es que esta vacio
        if(game.getAvailabeLanguages().getFirst() != null){
            if(game.getAvailabeLanguages().stream().filter(l -> l)) {}
        }
    }
}
