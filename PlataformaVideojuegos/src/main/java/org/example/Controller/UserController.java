package org.example.Controller;
import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.DTO.User.UserDTO;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.Updates.UserUpdate;
import org.example.Model.Form.UserForm;

import org.example.Repository.Interface.ICountryRepo;
import org.example.Repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserController {

    private IUserRepo userRepo;
    private ICountryRepo countryRepo;


    //Constructor


    public UserController(IUserRepo userRepo, ICountryRepo countryRepo) {
        this.userRepo = userRepo;
        this.countryRepo = countryRepo;
    }

    /**Crear una nueva cuenta de usuario en la plataforma
     * @param userForm Formulario con los datos introducidos por el usuario
     * @return UserDTO con todos los datos del usuario creado
     * @throws ValidationException Lista de errores de validacion
     * */
    public UserDTO registerNewUser(UserForm userForm) throws ValidationException {

        List<ErrorDto> errors = new ArrayList<>();

        //LLamo al validate del formulario y guardo la lista de errores
        errors.addAll(userForm.validate());
        //LLamo al validate del controlador y guardo la lista de errores
        errors.addAll(validate(userForm));

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        var userOpt = userRepo.create(userForm);
        var user = userOpt.orElse(null);

        return Mapper.mapFrom(user);
    }



    /**Muestra la información de un usuario específico
     * @param id id del usuario (optional)
     * @param name nombre del usuario (optional)
     * @throws ValidationException
     * @return UserDTO con los datos del usuario encontrado
     * */
    public UserDTO showUserProfile(Optional<Long> id, Optional<String> name) throws ValidationException {

        if (id.isEmpty() && name.isEmpty()) {
            throw new ValidationException(
                    List.of(new ErrorDto("IdUser, GameName", ErrorType.REQUERIDO))
            );
        }
        UserEntity user;

        if (id.isPresent()) {
            user = userRepo.getById(id.get())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")
                    );
        } else {
            user = userRepo.getAll().stream()
                    .filter(u -> u.getUserName().equalsIgnoreCase(name.get()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")
                    );
        }
        return Mapper.mapFrom(user);
    }




    /**Recarga dinero en la cartera virtual de Steam del usuario
     * @param id id del usuario
     * @param money cantidad de dinero a añadir
     * @return UserDTO con el saldo actualizado
     * */
    public UserDTO addBalanceToWallet(Long id, Float money) throws IllegalArgumentException{
        UserEntity userOpt = userRepo.getById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        //Compruebo que la cuenta del usuario que se encontró este activa
        if(!userOpt.getAccountState().equals(AccountState.ACTIVE)){
            throw new IllegalArgumentException("Cuenta no activa");
        }

        if(money == null){
            throw new IllegalArgumentException("Cantidad no encontrada");
        }
        float amount = money;

        //Compruebo que la cantidad de saldo que intenta agregar el usuario está entre 5 y 500
        if(amount < 5 || amount > 500){
            throw new IllegalArgumentException("La cantidad de dinero debe estar entre 5 y 500");
        }

        float newBalance = userOpt.getPortfolioBalance() + amount;

        UserUpdate userForm = new UserUpdate(userOpt.getUserName(), userOpt.getEmail(), userOpt.getPassword(), userOpt.getRealName(), userOpt.getCountry(), userOpt.getBirthDate(), userOpt.getRegistrationDate(), userOpt.getAvatar(), newBalance, userOpt.getAccountState());

        UserEntity updatedUser = userRepo.update(id, userForm).get();

        return Mapper.mapFrom(updatedUser);
    }


    /**Comprueba dinero en la cartera virtual de Steam del usuario
     * @param id id del usuaio
     * @return UserDTO con todos sus datos
     * */
    public UserDTO showBalanceFromWallet(Long id){

        UserEntity user = userRepo.getById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return Mapper.mapFrom(user);
    }




    //Validaciones que dependen del usuario, pero requieren acceso a datos
    /**Realiza las validaciones del UserForm que necesitan acceso a datos
     * @param user Formulario con los datos introducidos por el usuario
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ErrorDto>  validate(UserForm user) {

        List<ErrorDto> errores = new ArrayList<>();

        //Valida que el nombre de usuario no se repita
        if (userRepo.getAll().stream().anyMatch(e -> e.getUserName().equals(user.userName()))) {
            errores.add(new ErrorDto("Name", ErrorType.DUPLICADO));
        }
        //Valida que el email no se repita
        if (userRepo.getAll().stream().anyMatch(u -> u.getEmail().equals(user.email()))) {
            errores.add(new ErrorDto("Email", ErrorType.DUPLICADO));
        }
        //Valida que el pais coincida con alguno de la lista del repositorio de paises
        if (countryRepo.getAll().stream().noneMatch(c -> c.name().equals(user.country()))) {
            errores.add(new ErrorDto("Country", ErrorType.NO_ENCONTRADO));
        }
        return errores;
    }
}
