package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException, ResponseException{
        String[] createStatement = {
                """
                CREATE TABLE IF NOT EXISTS user (
                username varchar(256) NOT NULL,
                password varchar(256) NOT NULL,
                email varchar(256) NOT NULL,
                PRIMARY KEY (username)
                )
                """
        };
        DataConversionTool.configureDatabase(createStatement);
    }
    @Override
    public void addUser(UserData user) throws ResponseException {
        var statement = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
        String encryptedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        DataConversionTool.executeUpdate(statement, user.username(), encryptedPassword, user.email());
    }

    @Override
    public UserData getUser(String username) throws ResponseException{
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM user WHERE username = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, username);
                try(var rs = prepStatement.executeQuery()){
                    if(rs.next()){
                        String userName = rs.getString("username");
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserData(userName, password, email);
                    }
                }
            }
        }
        catch (Exception e){
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void clearUsers() throws ResponseException {
        var statement = "TRUNCATE user";
        DataConversionTool.executeUpdate(statement);
    }
}
