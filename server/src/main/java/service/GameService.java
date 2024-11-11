package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.SQLGameDAO;
import exception.ResponseException;
import model.*;

import java.util.Objects;
import java.util.Random;

import java.util.List;

public class GameService {
    GameDAO gameDb = new SQLGameDAO();
    AuthService authService = new AuthService();
    Random rand = new Random();

    private static final int GAME_ID_LIMIT = 1000000;

    public GameService() throws ResponseException, DataAccessException {

    }

    public GameId createGame(String authToken, String gameName) throws DataAccessException, ResponseException {
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

    public GameData getGame(int gameId) throws ResponseException{
        return gameDb.getGame(gameId);
    }

    public ListGames listGames(String authToken) throws DataAccessException, ResponseException{
        if(authService.getAuthData(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        else{
            return new ListGames(gameDb.listGames());
        }
    }

    public void updateGame(GameData game) throws DataAccessException, ResponseException{
        gameDb.updateGame(game);
    }

    public GameData joinGame(String authToken, JoinGameRequest player) throws DataAccessException, ResponseException{
        GameData gameToJoin = getGame(player.gameID());
        if(authService.getAuthData(authToken) != null && player.playerColor() != null){
            GameData updatedGame = null;
            AuthData authData = authService.getAuthData(authToken);
            if(gameToJoin == null){
                throw new DataAccessException("Error: bad request");
            }
            if(gameToJoin.blackUsername() != null && gameToJoin.whiteUsername() != null){
                throw new DataAccessException("Error: Game is full");
            }
            if (gameToJoin.whiteUsername() != null && player.playerColor().equalsIgnoreCase("WHITE")){
                throw new DataAccessException("Error: already taken");
            }
            if((Objects.equals(gameToJoin.whiteUsername(), authData.username())) || (Objects.equals(gameToJoin.blackUsername(), authData.username()))){
                throw new DataAccessException(("Error: you cannot join twice"));
            }
            else {
                if (player.playerColor() != null){
                    if(player.playerColor().equalsIgnoreCase("WHITE")) {
                        updatedGame = new GameData(player.gameID(), authData.username(),
                                gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game());
                        updateGame(updatedGame);
                    }
                }
            }
            if (gameToJoin.blackUsername() != null && player.playerColor().equalsIgnoreCase("BLACK")){
                throw new DataAccessException("Error: already taken");
            }
            else{
                if (player.playerColor() != null){
                    if(player.playerColor().equalsIgnoreCase("BLACK")){
                        updatedGame = new GameData(player.gameID(), gameToJoin.whiteUsername(),
                                authData.username(), gameToJoin.gameName(), gameToJoin.game());
                        updateGame(updatedGame);
                    }
                }
            }
            return updatedGame;
        }
        else if(player.playerColor() == null){
            return gameToJoin;
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void clearGames() throws ResponseException{
        gameDb.clearGames();
    }

    private GameId generateGameId() throws ResponseException{
        List<Integer> existingGameIds = gameDb.getAllGameIds();
        int gameId = rand.nextInt(GAME_ID_LIMIT);
        GameId game = new GameId(gameId);
        while(existingGameIds.contains(gameId)){
            gameId = rand.nextInt(GAME_ID_LIMIT);
            game = new GameId(gameId);
        }
        return game;
    }
}
