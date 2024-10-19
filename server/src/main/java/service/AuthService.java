package service;

import dataaccess.*;
import model.AuthData;

import java.util.UUID;

public class AuthService {
    AuthDao authDb = new MemoryAuthDAO();

    public AuthData createAuthData(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData toCreate = new AuthData(authToken, username);
        authDb.createAuthData(toCreate);
        return toCreate;
    }

    public AuthData getAuthData(String authToken){
        return authDb.getAuthData(authToken);
    }

    public void deleteAuthData(String authToken) throws DataAccessException{
        authDb.deleteAuthData(authToken);
    }

    public void clearAllAuthData() {
        authDb.clearAllAuthData();
    }
}
