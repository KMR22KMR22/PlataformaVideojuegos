package org.example.Controller;

import org.example.Model.DTO.Game.AgeClasification;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Errors.GameErrors;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Form.GameForm;
import org.example.Repository.InMemory.GameRepoInMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameController {

    private GameRepoInMemory repository = new GameRepoInMemory();

    //Validaciones que dependen del usuario, pero requieren acceso a datos
    public List<String>  validateGameCreation(GameForm game) {

        List<String> errores = new ArrayList<>();

        //Comprueba que el titulo no se repita
        if (repository.obtenerTodos().stream().anyMatch(g -> g.equals(game.getTittle()))) {
            errores.add(GenericErrors.ALREADY_EXISTS.getMessage());
        }
        //Comprueba que la clasificacion de edad del juego este entre las disponibles
        if(Arrays.stream(AgeClasification.values()).noneMatch(c -> c.equals(game.getAgeClasification()))) {
            errores.add(GenericErrors.NOT_EXISTS.getMessage());
        }


        return errores;
    }


    //Validaciones para la actualizacion del juego
    public List<String>  validateGameUpdate(GameEntity game) {
        List<String> errores = new ArrayList<>();

        //Comprueba que al actualizar el juego el estado del mismo este entre los valores admisibles
        if(Arrays.stream(GameState.values()).noneMatch(c -> c.equals(game.getState()))) {
            errores.add(GenericErrors.NOT_EXISTS.getMessage());
        }
        //Comprueba que al actualizar el juego el descuento este en un rango entre o y 100
        if(game.getCurrentDescount() < 0 ||  game.getCurrentDescount() > 100) {
            errores.add(GameErrors.DISCOUNT_INVALID.getMessage());
        }

        return errores;
    }








}


