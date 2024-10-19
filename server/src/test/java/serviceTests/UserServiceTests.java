package serviceTests;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

public class UserServiceTests {
    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();
    @BeforeEach
    public void setUp() throws DataAccessException {
        userService.register(new UserData("test", "testPass", "test@email.com"));
    }

    @AfterEach
    public void cleanUp(){
        userService.clearUsers();
    }

    @Test
    public void register() throws DataAccessException{
        AuthData newUser = userService.register(new UserData("tacoTime", "password", "tacoTime@gmail.com"));
        Assertions.assertEquals(newUser.userName(), userService.getByUsername(newUser.userName()).username());
    }

    @Test
    public void alreadyRegistered(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> userService.register(new UserData("test", "testPass", "test@email.com")));
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    public void userDataInvalid(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> userService.register(new UserData(null, "ague", "something@something")));
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    public void login() throws DataAccessException{
        UserData user = userService.getByUsername("test");
        AuthData loggedIn = userService.login(user);
        Assertions.assertEquals(loggedIn.userName(), user.username());
    }

    @Test
    public void loginWrongPassword(){
        UserData user = userService.getByUsername("test");
        UserData wrongPassword = new UserData(user.username(), "fail", user.email());
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> userService.login(wrongPassword));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void logout() throws DataAccessException{
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        userService.logout(userLogin.authToken());
        AuthData authData = authService.getAuthData(userLogin.authToken());
        Assertions.assertNull(authData);
    }

    @Test
    public void logoutUnauthorized() throws DataAccessException{
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> userService.logout("invalidToken"));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void getUser(){
        UserData user = new UserData("test", "testPass", "test@email.com");
        Assertions.assertEquals(user, userService.getByUsername(user.username()));
    }

    @Test
    public void getNonexistentUser(){
        Assertions.assertNull(userService.getByUsername("INoExist"));
    }
}
