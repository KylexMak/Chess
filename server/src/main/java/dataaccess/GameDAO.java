package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    public void createGame(GameData game) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public List<GameData> listGames() throws DataAccessException;
    public void updateGame(int gameID, GameData game) throws DataAccessException;
    public void deleteGame(int gameID) throws DataAccessException;
    public void clearGames() throws DataAccessException;
}
