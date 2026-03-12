package org.example.Repository.Interface;

import org.example.Model.Entidad.GameEntity;
import org.example.Model.Form.GameForm;
import org.example.Model.Form.Updates.GameUpdate;

public interface IGameRepo extends ICrud<GameEntity, GameForm, GameUpdate, Long> {
}
