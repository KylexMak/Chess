package server;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.JoinGameRequest;
import model.ListGames;
import spark.*;

public class Server {
    ClearHandler clear;
    RegisterHandler register;
    LoginHandler login;
    LogoutHandler logout;
    ListGamesHandler list;
    CreateGameHandler create;
    JoinGameHandler join;
    {
        try{
            clear = new ClearHandler();
            register = new RegisterHandler();
            login = new LoginHandler();
            logout = new LogoutHandler();
            list = new ListGamesHandler();
            create = new CreateGameHandler();
            join = new JoinGameHandler();
        }
        catch (ResponseException | DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public Server(){
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