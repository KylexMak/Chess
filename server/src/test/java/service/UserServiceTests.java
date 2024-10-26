package service;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();

    public UserServiceTests() throws ResponseException, DataAccessException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException, ResponseException {
        userService.register(new UserData("test", "testPass", "test@email.com"));
    }

    @AfterEach
    public void cleanUp() throws ResponseException{
        userService.clearUsers();
    }

    @Test
    public void register() throws DataAccessException, ResponseException{
        AuthData newUser = userService.register(new UserData("tacoTime", "password", "tacoTime@gmail.com"));
        Assertions.assertEquals(newUser.username(), userService.getByUsername(newUser.username()).username());
    }

    @Test
    public void alreadyRegistered(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,
                () -> userService.register(new UserData("test", "testPass", "test@email.com")));
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    public void userDataInvalid(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,
                () -> userService.register(new UserData(null, "ague", "something@something")));
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    public void login() throws DataAccessException, ResponseException{
        UserData user = new UserData("test", "testPass", "test@email.com");
        AuthData loggedIn = userService.login(user);
        Assertions.assertEquals(loggedIn.username(), user.username());
    }

    @Test
    public void loginWrongPassword() throws ResponseException{
        UserData user = userService.getByUsername("test");
        UserData wrongPassword = new UserData(user.username(), "fail", user.email());
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> userService.login(wrongPassword));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void logout() throws DataAccessException, ResponseException{
        UserData user = new UserData("test", "testPass", "test@email.com");
        AuthData userLogin = userService.login(user);
        userService.logout(userLogin.authToken());
        AuthData authData = authService.getAuthData(userLogin.authToken());
        Assertions.assertNull(authData);
    }

    @Test
    public void logoutUnauthorized() throws DataAccessException, ResponseException{
        UserData user = new UserData("test", "testPass", "test@email.com");
        AuthData userLogin = userService.login(user);
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> userService.logout("invalidToken"));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void getUser() throws ResponseException{
        UserData user = new UserData("test", "testPass", "test@email.com");
        Assertions.assertEquals(user.username(), userService.getByUsername(user.username()).username());
    }

    @Test
    public void getNonexistentUser() throws ResponseException{
        Assertions.assertNull(userService.getByUsername("INoExist"));
    }
}
