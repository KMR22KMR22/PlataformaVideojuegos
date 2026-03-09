package org.example.Repository.Interface;

import org.example.Model.DTO.Game.GameState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Form.GameForm;
import org.example.Model.Form.Updates.GameUpdateForm;

import java.util.Optional;

public interface IGameRepo extends ICrud<GameEntity, GameForm, GameUpdateForm, Long> {
}
