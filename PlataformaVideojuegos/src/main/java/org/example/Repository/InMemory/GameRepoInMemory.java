package org.example.Repository.InMemory;

import org.example.Model.Entidad.GameEntity;
import org.example.Model.Form.GameForm;
import org.example.Model.Form.Updates.GameUpdateForm;
import org.example.Repository.Interface.IGameRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameRepoInMemory implements IGameRepo {
    private static final List<GameEntity> GAMES = new ArrayList<>();
    private static Long NEXT_ID = 1L;


    @Override
    public Optional<GameEntity> crear(GameForm form) {
        var game = new GameEntity(NEXT_ID++, form.tittle(), form.description(), form.developer(), form.launchDate(), form.basePrice(), form.category(), form.gameAgeClasification(), form.availabeLanguages());
        GAMES.add(game);
        return Optional.of(game);
    }

    @Override
    public Optional<GameEntity> obtenerPorId(Long id) {
        return GAMES.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    @Override
    public List<GameEntity> obtenerTodos() {return new ArrayList<>(GAMES);
    }

    @Override
    public Optional<GameEntity> actualizar(Long id, GameUpdateForm form) {
        obtenerPorId(id).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

        var gameUpdated = new GameEntity(form.id(), form.tittle(), form.description(), form.developer(), form.launchDate(), form.basePrice(), form.currentDescount(), form.category(), form.gameAgeClasification(), form.availabeLanguages(), form.State());
        GAMES.removeIf(p -> p.getId().equals(id));
        GAMES.add(gameUpdated);
        return Optional.of(gameUpdated);
    }

    @Override
    public boolean eliminar(Long aLong) {
        return false;
    }

}
