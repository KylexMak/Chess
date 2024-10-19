package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.ListGames;

import java.util.Random;

import java.util.List;

public class GameService {
    GameDAO gameDb = new MemoryGameDAO();
    AuthService authService = new AuthService();
    Random rand = new Random();

    private static final int gameIdLimit = 1000000;

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if(authService.getAuthData(authToken) != null){
            int gameId = generateGameId();
            GameData gameData = new GameData(gameId, null, null, gameName, new ChessGame());
            gameDb.createGame(gameData);
            return gameId;
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public GameData getGame(int gameId) throws DataAccessException{
        return gameDb.getGame(gameId);
    }

    public ListGames listGames(String authToken) throws DataAccessException{
        if(authService.getAuthData(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        else{
            return new ListGames(gameDb.listGames());
        }
    }

    public void updateGame(int gameId, ChessGame game) throws DataAccessException{
        GameData gameData = getGame(gameId);
        GameData gameUpdated = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        gameDb.updateGame(gameId, gameUpdated);
    }

    public void joinGame(int gameId, String authToken, String playerColor) throws DataAccessException{
        if(authService.getAuthData(authToken) != null){
            GameData gameToJoin = getGame(gameId);
            AuthData authData = authService.getAuthData(authToken);
            if(playerColor.equals("WHITE") && gameToJoin.whiteUsername() == null){
                GameData updatedGame = new GameData(gameId, authData.userName(), gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game());
                updateGame(updatedGame.gameID(), updatedGame.game());
            }
            else{
                throw new DataAccessException("Error: already taken");
            }
            if(playerColor.equals("BLACK") && gameToJoin.blackUsername() == null){
                GameData updatedGame = new GameData(gameId, gameToJoin.whiteUsername(), authData.userName(), gameToJoin.gameName(), gameToJoin.game());
                updateGame(updatedGame.gameID(), updatedGame.game());
            }else{
                throw new DataAccessException("Error: already taken");
            }
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void deleteGame(int gameId) throws DataAccessException{
        gameDb.deleteGame(gameId);
    }

    public void clearGames(){
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
