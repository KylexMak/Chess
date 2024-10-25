package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.util.List;

public interface GameDAO {
    void createGame(GameData game) throws ResponseException;
    GameData getGame(int gameID) throws ResponseException;
    List<Integer> getAllGameIds() throws ResponseException;
    List<GameData> listGames() throws DataAccessException, ResponseException;
    void updateGame(GameData game) throws DataAccessException, ResponseException;
    void clearGames() throws ResponseException;
}
