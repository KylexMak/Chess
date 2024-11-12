package ui;

import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

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

    public GameId createGame(AuthData authToken, GameName gameName) throws IOException{
        try{
            var path = "/game";
            return makeRequest("POST", path, gameName, GameId.class, authToken);
        }
        catch (IOException e){
            throw new IOException("Could not create game");
        }
    }

    public GameData joinGame(AuthData authToken, JoinGameRequest player) throws IOException{
        try{
            var path = "/game";
            return makeRequest("PUT", path, player, GameData.class, authToken);
        }
        catch (IOException e){
            String error = "";
            if(e.getMessage().contains("400")){
                error = "Bad request\n";
            }
            if(e.getMessage().contains("403")){
                error = "Color already taken\n";
            }
            if(e.getMessage().contains("401")){
                error = "User unauthorized\n";
            }
            throw new IOException("Could not join game: " + error);
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
            String empty = "";
            makeRequest("DELETE", path, empty, String.class, authToken);
        }
        catch (IOException e){
            throw new IOException("Could not logout");
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthData authToken) throws IOException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            if(authToken == null){
                http.setRequestProperty("Content-Type", "application/json");
            }
            else{
                http.setRequestProperty("Authorization", authToken.authToken());
            }
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            //System.out.println(request);
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            String reqData = new Gson().toJson(request);
            //System.out.println("ReqData: " + reqData);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
                //System.out.println(reqBody.toString());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        String returnCode = null;
        //System.out.println("Entered Read body");
        if (http.getContentLength() < 0) {
            //System.out.println("http length is less than 0");
            try (InputStream respBody = http.getInputStream()) {
                //System.out.println("respbody");
                InputStreamReader reader = new InputStreamReader(respBody);
                //System.out.println(responseClass);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                    //System.out.println(response);
                    if(response != null){
                        returnCode = getResponseCode(http, response);
                    }
                    else{
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

    private static <T> String getResponseCode(HttpURLConnection http, T response) throws IOException {
        String returnCode = null;
        if(response.getClass() == String.class){
            returnCode = String.valueOf(http.getResponseCode());
        }
        return returnCode;
    }
}
