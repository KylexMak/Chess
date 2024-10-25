package service;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthServiceTests {
    private final AuthService authService = new AuthService();

    public AuthServiceTests() throws ResponseException, DataAccessException {
    }

    @BeforeEach
    public void mockAuthDB() throws ResponseException{
        authService.createAuthData("testUserName");
    }
    @AfterEach
    public void resetAuthDB() throws ResponseException{
        authService.clearAllAuthData();
    }

    @Test
    public void addingAuthData() throws ResponseException {
        AuthData auth = authService.createAuthData("newTestUserName");
        Assertions.assertEquals(auth, authService.getAuthData(auth.authToken()));
    }

    @Test
    public void deleteAuthData() throws DataAccessException, ResponseException {
        AuthData auth = authService.createAuthData("newTestUserName");
        authService.deleteAuthData(auth.authToken());
        Assertions.assertNull(authService.getAuthData(auth.authToken()));
    }

    @Test
    public void getAuthData() throws ResponseException{
        AuthData auth = authService.createAuthData("newTestUserName");
        AuthData retrieved = authService.getAuthData(auth.authToken());
        Assertions.assertEquals(auth, retrieved);
    }
}
