package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.ErrorMessage;
import model.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    GameService gameService = new GameService();

    public JoinGameHandler() throws ResponseException, DataAccessException {

    }

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
            String errorMessage = dataAccessException.getMessage();
            if(errorMessage.contains("bad request")){
                response.status(400);
            }
            if(errorMessage.contains("already taken")){
                response.status(403);
            }
            if(errorMessage.contains("full")){
                response.status(500);
            }
            if(errorMessage.contains("unauthorized")){
                response.status(401);
            }
            ErrorMessage message = new ErrorMessage(errorMessage);
            return serializer.toJson(message);
        }
    }
}
