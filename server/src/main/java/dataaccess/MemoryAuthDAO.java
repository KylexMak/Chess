package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDao {
    public static HashMap<String, AuthData> allAuthData = new HashMap<>();
    @Override
    public void createAuthData(AuthData authData){
        String authToken = authData.authToken();
        allAuthData.put(authToken, authData);
    }

    @Override
    public AuthData getAuthData(String authToken){
        return allAuthData.get(authToken);
    }

    @Override
    public void deleteAuthData(String authToken) {
        AuthData authData = getAuthData(authToken);
        allAuthData.remove(authData.authToken());
    }

    @Override
    public void clearAllAuthData(){
        allAuthData.clear();
    }
}
