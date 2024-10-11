package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO{
    public static List<UserData> allUsers = new ArrayList<>();
    @Override
    public void addUser(UserData user) throws DataAccessException {
        allUsers.add(user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for(UserData user : allUsers){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        for(UserData user : allUsers){
            if(user.username().equals(username)){
                allUsers.remove(user);
            }
        }
    }

    @Override
    public void clearUsers() throws DataAccessException {
        allUsers.clear();
    }
}
