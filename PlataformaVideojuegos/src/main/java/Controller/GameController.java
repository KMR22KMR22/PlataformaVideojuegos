package Controller;

import Model.Form.GameForm;
import Repository.InMemory.GameRepoInMemory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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


