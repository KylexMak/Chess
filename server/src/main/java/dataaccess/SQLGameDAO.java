package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import service.GameService;

import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException, ResponseException {
        String[] createStatement = {
                """
                CREATE TABLE IF NOT EXISTS game(
                gameID int NOT NULL,
                whiteUsername varchar(256),
                blackUsername varchar(256),
                gameName varchar(256),
                json TEXT DEFAULT NULL,
                PRIMARY KEY (gameID)
                )
                """
        };
        DataConversionTool.configureDatabase(createStatement);
    }
    @Override
    public void createGame(GameData game) throws ResponseException{
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?, ?)";
        var gameJson = new Gson().toJson(game);
        DataConversionTool.executeUpdate(statement, game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), gameJson);
    }

    @Override
    public GameData getGame(int gameID) throws ResponseException{
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM game WHERE gameID = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setInt(1, gameID);
                try(var rs = prepStatement.executeQuery()){
                    if(rs.next()){
                        int gameId = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String gameJson = rs.getString("json");
                        GameData game = new Gson().fromJson(gameJson, GameData.class);
                        return game;
                    }
                }
            }
        }
        catch (Exception e){
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public List<Integer> getAllGameIds() throws ResponseException{
        List<Integer> gameIds = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID FROM game";
            try(var prepStatement = conn.prepareStatement(statement)){
                try(var rs = prepStatement.executeQuery()){
                    while(rs.next()){
                        gameIds.add(rs.getInt("gameID"));
                    }
                }
            }
        }
        catch (Exception e){
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return gameIds;
    }

    @Override
    public List<GameData> listGames() throws ResponseException {
        List<GameData> games = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT json FROM game";
            try(var prepStatement = conn.prepareStatement(statement)){
                try(var rs = prepStatement.executeQuery()){
                    while(rs.next()){
                        var gameJson = rs.getString("json");
                        GameData game = new Gson().fromJson(gameJson, GameData.class);
                        games.add(game);
                    }
                }
            }
        }
        catch (Exception e){
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public void updateGame(GameData game) throws ResponseException {
        var statement = "UPDATE game SET gameID = ?, whiteUsername = ?, blackUsername = ?, gameName = ?, json = ? WHERE gameID = ?";
        DataConversionTool.executeUpdate(statement, game.gameID(), game.whiteUsername(), game.blackUsername(),
                game.gameName(), new Gson().toJson(game), game.gameID());
    }

    @Override
    public void clearGames() throws ResponseException{
        var statement = "TRUNCATE game";
        DataConversionTool.executeUpdate(statement);
    }
}
