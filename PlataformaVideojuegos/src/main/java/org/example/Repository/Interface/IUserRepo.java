package org.example.Repository.Interface;

import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.Updates.UserUpdateForm;
import org.example.Model.Form.UserForm;

import java.util.Optional;

public interface IUserRepo extends ICrud<UserEntity, UserForm, UserUpdateForm, Long> {

}
