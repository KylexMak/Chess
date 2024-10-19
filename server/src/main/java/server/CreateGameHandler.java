package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.ErrorMessage;
import model.GameId;
import model.GameName;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    GameService gameService = new GameService();

    public CreateGameHandler(){

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        String authToken = request.headers("authorization");
        GameName gameName = serializer.fromJson(request.body(), GameName.class);
        try{
            GameId gameId = gameService.createGame(authToken, gameName.gameName());
            return serializer.toJson(gameId);
        }
        catch (DataAccessException dataAccessException){
            String errorMessage = dataAccessException.getMessage();
            if(errorMessage.contains("bad request")){
                response.status(400);
            }
            if(errorMessage.contains("unauthorized")){
                response.status(401);
            }
            if(errorMessage.contains("no user")){
                response.status(500);
            }
            ErrorMessage message = new ErrorMessage(errorMessage);
            return serializer.toJson(message);
        }
    }
}
