package org.example.repository.Interface;

import org.example.model.entidad.UserEntity;
import org.example.model.form.updates.UserUpdate;
import org.example.model.form.UserForm;

public interface IUserRepo extends ICrud<UserEntity, UserForm, UserUpdate, Long> {

}
