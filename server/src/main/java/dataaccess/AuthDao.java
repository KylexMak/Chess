package dataaccess;

import exception.ResponseException;
import model.AuthData;

public interface AuthDao {
    void createAuthData(AuthData authData) throws ResponseException;
    AuthData getAuthData(String authToken) throws ResponseException;
    void deleteAuthData(String authToken) throws ResponseException;
    void clearAllAuthData() throws ResponseException;
}
