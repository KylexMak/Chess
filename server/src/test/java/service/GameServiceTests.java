package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GameServiceTests {
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();

    public GameServiceTests() throws ResponseException, DataAccessException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException, ResponseException {
        AuthData user = userService.register(new UserData("test","testPassword","test@test.com"));
        gameService.createGame(user.authToken(), "testGame");
    }

    @AfterEach
    public void cleanUp(){
        userService.clearUsers();
        gameService.clearGames();
    }

    @Test
    public void createGame() throws DataAccessException, ResponseException{
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        GameId gameId = gameService.createGame(userLogin.authToken(), "firstGame");
        GameData retrieveGame = gameService.getGame(gameId.gameID());
        Assertions.assertNotNull(gameService.getGame(gameId.gameID()));
        Assertions.assertEquals(gameId.gameID(), retrieveGame.gameID());
    }

    @Test
    public void createGameInvalidAuthToken() throws DataAccessException, ResponseException{
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame("InvalidToken", "testGame1"));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void createGameInvalidData() throws DataAccessException, ResponseException{
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        String gameName = "";
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(userLogin.authToken(), null));
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    public void get() throws DataAccessException, ResponseException{
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        GameId gameId = gameService.createGame(userLogin.authToken(), "Test3");
        GameData game = gameService.getGame(gameId.gameID());
        GameData newGame = new GameData(gameId.gameID(), null, null, "Test3", new ChessGame());
        Assertions.assertEquals(game, newGame);
    }

    @Test
    public void getNoGame(){
        GameData game = gameService.getGame(156);
        Assertions.assertNull(game);
    }

    @Test
    public void joinGame() throws DataAccessException, ResponseException {
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        GameId gameId = gameService.createGame(userLogin.authToken(), "Test3");
        gameService.joinGame(gameId.gameID(), userLogin.authToken(), "WHITE");
        GameData retrieveGame = gameService.getGame(gameId.gameID());
        Assertions.assertNotNull(retrieveGame.whiteUsername());
    }

    @Test
    public void joinGameInvalidAuth() throws DataAccessException, ResponseException{
        UserData user = userService.getByUsername("test");
        AuthData userLogin = userService.login(user);
        GameId gameId = gameService.createGame(userLogin.authToken(), "Test4");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,
                () -> gameService.joinGame(gameId.gameID(), "invalidToken", "BLACK"));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void joinGameAlreadyTaken() throws DataAccessException, ResponseException{
        UserData user = userService.getByUsername("test");
        AuthData newUser = userService.register(new UserData("taco", "password", "taco@taco.com"));
        AuthData userLogin = userService.login(user);
        GameId gameId = gameService.createGame(userLogin.authToken(), "Test4");
        gameService.joinGame(gameId.gameID(), newUser.authToken(), "WHITE");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,
                () -> gameService.joinGame(gameId.gameID(), userLogin.authToken(),"WHITE"));
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    public void listGames() throws DataAccessException, ResponseException{
        UserData user = userService.getByUsername("test");
        AuthData newUser = userService.register(new UserData("taco", "password", "taco@taco.com"));
        AuthData userLogin = userService.login(user);
        gameService.clearGames();
        GameId gameId = gameService.createGame(userLogin.authToken(), "Test4");
        List<GameData> original = new ArrayList<>();
        original.add(gameService.getGame(gameId.gameID()));
        ListGames origin = new ListGames(original);
        ListGames get = gameService.listGames(userLogin.authToken());
        Assertions.assertEquals(origin, get);
    }

    @Test
    public void listGamesInvalidAuth(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames("InvalidToken"));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void updateGameNoGame(){
        GameData fakeGame = new GameData(1, null, null, "poke", new ChessGame());
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> gameService.updateGame(fakeGame));
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }
}
