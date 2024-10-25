package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.ErrorMessage;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    UserService userService = new UserService();

    public LogoutHandler() throws ResponseException, DataAccessException {

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        String authToken = request.headers("authorization");
        try{
            userService.logout(authToken);
            return "";
        }
        catch(DataAccessException exception){
            String errorMessage = exception.getMessage();
            if(errorMessage.contains("unauthorized")){
                response.status(401);
            }
            ErrorMessage message = new ErrorMessage(errorMessage);
            return serializer.toJson(message);
        }
    }
}
