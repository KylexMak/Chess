package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        String authToken = request.headers("authorization");
        String gameName = serializer.fromJson(request.body(), String.class);
        try{
            int gameId = gameService.createGame(authToken, gameName);
            return serializer.toJson(gameId);
        }
        catch (DataAccessException dataAccessException){
            String errorMessage = dataAccessException.toString();
            if(errorMessage.contains("unauthorized")){
                response.status(401);
            }
            if(errorMessage.contains("no user")){
                response.status(500);
            }
            return serializer.toJson(errorMessage);
        }
    }
}
