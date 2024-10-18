package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO{
    public static List<UserData> allUsers = new ArrayList<>();
    @Override
    public void addUser(UserData user) {
        allUsers.add(user);
    }

    @Override
    public UserData getUser(String username){
        for(UserData user : allUsers){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        UserData user = getUser(username);
        if(user == null){
            throw new DataAccessException("Error: User does not exist");
        }
        allUsers.remove(user);
    }

    @Override
    public void clearUsers() {
        allUsers.clear();
    }
}
