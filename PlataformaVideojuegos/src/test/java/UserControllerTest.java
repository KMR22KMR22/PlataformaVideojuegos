import Model.User.CountState;
import Model.User.Countries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Controller.UserController;
import Model.User.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserControllerTest {

    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();

        user = new User(
                1,
                "juan123",
                "juan.perez@email.com",
                "Password123",
                "Juan Pérez",
                Countries.SPAIN,
                LocalDate.of(2005, 3, 15),
                LocalDate.now(),
                2500.75f,
                CountState.ACTIVE
        );
    }

    @Test
    public void validateUserNameTest(){
        var realValue = userController.validateUserName(user).size();
        assertEquals(0,realValue);
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameIsEmpty() {
        user.setUserName(""); // vacío
        List<String> errores = userController.validateUserName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Nombre obligatorio"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameIsTooShort() {
        user.setUserName("ab"); // < 3 caracteres
        List<String> errores = userController.validateUserName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre de usuario debe tener minimo 3 caracteres"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameIsTooLong() {
        user.setUserName("abcdefghijklmnopqrstu"); // > 20 caracteres
        List<String> errores = userController.validateUserName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre de usuario debe tener maximo 20 caracteres"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameHasInvalidCharacters() {
        user.setUserName("juan$123"); // caracter inválido
        List<String> errores = userController.validateUserName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Solo se admiten caracteres alfanuméricos, guiones y guiones bajos"));
    }

    @Test
    void validateUserName_shouldReturnError_whenUserNameStartsWithNumber() {
        user.setUserName("1juan"); // empieza por número
        List<String> errores = userController.validateUserName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre de usuario no puede empezar por numero"));
    }






    @Test
    public void validateUserEmailTest(){
        var realValue = userController.validateEmail(user).size();
        assertEquals(0,realValue);
    }

    @Test
    void validateEmail_shouldReturnError_whenEmailIsEmpty() {
        user.setEmail(""); //Vacio
        var errores = userController.validateEmail(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Email obligatorio"));
    }

    @Test
    void validateEmail_shouldReturnError_whenEmailFormatIsInvalid() {
        user.setEmail("email-invalido"); //Formato invalido
        var errores = userController.validateEmail(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Debe ingresar un formato valido de email"));
    }






    @Test
    public void validateUserPasswordTest(){
        var realValue = userController.validatePassword(user).size();
        assertEquals(0,realValue);
    }

    @Test
    void validatePassword_shouldReturnError_whenPasswordIsEmpty() {
        user.setPassword(""); //Vacio
        var errores = userController.validatePassword(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Contraseña obligatoria"));
    }

    @Test
    void validatePassword_shouldReturnError_whenPasswordIsTooShort() {
        user.setPassword("123"); // demasiado corta
        var errores = userController.validatePassword(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("La contraseña debe tener minimo 8 caracteres"));
    }

    @Test
    void validatePassword_shouldReturnError_whenPasswordHasNoNumbers() {
        user.setPassword("Password"); //Formato incorrecto
        var errores = userController.validatePassword(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("La contraseña debe contener al menos una mayúscula, una minúscula y un número"));
    }






    @Test
    void validateRealNameTest(){
        var realValue = userController.validateRealName(user).size();
        assertEquals(0,realValue);
    }

    @Test
    void validateRealName_shouldReturnError_whenRealNameIsEmpty() {
        user.setRealName(""); //Vaio
        var errores = userController.validateRealName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("Nombre obligatorio"));
    }

    @Test
    void validateRealName_shouldReturnError_whenRealNameIsTooShort() {
        user.setRealName("J"); //Nombre con menos de dos caracteres
        var errores = userController.validateRealName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre debe tener mas de 2 caracteres"));
    }

    @Test
    void validateRealName_shouldReturnError_whenRealNameIsTooLong() {
        user.setRealName("J".repeat(51)); //Nombre con menos de dos caracteres
        var errores = userController.validateRealName(user);
        assertEquals(1, errores.size());
        assertTrue(errores.contains("El nombre no puede tener mas de 50 caracteres"));
    }





}
