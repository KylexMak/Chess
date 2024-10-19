package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.ListGames;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ListGamesHandler implements Route {
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        String authToken = request.headers("authorization");
        try{
            ListGames games = gameService.listGames(authToken);
            return serializer.toJson(games);
        }
        catch (DataAccessException dataAccessException){
            String errorMessage = dataAccessException.toString();
            if(errorMessage.contains("unauthorized")){
                response.status(401);
            }
            return serializer.toJson(errorMessage);
        }
    }
}
