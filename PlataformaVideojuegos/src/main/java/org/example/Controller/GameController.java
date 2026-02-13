package org.example.Controller;

import org.example.Model.Form.GameForm;
import org.example.Repository.InMemory.GameRepoInMemory;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameRepoInMemory repository = new GameRepoInMemory();
    private List<String> errores = new ArrayList<>();

    public List<String>  validateGameCreation(GameForm game) {

        errores.clear();

        if (repository.obtenerTodos().stream().anyMatch(g -> g.equals(game.getTittle()))) {
            errores.add("Ese titulo de juego ya existe");
        }


        return errores;
    }


    //Validaciones que no dependen del usuario (Reglas de inicializacion)

    //Descuento por defecto 0
    //Estado por defecto DISPONIBLE








}


