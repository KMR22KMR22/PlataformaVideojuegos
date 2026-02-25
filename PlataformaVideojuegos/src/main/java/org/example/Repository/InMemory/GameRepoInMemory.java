package org.example.Repository.InMemory;

import org.example.Exeptions.GenericExeption;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Form.GameForm;
import org.example.Repository.Interface.IGameRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameRepoInMemory implements IGameRepo {
    private final List<GameEntity> games = new ArrayList<>();
    private static Long idCounter = 1L;


    @Override
    public Optional<GameEntity> crear(GameForm form) {
        var game = new GameEntity(idCounter++, form.getTittle(), form.getDescription(), form.getDeveloper(), form.getLaunchDate(), form.getBasePrice(), form.getCategory(), form.getAgeClasification(), form.getAvailabeLanguages());
        games.add(game);
        return Optional.of(game);
    }

    @Override
    public Optional<GameEntity> obtenerPorId(Long id) {
        return games.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    @Override
    public List<GameEntity> obtenerTodos() {return new ArrayList<>(games);
    }


    @Override
    public GameEntity update(Long id, Optional<GameState> newState, Optional<Integer> percent){
        //Compruebo que el jugo exista y lo guardo, en caso de no existir mando una exepcion
        GameEntity game = obtenerPorId(id).orElseThrow(() -> new GenericExeption(GenericErrors.NOT_EXISTS.getMessage()));

        //Compruebo que el nuevo estado este entre los admisibles
        if(newState.isPresent() && Arrays.stream(GameState.values()).noneMatch(gameState->gameState.equals(newState.get()))){throw new GenericExeption(GenericErrors.NOT_EXISTS.getMessage());}

        //Copruebo que el porciento que se quiere aplicar este en un rango correcto
        if(percent.isPresent() && percent.get() < 0 || percent.get() > 100){throw new GenericExeption(GenericErrors.INVALID_RANGE.getMessage());}

        GameEntity updatedGame = new GameEntity(id, game.getTittle(), game.getDescription(), game.getDeveloper(), game.getLaunchDate(), game.getBasePrice(), game.getCategory(), game.getAgeClasification(), game.getAvailabeLanguages(), percent.orElse(game.getCurrentDescount()), newState.orElse(game.getState()));
        games.removeIf(g -> g.getId().equals(id));
        games.add(updatedGame);

        return updatedGame;
    }

    @Override
    public boolean eliminar(Long aLong) {
        return false;
    }

}
