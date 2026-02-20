package org.example.Controller;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Errors.UserErrors;
import org.example.Model.Form.UserForm;
import org.example.Repository.InMemory.CountryRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserController {

    private UserRepoInMemory userRepository = new UserRepoInMemory();
    private CountryRepoInMemory countryRepository = new CountryRepoInMemory();


    /**Crea un nuevo usuario
     * @param user
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> registerNewUser(UserForm user) {

        List<String> errores = new ArrayList<>();

        //LLamo al validate del formulario y guardo la lista de errores
        errores.addAll(user.validate());
        //LLamo al validate del controlador y guardo la lista de errores
        errores.addAll(validate(user));

        //Si no se detectaron errores en el formulario se llamo a la funcion de cear nueva UserEntity que esta en el repositorio
        if (errores.isEmpty()) {
            userRepository.crear(user);
        }

        return errores;
    }








    /**Muestra la informacion de un usuario especifico
     * @param id
     * @return Lista con los datos del usuario, o con el mensaje de usuario no encontrado
     * */
    public List<String> viewProfileByID(Long id){

        List<String> autput = new ArrayList<>();

        //Aqui busco al usuario y lo guardo como optional ya que si no lo encuentra me dara un null
        Optional<UserEntity> user = userRepository.obtenerPorId(id);


        //El isPresent es para comprobar si un optional tiene algun valor dentro
        //Para acceder al valor que esta dentro del optional hay que usar un get, y luego ya uso los getters normales para otener cada atributo que necesito mostrar del usuario
        if(user.isPresent()){
            autput.add(user.get().getUserName());
            autput.add(user.get().getAvatar());
            autput.add(user.get().getCountry());
            autput.add(user.get().getRegistrationDate().toString());
            autput.add(user.get().getUserName());
        }else {
            autput.add(GenericErrors.NOT_FOUND.getMessage());
        }
        return autput;
    }

    /**Muestra la informacion de un usuario especifico
     * @param userName
     * @return Lista con los datos del usuario, o con el mensaje de usuario no encontrado
     * */
    public List<String> viewProfileByUserName(String userName){

        List<String> autput = new ArrayList<>();

        Optional<UserEntity> user = userRepository.obtenerTodos().stream().filter(u->u.getUserName().equals(userName)).findFirst();

        if(user.isPresent()){
            autput.add(user.get().getUserName());
            autput.add(user.get().getAvatar());
            autput.add(user.get().getCountry());
            autput.add(user.get().getRegistrationDate().toString());
            autput.add(user.get().getUserName());
        }else {
            autput.add(GenericErrors.NOT_FOUND.getMessage());
        }
        return autput;
    }




    /**Recarga dinero en la cartera virtual de Steam del usuario
     * @param id, cantidad
     * @return Nuevo saldo de la cartera o mensaje de error
     * */
    public String addBalanceToWallet(Long id, Optional<Float> money){

        try {
            UserEntity user = userRepository.actualizarMoney(id, money);
            return "Su nuevo saldo es: " + user.getPortfolioBalance();
        }catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }
























    //Validaciones que dependen del usuario, pero requieren acceso a datos
    public List<String>  validate(UserForm user) {

        List<String> errores = new ArrayList<>();

        //Valida que el nombre de usuario no se repita
        if (userRepository.obtenerTodos().stream().anyMatch(e -> e.getUserName().equals(user.getUserName()))) {
            errores.add(GenericErrors.ALREADY_EXISTS.getMessage());
        }
        //Valida que el email no se repita
        if (userRepository.obtenerTodos().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            errores.add(GenericErrors.ALREADY_EXISTS.getMessage());
        }
        //Valida que el pais coincida con alguno de la lista del repositorio de paises
        if (countryRepository.getCountries().stream().noneMatch(c -> c.equals(user.getCountry()))) {
            errores.add(GenericErrors.NOT_EXISTS.getMessage());
        }

        return errores;

    }

    //Validaciones para modificaiones del usuario

    public List<String> validateUserUpdate(UserEntity user) {
        List<String> errores = new ArrayList<>();

        //Valida que el saldo de la cuenta del usuario sea positivo o cero
        if(user.getPortfolioBalance() < 0){
            errores.add(UserErrors.MONEY_BELOW_CERO.getMessage());
        }
        //Falta comprobar que el saldo tenga maximo 2 decimales

        //El estado de la cuenta debe ser uno entre los valores del enum
        if(Arrays.stream(AccountState.values()).noneMatch(s -> s.equals(user.getAccountState()) )) {
            errores.add(GenericErrors.NOT_EXISTS.getMessage());
        }

        return errores;

    }

    //Validaciones que no dependen del usuario (Reglas de inicializacion)

    //Fecha de registro automatica
    //Saldo valor por defecto 0 y maximo 2 decimales
    //Estado de la cuenta por defecto activa









}
