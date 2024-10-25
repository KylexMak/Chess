package dataaccess;

import exception.ResponseException;
import model.UserData;

public interface UserDAO {
    void addUser(UserData user) throws ResponseException;
    UserData getUser(String username) throws ResponseException;
    void clearUsers() throws ResponseException;
}
