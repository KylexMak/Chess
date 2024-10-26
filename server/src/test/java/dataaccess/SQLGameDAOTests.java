package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> gameDb.createGame(new GameData(1, null, null, "test4", new ChessGame())));
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


}
