package org.example.Controller;
import org.example.Exeptions.GenericExeption;
import org.example.Model.DTO.User.AccountState;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Errors.UserErrors;
import org.example.Model.Form.UserForm;
import org.example.Repository.InMemory.CountryRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserController {

    private UserRepoInMemory userRepository = new UserRepoInMemory();
    private CountryRepoInMemory countryRepository = new CountryRepoInMemory();


    /**Crea un nuevo usuario
     * @param userForm
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> registerNewUser(UserForm userForm) {

        List<String> errores = new ArrayList<>();

        //LLamo al validate del formulario y guardo la lista de errores
        errores.addAll(userForm.validate());
        //LLamo al validate del controlador y guardo la lista de errores
        errores.addAll(validate(userForm));

        //Si no se detectaron errores en el formulario se llamo a la funcion de cear nueva UserEntity que esta en el repositorio
        if (errores.isEmpty()) {
            userRepository.crear(userForm);
        }

        return errores;
    }



    /**Muestra la informacion de un usuario especifico
     * @param id
     * @return Lista con los datos del usuario, o con el mensaje de usuario no encontrado
     * */
    public List<String> viewProfileByID(Long id){

        //Aqui busco al usuario y lo guardo si lo encuentra, si no se encuentra habra un null, por lo qeu voy a devolver una exepcion
        UserEntity user = userRepository.obtenerPorId(id).orElseThrow(() -> new GenericExeption(GenericErrors.NOT_EXISTS.getMessage()));

        //Me falta mostrar biblioteca y estadisticas de juego
        return List.of(user.getUserName(), user.getAvatar(), user.getCountry(), user.getRegistrationDate().toString());
    }


    /**Muestra la informacion de un usuario especifico
     * @param userName
     * @return Lista con los datos del usuario, o con el mensaje de usuario no encontrado
     * */
    public List<String> viewProfileByUserName(String userName){

        Optional<UserEntity> user = userRepository.obtenerTodos().stream().filter(u->u.getUserName().equals(userName)).findFirst();

        UserEntity userOpt = user.orElseThrow(() -> new GenericExeption(GenericErrors.NOT_EXISTS.getMessage()));

        return List.of(userOpt.getUserName(), userOpt.getAvatar(), userOpt.getCountry(), userOpt.getRegistrationDate().toString());
    }




    /**Recarga dinero en la cartera virtual de Steam del usuario
     * @param id, cantidad
     * @return Nuevo saldo de la cartera o mensaje de error
     * */
    public float addBalanceToWallet(Long id, Optional<Float> money){
        UserEntity user;

        try {
          user  = userRepository.updatePortafolioBalance(id,  money);
        }catch (Exception e){
            throw e;
        }

        return user.getPortfolioBalance();
    }


    /**Comprueba dinero en la cartera virtual de Steam del usuario
     * @param id
     * @return Saldo actual de la cartera
     * */
    public float showBalanceFromWallet(Long id){

        UserEntity user = userRepository.obtenerPorId(id).orElseThrow(() -> new GenericExeption(GenericErrors.NOT_EXISTS.getMessage()));
        return user.getPortfolioBalance();
    }




    //Validaciones que dependen del usuario, pero requieren acceso a datos
    /**Realiza las validaciones del UserForm que necesitan acceso a datos
     * @param user
     * @return Lista con errores en caso de haber
     * */
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
}
