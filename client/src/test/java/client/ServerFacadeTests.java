package client;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade func;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + port;
        func = new ServerFacade(url);
        try{
            URL otherurl = (new URI("http://localhost:" + port + "/db")).toURL();
            HttpURLConnection http = (HttpURLConnection) otherurl.openConnection();
            http.setRequestMethod("DELETE");
            http.setDoOutput(true);
            http.connect();
            if (http.getResponseCode() == 200) {
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
        System.out.println("Server stopped");
    }

    @AfterEach
    public void clearDB() {
        try{
            URL url = (new URI("http://localhost:" + port + "/db")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("DELETE");
            http.setDoOutput(true);
            http.connect();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup(){
        clearDB();
    }

    @Test
    public void registerTestPositive() {
        AuthData auth;
        try {
            auth = func.register(new UserData("testing", "testing", "testEmail"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(auth);
    }

    @Test
    public void registerTestNegative() {
        AuthData auth;
        try {
            func.register(new UserData("user", "pass", "email"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IOException thrown = Assertions.assertThrows(IOException.class, () -> func.register(new UserData("user", "pass", "email")));
        Assertions.assertEquals("Could not register", thrown.getMessage());
    }

    @Test
    public void loginTestPositive() {
        AuthData auth;
        try {
            func.register(new UserData("hi", "hi", "testEmail"));
            auth = func.login(new Login("hi", "hi"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(auth);
    }

    @Test
    public void loginTestNegative() {
        IOException thrown = Assertions.assertThrows(IOException.class, () -> func.login(new Login("user", "pass")));
        Assertions.assertEquals("Could not login", thrown.getMessage());
    }

    @Test
    public void createGameTestPositive() {
        GameId id;
        try {
            AuthData auth = func.register(new UserData("testly", "testly", "testEmail"));
            id = func.createGame(auth, new GameName("testGame"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(id);
    }
    @Test
    public void createGameNegativeTest() {
        AuthData fakeAuth = new AuthData("user", "auth");
        IOException thrown = Assertions.assertThrows(IOException.class, () -> func.createGame(fakeAuth, new GameName("gameName")));
        Assertions.assertEquals("Could not create game", thrown.getMessage());
    }

    @Test
    public void joinGamePositiveTest() {
        GameId id;
        ListGames list;
        try {
            AuthData auth = func.register(new UserData("faker", "faker", "testEmail"));
            id = func.createGame(auth, new GameName("testGame"));
            func.joinGame(auth, new JoinGameRequest("WHITE", id.gameID()));
            list = func.listGames(auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(list.games().contains(new GameData(id.gameID(), "faker", null, "testGame", new ChessGame())));
    }

    @Test
    public void joinGameNegativeTest() {
        AuthData fakeAuth = new AuthData("user", "auth");
        IOException thrown = Assertions.assertThrows(IOException.class, () -> func.joinGame(fakeAuth, new JoinGameRequest("WHITE", 123890)));
        Assertions.assertEquals("Could not join game", thrown.getMessage());
    }

    @Test
    public void listGamesPositiveTest() {
        GameId id;
        ListGames list;
        try {
            AuthData auth = func.register(new UserData("fake", "fake", "testEmail"));
            id = func.createGame(auth, new GameName("testGame"));
            func.joinGame(auth, new JoinGameRequest("WHITE", id.gameID()));
            list = func.listGames(auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(list);
    }

    @Test
    public void listGamesNegativeTest() {
        AuthData fakeAuth = new AuthData("user", "auth");
        IOException thrown = Assertions.assertThrows(IOException.class, () -> func.listGames(fakeAuth));
        Assertions.assertEquals("Could not list games", thrown.getMessage());
    }

    @Test
    public void logoutUserPositiveTest() {
        AuthData auth;
        try {
            auth = func.register(new UserData("testUser", "testPassword", "testEmail"));
            func.logout(auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IOException thrown = Assertions.assertThrows(IOException.class, () -> func.listGames(auth));
        Assertions.assertEquals("Could not list games", thrown.getMessage());
    }

    @Test
    public void logoutUserNegativeTest() {
        AuthData auth;
        GameId id;
        try {
            auth = func.register(new UserData("test", "test", "testEmail"));
            id = func.createGame(auth, new GameName("testGame"));
            func.logout(auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IOException thrown = Assertions.assertThrows(IOException.class, () -> func.joinGame(auth, new JoinGameRequest("WHITE", id.gameID())));
        Assertions.assertEquals("Could not join game", thrown.getMessage());
    }
}