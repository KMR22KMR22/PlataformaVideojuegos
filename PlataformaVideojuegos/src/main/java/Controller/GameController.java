package Controller;

import Model.Form.GameForm;
import Repository.InMemory.GameRepoInMemory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameRepoInMemory repository = new GameRepoInMemory();
    private List<String> errores = new ArrayList<>();

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

        //Fecha de registro
        validateRegistrationDate(user);

        //Avatar
        validateAvatar(user);



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
        if (repository.obtenerTodos().stream().anyMatch(g -> g.equals(game.getTittle()))) {
            errores.add("Ese titulo de juego ya existe");
        }
    }

    /**
     * Valida que la descripcion del juego se haya introducido correctamente
     * @param game
     * */
    private void validateDescription(GameForm game) {
        if (!Util.checkCadenaBlankOrEmpty(game.getDescription()) & game.getDescription().length() > 2000 ) {
            errores.add("La longitud de la descripcion no puede ser mayor a 2000 caracteres");
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
        //Falta fecha obligatoria

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
     * Valida que descuento actual del juego se haya introducido correctamente
     * @param game
     * */
    private void validateCurrentDescount(GameForm game) {

    }
}


