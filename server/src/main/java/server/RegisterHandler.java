package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.ErrorMessage;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    UserService userService = new UserService();

    public RegisterHandler() throws ResponseException, DataAccessException {

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        UserData user = serializer.fromJson(request.body(), UserData.class);
        try{
            AuthData registered = userService.register(user);
            return serializer.toJson(registered);
        }
        catch (DataAccessException exception){
            String errorMessage = exception.getMessage();
            if(errorMessage.contains("already taken")){
                response.status(403);
            }
            if(errorMessage.contains("bad request")){
                response.status(400);
            }
            ErrorMessage message = new ErrorMessage(errorMessage);
            return serializer.toJson(message);
        }
    }
}
