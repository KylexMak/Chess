package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SQLUserDAOTests {
    private UserDAO userDb;

    @BeforeEach
    public void setUp() throws DataAccessException , ResponseException{
        userDb = new SQLUserDAO();
        userDb.addUser(new UserData("test", "testPassword", "test@test.com"));
    }
    @AfterEach
    public void cleanUp() throws ResponseException{
        userDb.clearUsers();
    }

    @Test
    public void addingUserPositive() throws ResponseException{
        userDb.addUser(new UserData("positive", "password", "positive@test.com"));
        Assertions.assertNotNull(userDb.getUser("positive"));
    }

    @Test
    public void addingUserNegative(){
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> userDb.addUser(new UserData(null, "password", "negative@test.com")));
        Assertions.assertNotNull(exception);
    }

    @Test
    public void getUserPositive() throws ResponseException{
        Assertions.assertNotNull(userDb.getUser("test"));
    }

    @Test
    public void getUserNegative() throws ResponseException{
        Assertions.assertNull(userDb.getUser("random"));
    }

    @Test
    public void clearUserPositive() throws ResponseException{
        userDb.clearUsers();
        Assertions.assertNull(userDb.getUser("test"));
    }
}
