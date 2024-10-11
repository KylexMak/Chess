package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDao {
    public static HashMap<String, AuthData> allAuthData = new HashMap<>();
    @Override
    public void createAuthData(AuthData authData) throws DataAccessException {
        String authToken = authData.authToken();
        allAuthData.put(authToken, authData);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return allAuthData.get(authToken);
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {
        allAuthData.remove(authToken);
    }

    @Override
    public void clearAllAuthData() throws DataAccessException {
        allAuthData.clear();
    }
}
