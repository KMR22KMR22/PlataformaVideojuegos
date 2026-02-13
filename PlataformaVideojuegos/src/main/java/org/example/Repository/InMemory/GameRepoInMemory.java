package org.example.Repository.InMemory;

import org.example.Model.Entidad.GameEntity;
import org.example.Model.Form.GameForm;
import org.example.Repository.Interface.IGameRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameRepoInMemory implements IGameRepo {
    private final List<GameEntity> games = new ArrayList<>();
    private static Long idCounter = 1L;


    @Override
    public Optional<GameEntity> crear(GameForm form) {
        var game = new GameEntity(idCounter++, form.getTittle(), form.getDescription(), form.getDeveloper(), form.getLaunchDate(), form.getBasePrice(), form.getCurrentDescount(), form.getCategory(), form.getAgeClasification(), form.getAvailabeLanguages(), form.getState());
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
    public Optional<GameEntity> actualizar(Long aLong, GameForm dto) {
        return Optional.empty();
    }

    @Override
    public boolean eliminar(Long aLong) {
        return false;
    }

}
