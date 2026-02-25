package org.example.Repository.Interface;

import org.example.Model.DTO.Game.GameState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Form.GameForm;

import java.util.Optional;

public interface IGameRepo extends ICrud<GameEntity, GameForm, Long> {

    GameEntity update(Long id, Optional<GameState> newState, Optional<Integer> percent);
}
