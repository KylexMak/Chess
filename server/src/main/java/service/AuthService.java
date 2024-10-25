package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;

import java.util.UUID;

public class AuthService {
    AuthDao authDb = new SQLAuthDAO();

    public AuthService() throws ResponseException, DataAccessException {

    }

    public AuthData createAuthData(String username) throws ResponseException {
        String authToken = UUID.randomUUID().toString();
        AuthData toCreate = new AuthData(authToken, username);
        authDb.createAuthData(toCreate);
        return toCreate;
    }

    public AuthData getAuthData(String authToken) throws ResponseException{
        return authDb.getAuthData(authToken);
    }

    public void deleteAuthData(String authToken) throws ResponseException{
        authDb.deleteAuthData(authToken);
    }

    public void clearAllAuthData() throws ResponseException{
        authDb.clearAllAuthData();
    }
}
