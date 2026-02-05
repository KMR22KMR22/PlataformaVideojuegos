package main.java.Repository.Interface;

import main.java.Model.Entidad.UserEntity;
import main.java.Model.Form.UserForm;

public interface IUserRepo extends ICrud<UserEntity, UserForm, Long> {
}
