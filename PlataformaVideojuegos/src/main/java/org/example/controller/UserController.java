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
        //Compruebo que el formulario no sea null
        if (userForm == null){
            throw new ValidationException(List.of(new ErrorDto("Form", ErrorType.REQUERIDO)));
        }

        //Inicio transaccion
        var createdUser = tm.inTransaction(()->{
            List<ErrorDto> errors = new ArrayList<>();

            //LLamo al validate del formulario y guardo la lista de errores
            errors.addAll(userForm.validate());

            //LLamo al validate del controlador y guardo la lista de errores
            errors.addAll(validate(userForm));

            //Vuelvo a comprobar si hay errores mando una validation exeption
            Util.throwException(errors);

            return userRepo.create(userForm);
        }).orElse(null);

        return Mapper.mapFrom(createdUser);
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
        if (user == null) {
        errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));}

        //Compruebo si hay errores en la lista de errores para lanzar exepcion
        Util.throwException(errors);

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
    public UserDTO addBalanceToWallet(Long id, Float money) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que id no sea null
        if (id == null){
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        //Compruebo que se haya pasado por parametro alguna cantidad de dinero
        if (money == null) {
            errors.add(new ErrorDto("Money", ErrorType.REQUERIDO));
        }else {
            //Compruebo que la cantidad de saldo que intenta agregar el usuario está entre 5 y 500
            if (money < MIN_VALUE) {
                errors.add(new ErrorDto("Money", ErrorType.VALOR_DEMASIADO_BAJO));
            }
            if (money > MAX_VALUE){
                errors.add(new ErrorDto("Money", ErrorType.VALOR_DEMASIADO_ALTO));
            }
        }

        //En caso de que el money lo hayan pasado mal lanzo la exepcion antes de iniciar la transaccion
        Util.throwException(errors);

        //Inicio transaccion
        UserEntity updatedUser = tm.inTransaction(()->{
            List<ErrorDto> transactionErrors = new ArrayList<>();

            UserEntity user = userRepo.getById(id).orElse(null);
            if (user == null) {
                transactionErrors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }else{
                //Compruebo que la cuenta del usuario que se encontró este activa
                //Si lo meto dentro de este else evito un posible nullPointedExeption en caso de que userOpt no se haya encontrado y sea null e intente hacer un getAccountState()
                if (!user.getAccountState().equals(AccountState.ACTIVE)) {
                    transactionErrors.add(new ErrorDto("AccountState", ErrorType.FORMATO_INVALIDO));
                }

            }
            //Compruebo si hay errores mando una validation exeption
            Util.throwException(transactionErrors);

            //Calculo el nuevo saldo del usuario
            float newBalance = user.getPortfolioBalance() + money;

            //Creo el formulario del usuario con el nuevo saldo
            UserUpdate userForm = new UserUpdate(user.getUserName(), user.getEmail(), user.getPassword(), user.getRealName(), user.getCountry(), user.getBirthDate(), user.getRegistrationDate(), user.getAvatar(), newBalance, user.getAccountState());

            //Actualizo el usuario
            return userRepo.update(id, userForm).orElse(null);
        });

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

        //Compruebo que id no sea null
        if (id == null){
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        Util.throwException(errors);

        //Inicio transaccion
        UserEntity user = tm.inTransaction(()->{
            return userRepo.getById(id).orElse(null);
        });

        //Si no encuentra al usuario manda agrega una exepcion a la lista de errores
        if (user == null) {
            errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo si hay errores y mando una validation exeption en caso de haber
        Util.throwException(errors);

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

        //Compruebo que el formulario no venga null
        if (user == null) {
            errores.add(new ErrorDto("UserForm", ErrorType.REQUERIDO));
        }else {
            //Guardo todos los usuarios en una variable para no tener que acceder varias veces a la base de datos haciendo un userRepo.getAll()
            List<UserEntity> users = userRepo.getAll();

            //Valida que el nombre de usuario no se repita
            if (users.stream().anyMatch(e -> e.getUserName().equals(user.userName()))) {
                errores.add(new ErrorDto("Name", ErrorType.DUPLICADO));
            }
            //Valida que el email no se repita
            if (users.stream().anyMatch(u -> u.getEmail().equals(user.email()))) {
                errores.add(new ErrorDto("Email", ErrorType.DUPLICADO));
            }
            //Valida que el pais coincida con alguno de la lista del repositorio de paises
            if (countryRepo.getAll().stream().noneMatch(c -> c.getName().equals(user.country()))) {
                errores.add(new ErrorDto("Country", ErrorType.NO_ENCONTRADO));
            }
        }
        return errores;
    }
}
