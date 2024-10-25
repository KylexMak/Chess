package server;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.JoinGameRequest;
import model.ListGames;
import spark.*;

public class Server {
    ClearHandler clear = new ClearHandler();
    RegisterHandler register = new RegisterHandler();
    LoginHandler login = new LoginHandler();
    LogoutHandler logout = new LogoutHandler();
    ListGamesHandler list = new ListGamesHandler();
    CreateGameHandler create = new CreateGameHandler();
    JoinGameHandler join = new JoinGameHandler();

    public Server() throws ResponseException, DataAccessException {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clear);
        Spark.post("/user", register);
        Spark.post("/session", login);
        Spark.delete("/session", logout);
        Spark.post("/game", create);
        Spark.put("/game", join);
        Spark.get("/game", list);
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}