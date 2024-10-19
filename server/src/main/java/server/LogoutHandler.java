package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    UserService userService = new UserService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        String authToken = request.headers("authorization");
        try{
            userService.logout(authToken);
            return "";
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
