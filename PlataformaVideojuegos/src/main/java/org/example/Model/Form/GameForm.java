package org.example.Model.Form;

import org.example.Controller.Util;
import org.example.Model.DTO.Game.AgeClasification;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.Errors.GameErrors;
import org.example.Model.Errors.GenericErrors;

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
    /**
     * Valida que los datos del juego se hayan introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String>  validateGame() {

        List<String> errores = new ArrayList<>();


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
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<String> validateGameTittle() {
        List<String> errores = new ArrayList<>();

        if(Util.checkCadenaBlankOrEmpty(tittle)){
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if(tittle.length() > 100){
            errores.add(GenericErrors.TOO_LONG.getMessage());
        }
        return errores;
    }

    /**
     * Valida que la descripcion del juego se haya introducido correctamente
     *  @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<String> validateDescription() {
        List<String> errores = new ArrayList<>();

        if (!Util.checkCadenaBlankOrEmpty(description)){
            if(description.length() > 2000 ){
                errores.add(GenericErrors.TOO_LONG.getMessage());
            }
        }
        return errores;
    }

    /**
     * Valida que el desarrollador del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<String> validateDeveloper() {
        List<String> errores = new ArrayList<>();

        if(Util.checkCadenaBlankOrEmpty(developer)){
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if(developer.length() < 2){
            errores.add(GenericErrors.TOO_SHORT.getMessage());
        }
        if(developer.length() > 100){
            errores.add(GenericErrors.TOO_LONG.getMessage());
        }
        return errores;
    }

    /**
     * Valida que la fecha de lanzamiento del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<String> validateLunchDate() {
        List<String> errores = new ArrayList<>();

        if(launchDate == null){
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        if(launchDate.isBefore(LocalDate.now())){
            errores.add(GameErrors.LAUNCH_DATE_PAST.getMessage());
        }
        return errores;
    }

    /**
     * Valida que el precio base del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<String> validateBasePrice() {
        List<String> errores = new ArrayList<>();

        if(basePrice < 0 ||  basePrice > 999.99){
            errores.add(GameErrors.BASE_PRICE_INVALID.getMessage());
        }
        var value = BigDecimal.valueOf((basePrice));
        if(value.scale() > 2){
            errores.add(GameErrors.BASE_PRICE_DECIMALS.getMessage());
        }
        return errores;
    }

    /**
     * Valida que la clasificacion del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<String> validateAgeClasification() {
        List<String> errores = new ArrayList<>();

        if(Util.checkCadenaBlankOrEmpty(ageClasification.name())){
            errores.add(GenericErrors.REQUIRED_FIELD.getMessage());
        }
        return errores;
    }

    /**
     * Valida que los idiomas del juego se haya introducido correctamente
     * @return Lista de errores
     * */
    private List<String> validateLanguages() {
        List<String> errores = new ArrayList<>();

        //Si el array no es null en la posicion cero es que el usuario le puso idiomas al juego, y si no es que esta vacio
        if(availabeLanguages.getFirst() != null){
            if(availabeLanguages.) {}
        }
        return errores;
    }
}
