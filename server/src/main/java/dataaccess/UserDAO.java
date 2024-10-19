package dataaccess;

import model.UserData;

public interface UserDAO {
    void addUser(UserData user);
    UserData getUser(String username);
    void clearUsers();
}
