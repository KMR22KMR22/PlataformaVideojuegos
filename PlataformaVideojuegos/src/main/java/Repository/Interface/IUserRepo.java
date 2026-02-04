package Repository.Interface;

import Model.Entidad.UserEntity;
import Model.Form.UserForm;

public interface IUserRepo extends ICrud<UserEntity, UserForm, Long> {
}
