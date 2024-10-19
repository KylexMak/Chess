package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void createGame(GameData game);
    GameData getGame(int gameID);
    List<Integer> getAllGameIds();
    List<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    void clearGames();
}
