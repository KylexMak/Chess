package serviceTests;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;

public class AuthServiceTests {
    private final AuthService authService = new AuthService();
    @BeforeEach
    public void mockAuthDB(){
        authService.createAuthData("testUserName");
    }
    @AfterEach
    public void resetAuthDB(){
        authService.clearAllAuthData();
    }

    @Test
    public void addingAuthData() throws DataAccessException {
        AuthData auth = authService.createAuthData("newTestUserName");
        Assertions.assertEquals(auth, authService.getAuthData(auth.authToken()));
    }

    @Test
    public void deleteAuthData() throws DataAccessException {
        AuthData auth = authService.createAuthData("newTestUserName");
        authService.deleteAuthData(auth.authToken());
        DataAccessException error = Assertions.assertThrows(DataAccessException.class, () -> authService.getAuthData(auth.authToken()));
        Assertions.assertEquals("Error: There is no user with authToken: " + auth.authToken(), error.getMessage());
    }

    @Test
    public void getAuthData() throws DataAccessException{
        AuthData auth = authService.createAuthData("newTestUserName");
        AuthData retrieved = authService.getAuthData(auth.authToken());
        Assertions.assertEquals(auth, retrieved);
    }
}
