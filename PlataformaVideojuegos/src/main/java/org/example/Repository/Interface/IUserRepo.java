package org.example.Repository.Interface;

import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.UserForm;

import java.util.Optional;

public interface IUserRepo extends ICrud<UserEntity, UserForm, Long> {

    UserEntity update(Long id, Optional<Float> money);
}
