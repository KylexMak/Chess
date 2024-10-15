package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    GameDAO gameDb = new MemoryGameDAO();
    Random rand = new Random();

    private static final int gameIdLimit = 1000;

    public int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        int gameId = generateGameId();
        GameData gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
        gameDb.createGame(gameData);
        return gameId;
    }

    public GameData getGame(int gameId) throws DataAccessException{
        return gameDb.getGame(gameId);
    }

    public List<GameData> listGames() throws DataAccessException{
        return gameDb.listGames();
    }

    public void updateGame(int gameId, ChessGame game) throws DataAccessException{
        GameData gameData = getGame(gameId);
        GameData gameUpdated = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        gameDb.updateGame(gameId, gameUpdated);
    }

    public void deleteGame(int gameId) throws DataAccessException{
        gameDb.deleteGame(gameId);
    }

    public void clearGames() throws DataAccessException{
        gameDb.clearGames();
    }

    private int generateGameId(){
        List<Integer> existingGameIds = gameDb.getAllGameIds();
        int gameId = rand.nextInt(gameIdLimit);
        while(existingGameIds.contains(gameId)){
            gameId = rand.nextInt(gameIdLimit);
        }
        return gameId;
    }
}
