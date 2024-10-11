package dataaccess;

import model.AuthData;
import org.eclipse.jetty.client.HttpResponseException;

public interface AuthDao {
    public void createAuthData(AuthData authData) throws DataAccessException;
    public AuthData getAuthData(String authToken) throws DataAccessException;
    public void deleteAuthData(String authToken) throws DataAccessException;
    public void clearAllAuthData() throws DataAccessException;
}
