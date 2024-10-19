package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        String authToken = request.headers("authorization");
        JoinGameRequest person = serializer.fromJson(request.body(), JoinGameRequest.class);
        try{
            gameService.joinGame(person.gameID(), authToken, person.playerColor());
            return "";
        }
        catch(DataAccessException dataAccessException){

        }
        return null;
    }
}
