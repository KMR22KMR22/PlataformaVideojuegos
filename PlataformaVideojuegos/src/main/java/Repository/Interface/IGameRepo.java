package main.java.Repository.Interface;

import main.java.Model.Entidad.GameEntity;
import main.java.Model.Form.GameForm;

public interface IGameRepo extends ICrud<GameEntity, GameForm, Long> {
}
