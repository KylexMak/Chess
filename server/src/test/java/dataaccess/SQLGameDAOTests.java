package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SQLGameDAOTests {
    private GameDAO gameDb;

    @BeforeEach
    public void setUp() throws DataAccessException, ResponseException{
        gameDb = new SQLGameDAO();
        gameDb.createGame(new GameData(1, null, null, "test1", new ChessGame()));
        gameDb.createGame(new GameData(2, null, null, "test2", new ChessGame()));
        gameDb.createGame(new GameData(3, null, null, "test3", new ChessGame()));
    }
    @AfterEach
    public void cleanUp() throws ResponseException{
        gameDb.clearGames();
    }

    @Test
    public void createGamePositive() throws ResponseException{
        gameDb.createGame(new GameData(4, null, null, "test4", new ChessGame()));
        Assertions.assertNotNull(gameDb.getGame(4));
    }

    @Test
    public void createGameNegative(){
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> gameDb.createGame(new GameData(1, null, null, "test4", new ChessGame())));
        Assertions.assertNotNull(exception);
    }

    @Test
    public void getGamePositive() throws ResponseException{
        Assertions.assertNotNull(gameDb.getGame(1));
    }

    @Test
    public void getGameNegative() throws ResponseException{
        Assertions.assertNull(gameDb.getGame(8));
    }

    @Test
    public void getAllGameIdsPositive() throws ResponseException{
        List<Integer> gameIds = gameDb.getAllGameIds();
        Assertions.assertEquals(gameIds.size(), 3);
    }

    @Test
    public void getAllGameIdsNegative() throws ResponseException{
        List<Integer> gameIds = gameDb.getAllGameIds();
        gameDb.clearGames();
        Assertions.assertNotEquals(gameIds, gameDb.getAllGameIds());
    }

    @Test
    public void listGamesPositive() throws DataAccessException, ResponseException{
        List<GameData> games = gameDb.listGames();
        Assertions.assertEquals(games.size(), 3);
    }

    @Test
    public void listGamesNegative() throws DataAccessException, ResponseException{
        List<GameData> games = gameDb.listGames();
        gameDb.clearGames();
        Assertions.assertNotEquals(games, gameDb.listGames());
    }

    @Test
    public void updateGamePositive() throws DataAccessException, ResponseException{
        gameDb.updateGame(new GameData(1, "Random", null, "Random1", new ChessGame()));
        GameData updatedGame = gameDb.getGame(1);
        Assertions.assertEquals(updatedGame.whiteUsername(), "Random");
        Assertions.assertEquals(updatedGame.gameName(), "Random1");
    }

    @Test
    public void updateGameNegative() throws DataAccessException, ResponseException{
        gameDb.updateGame(new GameData(9, "Random", null, "Random1", new ChessGame()));
        GameData updatedGame = gameDb.getGame(9);
        Assertions.assertNull(updatedGame);
    }

    @Test
    public void clearPositive() throws DataAccessException, ResponseException{
        gameDb.clearGames();
        List<GameData> empty = new ArrayList<>();
        Assertions.assertEquals(gameDb.listGames(), empty);
    }
}
