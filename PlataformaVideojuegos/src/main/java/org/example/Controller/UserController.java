package org.example.Controller;
import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.DTO.User.UserDTO;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.Updates.UserUpdateForm;
import org.example.Model.Form.UserForm;
import org.example.Repository.InMemory.CountryRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserController {

    private UserRepoInMemory userRepo = new UserRepoInMemory();
    private CountryRepoInMemory countryRepo = new CountryRepoInMemory();


    /**Crea un nuevo usuario
     * @param userForm Formulario con los datos introducidos por el usuario
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * @throws ValidationException
     * */
    public UserDTO registerNewUser(UserForm userForm) throws ValidationException {

        List<ErrorDto> errores = new ArrayList<>();

        //LLamo al validate del formulario y guardo la lista de errores
        errores.addAll(userForm.validate());
        //LLamo al validate del controlador y guardo la lista de errores
        errores.addAll(validate(userForm));

        //Si no se detectaron errores en el formulario se llamo a la funcion de cear nueva UserEntity que esta en el repositorio
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var userOpt = userRepo.crear(userForm);
        var user = userOpt.orElse(null);

        return Mapper.mapFrom(user);
    }



    /**Muestra la informacion de un usuario especifico
     * @param id id del usuario
     * @param name nombre del usuario
     * @return Lista con los datos del usuario, o con el mensaje de usuario no encontrado
     * */
    public UserDTO showUserProfile(Optional<Long> id, Optional<String> name) throws ValidationException {

        if (id.isEmpty() && name.isEmpty()) {
            throw new ValidationException(
                    List.of(new ErrorDto("user", ErrorType.REQUERIDO))
            );
        }

        UserEntity user;

        if (id.isPresent()) {
            user = userRepo.obtenerPorId(id.get())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")
                    );
        } else {
            user = userRepo.obtenerTodos().stream()
                    .filter(u -> u.getUserName().equalsIgnoreCase(name.get()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")
                    );
        }

        return new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRealName(),
                user.getCountry(),
                user.getBirthDate(),
                user.getRegistrationDate(),
                user.getAvatar(),
                user.getPortfolioBalance(),
                user.getAccountState()
        );
    }




    /**Recarga dinero en la cartera virtual de Steam del usuario
     * @param id id del usuario
     * @param money cantidad de dinero a añadir
     * @return UserDTO con el saldo actualizado
     * */
    public UserDTO addBalanceToWallet(Long id, Optional<Float> money) throws IllegalArgumentException{
        UserEntity userOpt = userRepo.obtenerPorId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        //Compruebo que la cuenta de el usuario que se encontro este activa
        if(!userOpt.getAccountState().equals(AccountState.ACTIVE)){
            throw new IllegalArgumentException("Cuenta no activa");
        }

        float ammount = money.orElseThrow(() -> new IllegalArgumentException("Cantidad no encontrada"));

        //Compruebo que la cantidad de saldo que intenta agregar el usuario esta entre 5 y 500
        if(ammount < 5 || ammount > 500){
            throw new IllegalArgumentException("La cantidad de dinero debe estar entre 5 y 500");
        }

        float newbalance = userOpt.getPortfolioBalance() + ammount;

        UserUpdateForm userForm = new UserUpdateForm(userOpt.getUserName(), userOpt.getEmail(), userOpt.getPassword(), userOpt.getRealName(), userOpt.getCountry(), userOpt.getBirthDate(), userOpt.getRegistrationDate(), userOpt.getAvatar(), newbalance, AccountState.ACTIVE);

        UserEntity updatedUser = userRepo.actualizar(id, userForm).get();

        return Mapper.mapFrom(updatedUser);
    }


    /**Comprueba dinero en la cartera virtual de Steam del usuario
     * @param id id del usuaio
     * @return UserDTO
     * */
    public UserDTO showBalanceFromWallet(Long id){

        UserEntity user = userRepo.obtenerPorId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRealName(),
                user.getCountry(),
                user.getBirthDate(),
                user.getRegistrationDate(),
                user.getAvatar(),
                user.getPortfolioBalance(),
                user.getAccountState()
        );
    }




    //Validaciones que dependen del usuario, pero requieren acceso a datos
    /**Realiza las validaciones del UserForm que necesitan acceso a datos
     * @param user Formulario con los datos introducidos por el usuario
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ErrorDto>  validate(UserForm user) {

        List<ErrorDto> errores = new ArrayList<>();

        //Valida que el nombre de usuario no se repita
        if (userRepo.obtenerTodos().stream().anyMatch(e -> e.getUserName().equals(user.userName()))) {
            errores.add(new ErrorDto("Name", ErrorType.DUPLICADO));
        }
        //Valida que el email no se repita
        if (userRepo.obtenerTodos().stream().anyMatch(u -> u.getEmail().equals(user.email()))) {
            errores.add(new ErrorDto("Email", ErrorType.DUPLICADO));
        }
        //Valida que el pais coincida con alguno de la lista del repositorio de paises
        if (countryRepo.getCountries().stream().noneMatch(c -> c.equals(user.country()))) {
            errores.add(new ErrorDto("Country", ErrorType.NO_ENCONTRADO));
        }
        return errores;
    }
}
