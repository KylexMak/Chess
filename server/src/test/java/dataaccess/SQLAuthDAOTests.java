package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;


public class SQLAuthDAOTests {
    private AuthService authService;

    @BeforeEach
    public void setUp() throws DataAccessException, ResponseException{
        authService = new AuthService();
        authService.createAuthData("Test");
    }
    @AfterEach
    public void cleanUp() throws ResponseException{
        authService.clearAllAuthData();
    }

    @Test
    public void createAuthPositive() throws ResponseException{
        AuthData auth = authService.createAuthData("PositiveTest");
        Assertions.assertNotNull(authService.getAuthData(auth.authToken()));
    }

    @Test
    public void createAuthNegative() throws ResponseException{
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> authService.createAuthData("NegativeTest"));
        //Assertions.assertEquals();
    }
}
