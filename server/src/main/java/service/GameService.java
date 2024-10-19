package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.GameId;
import model.ListGames;

import java.util.Random;

import java.util.List;

public class GameService {
    GameDAO gameDb = new MemoryGameDAO();
    AuthService authService = new AuthService();
    Random rand = new Random();

    private static final int gameIdLimit = 1000000;

    public GameService(){

    }

    public GameId createGame(String authToken, String gameName) throws DataAccessException {
        if (authToken != null && gameName != null) {
            if (authService.getAuthData(authToken) != null) {
                GameId gameId = generateGameId();
                GameData gameData = new GameData(gameId.gameID(), null, null, gameName, new ChessGame());
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
            if(playerColor == null || gameToJoin == null){
                throw new DataAccessException("Error: bad request");
            }
            if(gameToJoin.blackUsername() != null && gameToJoin.whiteUsername() != null){
                throw new DataAccessException("Error: Game is full");
            }
            if (gameToJoin.whiteUsername() != null && playerColor.equals("WHITE")){
                throw new DataAccessException("Error: already taken");
            }
            else {
                if(playerColor.equals("WHITE")) {
                    GameData updatedGame = new GameData(gameId, authData.username(), gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game());
                    updateGame(updatedGame);
                }
            }
            if (gameToJoin.blackUsername() != null && playerColor.equals("BLACK")){
                throw new DataAccessException("Error: already taken");
            }
            else{
                if(playerColor.equals("BLACK")){
                    GameData updatedGame = new GameData(gameId, gameToJoin.whiteUsername(), authData.username(), gameToJoin.gameName(), gameToJoin.game());
                    updateGame(updatedGame);
                }
            }
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void clearGames(){
        gameDb.clearGames();
    }

    private GameId generateGameId(){
        List<Integer> existingGameIds = gameDb.getAllGameIds();
        int gameId = rand.nextInt(gameIdLimit);
        GameId game = new GameId(gameId);
        while(existingGameIds.contains(gameId)){
            gameId = rand.nextInt(gameIdLimit);
            game = new GameId(gameId);
        }
        return game;
    }
}
