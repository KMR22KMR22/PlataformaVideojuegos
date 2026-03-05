package org.example.Model.Form;

import org.example.Controller.Util;
import org.example.Model.DTO.Game.GameAgeClasification;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;

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
    private GameAgeClasification gameAgeClasification;
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

    public GameAgeClasification getAgeClasification() {
        return gameAgeClasification;
    }

    public List<String> getAvailabeLanguages() {
        return availabeLanguages;
    }

    public GameState getState() {
        return State;
    }


    //Constructor


    public GameForm(Long id, String tittle, String description, String developer, LocalDate launchDate, float basePrice, String category, GameAgeClasification gameAgeClasification, List<String> availabeLanguages) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.developer = developer;
        this.launchDate = launchDate;
        this.basePrice = basePrice;
        this.currentDescount = 0;
        this.category = category;
        this.gameAgeClasification = gameAgeClasification;
        this.availabeLanguages = availabeLanguages;
        State = GameState.DISPONIBLE;
    }


    //Validaciones
    /**
     * Valida que los datos del juego se hayan introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
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
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<ErrorDto> validateGameTittle() {
        List<ErrorDto> errores = new ArrayList<>();

        if(Util.checkCadenaBlankOrEmpty(tittle)){
            errores.add(new ErrorDto("Tittle", ErrorType.REQUERIDO));
        }
        if(tittle.length() > 100){
            errores.add(new ErrorDto("Tittle", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        return errores;
    }

    /**
     * Valida que la descripcion del juego se haya introducido correctamente
     *  @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<ErrorDto> validateDescription() {
        List<ErrorDto> errores = new ArrayList<>();

        if (!Util.checkCadenaBlankOrEmpty(description)){
            if(description.length() > 2000 ){
                errores.add(new ErrorDto("Description", ErrorType.VALOR_DEMASIADO_ALTO));
            }
        }
        return errores;
    }

    /**
     * Valida que el desarrollador del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<ErrorDto> validateDeveloper() {
        List<ErrorDto> errores = new ArrayList<>();

        if(Util.checkCadenaBlankOrEmpty(developer)){
            errores.add(new ErrorDto("Developer", ErrorType.REQUERIDO));
        }
        if(developer.length() < 2){
            errores.add(new ErrorDto("Developer", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(developer.length() > 100){
            errores.add(new ErrorDto("Developer", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        return errores;
    }

    /**
     * Valida que la fecha de lanzamiento del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<ErrorDto> validateLunchDate() {
        List<ErrorDto> errores = new ArrayList<>();

        if(launchDate == null){
            errores.add(new ErrorDto("LaunchDate", ErrorType.REQUERIDO));
        }
        if(launchDate.isBefore(LocalDate.now())){
            errores.add(new ErrorDto("LaunchDate", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        return errores;
    }

    /**
     * Valida que el precio base del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<ErrorDto> validateBasePrice() {
        List<ErrorDto> errores = new ArrayList<>();

        if(basePrice < 0){
            errores.add(new ErrorDto("BasePrice", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(basePrice > 999.99){
            errores.add(new ErrorDto("BasePrice", ErrorType.VALOR_DEMASIADO_ALTO));

        }
        var value = BigDecimal.valueOf((basePrice));
        if(value.scale() > 2){
            errores.add(new ErrorDto("BasePrice", ErrorType.FORMATO_INVALIDO));
        }
        return errores;
    }

    /**
     * Valida que la clasificacion del juego se haya introducido correctamente
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    private List<ErrorDto> validateAgeClasification() {
        List<ErrorDto> errores = new ArrayList<>();

        if(Util.checkCadenaBlankOrEmpty(gameAgeClasification.name())){
            errores.add(new ErrorDto("AgeClasification", ErrorType.REQUERIDO));
        }
        return errores;
    }

    /**
     * Valida que los idiomas del juego se haya introducido correctamente
     * @return Lista de errores
     * */
    private List<ErrorDto> validateLanguages() {
        List<ErrorDto> errores = new ArrayList<>();
        List<String> languages = new ArrayList<>();

        //Si el array no es null en la posicion cero es que el usuario le puso idiomas al juego, y si no es que esta vacio
        if(availabeLanguages.getFirst() != null){
            languages = availabeLanguages.stream()
                    .filter(l -> l.length() > 200)
                    .toList();
            if(!languages.isEmpty()){
                errores.add(new ErrorDto("Languages", ErrorType.FORMATO_INVALIDO));
            }
        }
        return errores;
    }
}
