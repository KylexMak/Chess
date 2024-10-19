package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.ErrorMessage;
import model.GameData;
import model.ListGames;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ListGamesHandler implements Route {
    GameService gameService = new GameService();

    public ListGamesHandler(){

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        String authToken = request.headers("authorization");
        try{
            ListGames games = gameService.listGames(authToken);
            return serializer.toJson(games);
        }
        catch (DataAccessException dataAccessException){
            String errorMessage = dataAccessException.getMessage();
            if(errorMessage.contains("unauthorized")){
                response.status(401);
            }
            ErrorMessage message = new ErrorMessage(errorMessage);
            return serializer.toJson(message);
        }
    }
}
