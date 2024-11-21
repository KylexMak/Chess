package server.websocket;

import chess.ChessGame;

import javax.websocket.Session;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnections {
    public final ConcurrentHashMap<String, UserConnections> watchers = new ConcurrentHashMap<>();
    public UserConnections whitePlayer;
    public UserConnections blackPlayer;
    public int gameId;

    public GameConnections(int gameId){
        this.gameId = gameId;
    }

    public void addPlayer(String authToken, String username, ChessGame.TeamColor color, Session session){
        UserConnections player = new UserConnections(authToken, username, session, color);

        switch (color){
            case null -> {
                watchers.put(authToken, player);
            }
            case WHITE -> {
                if(whitePlayer == null){
                    whitePlayer = player;
                }
            }
            case BLACK -> {
                if(blackPlayer == null){
                    blackPlayer = player;
                }
            }
        }
    }

    public UserConnections getPlayer(String authToken){
        if(isWhitePlayer(authToken)){
            return whitePlayer;
        }
        else if (isBlackPlayer(authToken)) {
            return blackPlayer;
        }
        else{
            return watchers.get(authToken);
        }
    }
    public void removePlayer(String authToken){
        if(isWhitePlayer(authToken)){
            whitePlayer = null;
        }
        else if (isBlackPlayer(authToken)) {
            blackPlayer = null;
        }
        else {
            watchers.remove(authToken);
        }
    }

    private boolean isWhitePlayer(String authToken){
        if(whitePlayer != null && Objects.equals(whitePlayer.authToken, authToken)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isBlackPlayer(String authToken){
        if(blackPlayer != null && Objects.equals(blackPlayer.authToken, authToken)){
            return true;
        }
        else{
            return false;
        }
    }
}
