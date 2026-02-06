package Repository.Interface;

import Model.Entidad.UserEntity;
import Model.Form.UserForm;

import java.util.List;

public interface IUserRepo extends ICrud<UserEntity, UserForm, Long> {

    public List<String> getCountries();

    public void addCountry(String country);
}
