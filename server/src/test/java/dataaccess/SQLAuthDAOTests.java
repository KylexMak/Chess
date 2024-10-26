package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SQLAuthDAOTests {
    private AuthDao authDB;

    @BeforeEach
    public void setUp() throws DataAccessException, ResponseException{
        authDB = new SQLAuthDAO();
        authDB.createAuthData(new AuthData("test", "Test"));
    }
    @AfterEach
    public void cleanUp() throws ResponseException{
        authDB.clearAllAuthData();
    }

    @Test
    public void createAuthPositive() throws ResponseException{
        authDB.createAuthData(new AuthData("positive", "PositiveTest"));
        Assertions.assertNotNull(authDB.getAuthData("positive"));
    }

    @Test
    public void createAuthNegative() throws ResponseException{
        authDB.createAuthData(new AuthData("negative", "NegativeTest"));
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> authDB.createAuthData(new AuthData("negative", "NegativeTest")));
        Assertions.assertNotNull(exception);
    }

    @Test
    public void getAuthPositive() throws ResponseException{
        Assertions.assertNotNull(authDB.getAuthData("test"));
    }

    @Test
    public void getAuthNegative() throws ResponseException{
        Assertions.assertNull(authDB.getAuthData("random"));
    }

    @Test
    public void deleteAuthPositive() throws ResponseException{
        authDB.deleteAuthData("test");
        Assertions.assertNull(authDB.getAuthData("test"));
    }

    @Test
    public void deleteAuthNegative() throws ResponseException{
        authDB.deleteAuthData("random");
        Assertions.assertNotNull(authDB.getAuthData("test"));
    }

    @Test
    public void clearPositive() throws ResponseException{
        authDB.clearAllAuthData();
        Assertions.assertNull(authDB.getAuthData("test"));
    }
}
