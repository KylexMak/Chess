package dataaccess;

import model.AuthData;
import org.eclipse.jetty.client.HttpResponseException;

public interface AuthDao {
    void createAuthData(AuthData authData) throws DataAccessException;
    AuthData getAuthData(String authToken) throws DataAccessException;
    void deleteAuthData(String authToken) throws DataAccessException;
    void clearAllAuthData() throws DataAccessException;
}
