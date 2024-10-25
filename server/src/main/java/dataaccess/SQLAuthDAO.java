package dataaccess;

import exception.ResponseException;
import model.AuthData;

public class SQLAuthDAO implements AuthDao{

    public SQLAuthDAO() throws DataAccessException, ResponseException {
        String[] create = {
                """
                CREATE TABLE IF NOT EXISTS auth (
                authToken varchar(256) NOT NULL,
                username varchar(256) NOT NULL,
                PRIMARY KEY (authToken))
                """
        };
        DataConversionTool.configureDatabase(create);
    }
    @Override
    public void createAuthData(AuthData authData) throws ResponseException{
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        DataConversionTool.executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuthData(String authToken) throws ResponseException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM auth WHERE authToken = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, authToken);
                try(var rs = prepStatement.executeQuery()){
                    if(rs.next()){
                        String token = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthData(token, username);
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
    public void deleteAuthData(String authToken) throws ResponseException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        DataConversionTool.executeUpdate(statement, authToken);
    }

    @Override
    public void clearAllAuthData() throws ResponseException{
        var statement = "TRUNCATE auth";
        DataConversionTool.executeUpdate(statement);
    }
}
