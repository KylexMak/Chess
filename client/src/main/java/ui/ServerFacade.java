package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class ServerFacade {
    public final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(UserData user) throws IOException{
        try{
            var path = "/user";
            return makeRequest("POST", path, user, AuthData.class, null);
        }
        catch (IOException e){
            throw new IOException("Could not register");
        }
    }

    public AuthData login(Login user) throws IOException{
        try{
            var path = "/session";
            return makeRequest("POST", path, user, AuthData.class, null);
        }
        catch (IOException e){
            throw new IOException("Could not login");
        }
    }

    public GameId createGame(AuthData authToken, GameData game) throws IOException{
        try{
            var path = "/game";
            return makeRequest("POST", path, game, GameId.class, authToken);
        }
        catch (IOException e){
            throw new IOException("Could not create game");
        }
    }

    public void joinGame(AuthData authToken, JoinGameRequest player) throws IOException{
        try{
            var path = "/game";
            makeRequest("PUT", path, player, GameId.class, authToken);
        }
        catch (IOException e){
            throw new IOException("Could not join game");
        }
    }

    public ListGames listGames(AuthData authToken) throws IOException{
        try{
            var path = "/game";
            return makeRequest("GET", path, null, ListGames.class, authToken);
        }
        catch (IOException e){
            throw new IOException("Could not list games");
        }
    }

    public void logout(AuthData authToken) throws IOException{
        try{
            var path = "/session";
            makeRequest("DELETE", path, null, String.class, authToken);
        }
        catch (IOException e){
            throw new IOException("Could not logout");
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthData authToken) throws IOException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http, authToken);
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http, AuthData authToken) throws IOException {
        if (request != null) {
            if(authToken == null){
                http.addRequestProperty("Content-Type", "application/json");
            }
            else{
                http.addRequestProperty("Authorization", authToken.authToken());
            }
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        String returnCode = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                    if(response.getClass() == String.class){
                        returnCode = String.valueOf(http.getResponseCode());
                    }
                }

            }
        }
        if(returnCode != null){
            return (T) returnCode;
        }
        return response;
    }
}
