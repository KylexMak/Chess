package dataaccess;

import exception.ResponseException;
import model.AuthData;

import javax.xml.crypto.Data;

public class SQLAuthDAO implements AuthDao{

    public SQLAuthDAO() throws DataAccessException, ResponseException {
        String[] create = {

        };
        DataConversionTool.configureDatabase(create);
    }
    @Override
    public void createAuthData(AuthData authData) {

    }

    @Override
    public AuthData getAuthData(String authToken) {
        return null;
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {

    }

    @Override
    public void clearAllAuthData() {

    }
}
