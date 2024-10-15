package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    AuthDao authDb = new MemoryAuthDAO();
    UserDAO userDb = new MemoryUserDAO();
    AuthService authService = new AuthService();

    public AuthData register(UserData user) throws DataAccessException{
        if(userDb.getUser(user.username()) == null){
            if(user.username() == null || user.password() == null || user.email() == null){
                throw new DataAccessException("Error: bad request");
            }
        }
        else{
            userDb.addUser(user);
        }

        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        authDb.createAuthData(auth);
        return auth;
    }

    public AuthData login(UserData user) throws DataAccessException{
        if(userDb.getUser(user.username()) != null){
            UserData assumedUser = userDb.getUser(user.username());
            if(assumedUser.password().equals(user.password())){
                return authService.createAuthData(user.username());
            }
            else{
                throw new DataAccessException("Error: unauthorized");
            }
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        if(authDb.getAuthData(authToken) == null){
            throw new DataAccessException("Error: unauthroized");
        }
        else{
            authDb.deleteAuthData(authToken);
        }
    }

    public void deleteUser(String username) throws DataAccessException{
        userDb.deleteUser(username);
    }

    public void clearUsers() throws DataAccessException{
        userDb.clearUsers();
    }
}
