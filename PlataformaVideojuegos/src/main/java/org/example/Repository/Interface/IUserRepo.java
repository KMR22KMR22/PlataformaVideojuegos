package org.example.Repository.Interface;

import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.UserForm;

import java.util.Optional;

public interface IUserRepo extends ICrud<UserEntity, UserForm, Long> {

    UserEntity updatePortafolioBalance(Long id, Optional<Float> money);
}
