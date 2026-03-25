package org.example.repository.Interface;

import org.example.model.entidad.GameEntity;
import org.example.model.form.GameForm;
import org.example.model.form.updates.GameUpdate;

public interface IGameRepo extends ICrud<GameEntity, GameForm, GameUpdate, Long> {
}
