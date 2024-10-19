package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.ErrorMessage;
import model.Login;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    UserService userService = new UserService();

    public LoginHandler(){

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        UserData user = serializer.fromJson(request.body(), UserData.class);
        try{
            AuthData userLoggedIn = userService.login(user);
            return serializer.toJson(userLoggedIn);
        }
        catch(DataAccessException exception){
            if(exception.getMessage().contains("unauthorized")){
                response.status(401);
            }
            ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
            return serializer.toJson(errorMessage);
        }

    }
}
