package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    void addUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    void clearUsers() throws DataAccessException;
}
