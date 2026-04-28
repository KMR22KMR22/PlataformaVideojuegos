package org.example.controller;

import org.example.exeptions.ValidationException;
import org.example.mapper.Mapper;
import org.example.model.dto.user.AccountState;
import org.example.model.dto.user.UserDTO;
import org.example.model.entidad.UserEntity;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;
import org.example.model.form.updates.UserUpdate;
import org.example.model.form.UserForm;

import org.example.repository.Interface.ICountryRepo;
import org.example.repository.Interface.IUserRepo;
import org.example.transaction.ITransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserController {

    public static final float MIN_VALUE = 5;
    public static final float MAX_VALUE = 500;
    private IUserRepo userRepo;
    private ICountryRepo countryRepo;
    public ITransactionManager tm;


    //Constructor


    public UserController(IUserRepo userRepo, ICountryRepo countryRepo, ITransactionManager tm) {
        this.userRepo = userRepo;
        this.countryRepo = countryRepo;
        this.tm = tm;
    }


    /**
     * Crear una nueva cuenta de usuario en la plataforma
     *
     * @param userForm Formulario con los datos introducidos por el usuario
     * @return UserDTO con todos los datos del usuario creado
     * @throws ValidationException Lista de errores de validacion
     *
     */
    public UserDTO registerNewUser(UserForm userForm) throws ValidationException {

        List<ErrorDto> errors = new ArrayList<>();

        //LLamo al validate del formulario y guardo la lista de errores
        errors.addAll(userForm.validate());

        //Inicio transaccion
        var createdGame = tm.inTransaction(()->{

            //LLamo al validate del controlador y guardo la lista de errores
            errors.addAll(validate(userForm));

            //Si hay errores en el usuario mando una ilegalArgumentExeption para que la funcion inTransaction la capture en el catch y haga un rollback de la transaccion
            if(!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            return userRepo.create(userForm);
        }).orElse(null);

        //Vuelvo a comprobar si hay errores mando una validation exeption
        Util.thowException(errors);

        return Mapper.mapFrom(createdGame);
    }



    /**
     * Muestra la información de un usuario específico
     * Si se le pasa el id y el nombre la funcion busca al usuario por id
     * @param id   id del usuario (optional)
     * @param name nombre del usuario (optional)
     * @return UserDTO con los datos del usuario encontrado
     * @throws ValidationException
     *
     */
    public UserDTO showUserProfile(Optional<Long> id, Optional<String> name) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que me hayan pasado al menos id o nombre
        if (id.isEmpty() && name.isEmpty()) {
            throw new ValidationException(
                    List.of(new ErrorDto("IdUser, UserName", ErrorType.REQUERIDO))
            );
        }

        //Inicio transaccion
        UserEntity user = tm.inTransaction(()->{

            if (id.isPresent()) {
                return userRepo.getById(id.get()).orElse(null);


            } else {
                return userRepo.getAll().stream()
                        .filter(u -> u.getUserName().trim().equalsIgnoreCase(name.get().trim()))
                        .findFirst()
                        .orElse(null);
            }
        });

        //Si no encuentra al usuario agrego el error
        if (user == null) {errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));}

        //Compruebo si hay errores en la lista de errores para lanzar exepcion
        Util.thowException(errors);

        return Mapper.mapFrom(user);
    }


    /**
     * Recarga dinero en la cartera virtual de Steam del usuario
     *
     * @param id    id del usuario
     * @param money cantidad de dinero a añadir
     * @return UserDTO con el saldo actualizado
     *
     */
    public UserDTO addBalanceToWallet(Long id, Float money) throws IllegalArgumentException, ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que se se haya pasado por parametro alguna cantidad de dinero
        if (money == null) {
            errors.add(new ErrorDto("Money", ErrorType.NO_ENCONTRADO));
        }else {
            //Compruebo que la cantidad de saldo que intenta agregar el usuario está entre 5 y 500
            if (money < MIN_VALUE || money > MAX_VALUE) {
                errors.add(new ErrorDto("Money", ErrorType.FORMATO_INVALIDO));
            }
        }

        //En caso de que el money lo hayan pasado mal lanzo la exepcion antes de iniciar la transaccion
        Util.thowException(errors);

        //Inicio transaccion
        UserEntity updatedUser = tm.inTransaction(()->{
            UserEntity userOpt = userRepo.getById(id).orElse(null);
            if (userOpt == null) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }else{
                //Compruebo que la cuenta del usuario que se encontró este activa
                //Si lo meto dentro de este else evito un posible nullPointedExeption en caso de que userOpt no se haya encontrado y sea null e intente hacer un getAccountState()
                if (!userOpt.getAccountState().equals(AccountState.ACTIVE)) {
                    errors.add(new ErrorDto("AccountState", ErrorType.FORMATO_INVALIDO));
                }

            }

            //Si hay errores en el usuario mando una ilegalArgumentExeption para que la funcion inTransaction la capture en el catch y haga un rollback de la transaccion
            if(!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            //Calculo el nuevo saldo del usuario
            float newBalance = userOpt.getPortfolioBalance() + money;

            UserUpdate userForm = new UserUpdate(userOpt.getUserName(), userOpt.getEmail(), userOpt.getPassword(), userOpt.getRealName(), userOpt.getCountry(), userOpt.getBirthDate(), userOpt.getRegistrationDate(), userOpt.getAvatar(), newBalance, userOpt.getAccountState());

            return userRepo.update(id, userForm).orElse(null);
        });

        if (updatedUser == null) {
            errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
        }

        //Vuelvo a comprobar si hay errores mando una validation exeption
        Util.thowException(errors);

        return Mapper.mapFrom(updatedUser);
    }


    /**
     * Comprueba dinero en la cartera virtual de Steam del usuario
     *
     * @param id id del usuaio
     * @return UserDTO con todos sus datos
     *
     */
    public UserDTO showBalanceFromWallet(Long id) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio transaccion
        UserEntity user = tm.inTransaction(()->{
            return userRepo.getById(id).orElse(null);
        });

        //Si no encuentra al usuario manda agrega una exepcion a la lista de errores
        if (user == null) {
            errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo si hay errores y mando una validation exeption en caso de haber
        Util.thowException(errors);

        return Mapper.mapFrom(user);
    }



    /**
     * Realiza las validaciones del UserForm que necesitan acceso a datos
     * @param user Formulario con los datos introducidos por el usuario
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     *
     */
    public List<ErrorDto> validate(UserForm user) {
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
