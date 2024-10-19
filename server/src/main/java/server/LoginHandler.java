package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.Login;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    UserService userService = new UserService();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        Login user = serializer.fromJson(request.body(), Login.class);
        UserData conversion = new UserData(user.username(), user.password(), null);
        try{
            AuthData userLoggedIn = userService.login(conversion);
            return serializer.toJson(userLoggedIn);
        }
        catch(DataAccessException exception){
            String errorMessage = exception.toString();
            if(errorMessage.contains("unauthorized")){
                response.status(401);
            }
            return serializer.toJson(errorMessage);
        }

    }
}
