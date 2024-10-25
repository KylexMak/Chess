package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    UserDAO userDb = new SQLUserDAO();
    AuthService authService = new AuthService();

    public UserService() throws ResponseException, DataAccessException {

    }

    public AuthData register(UserData user) throws DataAccessException, ResponseException {
        if(userDb.getUser(user.username()) == null){
            if(user.username() == null || user.password() == null || user.email() == null){
                throw new DataAccessException("Error: bad request");
            }
            else{
                userDb.addUser(user);
                return authService.createAuthData(user.username());
            }
        }
        else{
            throw new DataAccessException("Error: already taken");
        }
    }

    public AuthData login(UserData user) throws DataAccessException, ResponseException{
        UserData assumedUser = userDb.getUser(user.username());
        if(assumedUser != null){
            if(BCrypt.checkpw(user.password(), assumedUser.password())){
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

    public void logout(String authToken) throws DataAccessException, ResponseException {
        if(authService.getAuthData(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        else{
            authService.deleteAuthData(authToken);
        }
    }

    public UserData getByUsername(String userName) throws ResponseException{
        return userDb.getUser(userName);
    }

    public void clearUsers() throws ResponseException{
        userDb.clearUsers();
    }
}
