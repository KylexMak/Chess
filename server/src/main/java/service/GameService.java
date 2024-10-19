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
        if (authToken != null && gameName != null) {
            if (authService.getAuthData(authToken) != null) {
                int gameId = generateGameId();
                GameData gameData = new GameData(gameId, null, null, gameName, new ChessGame());
                gameDb.createGame(gameData);
                return gameId;
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        }
        else{
            throw new DataAccessException("Error: bad request");
        }
    }

    public GameData getGame(int gameId){
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

    public void updateGame(GameData game) throws DataAccessException{
        gameDb.updateGame(game);
    }

    public void joinGame(int gameId, String authToken, String playerColor) throws DataAccessException{
        if(authService.getAuthData(authToken) != null){
            GameData gameToJoin = getGame(gameId);
            AuthData authData = authService.getAuthData(authToken);
            if(gameToJoin.blackUsername() != null && gameToJoin.whiteUsername() != null){
                throw new DataAccessException("Error: Game is full");
            }
            if(playerColor.equals("WHITE") && gameToJoin.whiteUsername() == null){
                GameData updatedGame = new GameData(gameId, authData.userName(), gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game());
                updateGame(updatedGame);
            }
            else if (gameToJoin.whiteUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
            if(playerColor.equals("BLACK") && gameToJoin.blackUsername() == null){
                GameData updatedGame = new GameData(gameId, gameToJoin.whiteUsername(), authData.userName(), gameToJoin.gameName(), gameToJoin.game());
                updateGame(updatedGame);
            }else if (gameToJoin.blackUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
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
