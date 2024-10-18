package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<Integer> getAllGameIds();
    List<GameData> listGames() throws DataAccessException;
    void updateGame(int gameID, GameData game) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    void clearGames();
}
