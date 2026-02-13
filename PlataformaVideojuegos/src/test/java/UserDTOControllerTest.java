import org.example.Model.DTO.User.AccountState;
import Model.DTO.User.Countries;
import org.example.Model.DTO.User.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.Controller.UserController;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserDTOControllerTest {

    private UserController userController;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userController = new UserController();

        userDTO = new UserDTO(
                1,
                "juan123",
                "juan.perez@email.com",
                "Password123",
                "Juan Pérez",
                Countries.SPAIN,
                LocalDate.of(2005, 3, 15),
                LocalDate.now(),
                2500.75f,
                AccountState.ACTIVE
        );
    }

    @Test
    public void validateUserNameTest(){
        var realValue = userController.validateUserName(userDTO).size();
        assertEquals(0,realValue);
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameIsEmpty() {
        userDTO.setUserName(""); // vacío
        List<String> errores = userController.validateUserName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Nombre obligatorio"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameIsTooShort() {
        userDTO.setUserName("ab"); // < 3 caracteres
        List<String> errores = userController.validateUserName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre de usuario debe tener minimo 3 caracteres"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameIsTooLong() {
        userDTO.setUserName("abcdefghijklmnopqrstu"); // > 20 caracteres
        List<String> errores = userController.validateUserName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre de usuario debe tener maximo 20 caracteres"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameHasInvalidCharacters() {
        userDTO.setUserName("juan$123"); // caracter inválido
        List<String> errores = userController.validateUserName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Solo se admiten caracteres alfanuméricos, guiones y guiones bajos"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameStartsWithNumber() {
        userDTO.setUserName("1juan"); // empieza por número
        List<String> errores = userController.validateUserName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre de usuario no puede empezar por numero"));
    }






    @Test
    public void validateUserEmailTest(){
        var realValue = userController.validateEmail(userDTO).size();
        assertEquals(0,realValue);
    }

    @Test
    void validateEmail_shouldReturnError_whenEmailIsEmpty() {
        userDTO.setEmail(""); //Vacio
        var errores = userController.validateEmail(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Email obligatorio"));
    }

    @Test
    void validateEmail_shouldReturnError_whenEmailFormatIsInvalid() {
        userDTO.setEmail("email-invalido"); //Formato invalido
        var errores = userController.validateEmail(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Debe ingresar un formato valido de email"));
    }






    @Test
    public void validateUserPasswordTest(){
        var realValue = userController.validatePassword(userDTO).size();
        assertEquals(0,realValue);
    }

    @Test
    void validatePassword_shouldReturnError_whenPasswordIsEmpty() {
        userDTO.setPassword(""); //Vacio
        var errores = userController.validatePassword(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Contraseña obligatoria"));
    }

    @Test
    void validatePassword_shouldReturnError_whenPasswordIsTooShort() {
        userDTO.setPassword("123"); // demasiado corta
        var errores = userController.validatePassword(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("La contraseña debe tener minimo 8 caracteres"));
    }

    @Test
    void validatePassword_shouldReturnError_whenPasswordHasNoNumbers() {
        userDTO.setPassword("Password"); //Formato incorrecto
        var errores = userController.validatePassword(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("La contraseña debe contener al menos una mayúscula, una minúscula y un número"));
    }






    @Test
    void validateRealNameTest(){
        var realValue = userController.validateRealName(userDTO).size();
        assertEquals(0,realValue);
    }

    @Test
    void validateRealName_shouldReturnError_whenRealNameIsEmpty() {
        userDTO.setRealName(""); //Vaio
        var errores = userController.validateRealName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Nombre obligatorio"));
    }

    @Test
    void validateRealName_shouldReturnError_whenRealNameIsTooShort() {
        userDTO.setRealName("J"); //Nombre con menos de dos caracteres
        var errores = userController.validateRealName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre debe tener mas de 2 caracteres"));
    }

    @Test
    void validateRealName_shouldReturnError_whenRealNameIsTooLong() {
        userDTO.setRealName("J".repeat(51)); //Nombre con menos de dos caracteres
        var errores = userController.validateRealName(userDTO);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre no puede tener mas de 50 caracteres"));
    }





}
