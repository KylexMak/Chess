package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    public void addUser(UserData user) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void deleteUser(String username) throws DataAccessException;
    public void clearUsers() throws DataAccessException;
}
