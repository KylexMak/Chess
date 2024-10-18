package server;

import org.eclipse.jetty.server.Authentication;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    AuthService authService = new AuthService();
    UserService userService = new UserService();
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        gameService.clearGames();
        authService.clearAllAuthData();
        userService.clearUsers();
        return null;
    }
}
