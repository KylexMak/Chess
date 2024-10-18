package dataaccess;

import model.AuthData;

public interface AuthDao {
    void createAuthData(AuthData authData);
    AuthData getAuthData(String authToken) throws DataAccessException;
    void deleteAuthData(String authToken) throws DataAccessException;
    void clearAllAuthData();
}
