package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, GameConnections> connections = new ConcurrentHashMap<>();

    public void addGame(int gameId){
        if(connections.get(gameId) == null)
        {
            GameConnections connect = new GameConnections(gameId);
            connections.put(gameId, connect);
        }
    }

    public GameConnections getGame(int gameId){
        if(connections.get(gameId) != null){
            return connections.get(gameId);
        }
        return null;
    }

    public void addPlayer(int gamedId, String authToken, String username, ChessGame.TeamColor color, Session session){
        GameConnections game = connections.get(gamedId);
        assert(game != null);
        game.addPlayer(authToken, username, color, session);
    }

    public UserConnections getPlayer(int gameId, String authToken){
        GameConnections game = connections.get(gameId);
        return game.getPlayer(authToken);
    }

    public void removePlayer(int gameId, String authToken){
        GameConnections game = connections.get(gameId);
        game.removePlayer(authToken);
    }

    public void sendToConnection(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcastMessage(int gameId, String authToken, String message) throws IOException {
        GameConnections game = connections.get(gameId);
        UserConnections whitePlayer = game.whitePlayer;
        UserConnections blackPlayer = game.blackPlayer;

        if(whitePlayer != null && !Objects.equals(whitePlayer.authToken, authToken)){
            if(whitePlayer.session.isOpen()){
                whitePlayer.send(message);
            }
        }
        if(blackPlayer != null && !Objects.equals(blackPlayer.authToken, authToken)){
            if(blackPlayer.session.isOpen()){
                blackPlayer.send(message);
            }
        }
        for(UserConnections watcher : game.watchers.values()){
            if(watcher.session.isOpen()){
                if(!Objects.equals(watcher.authToken, authToken)){
                    watcher.send(message);
                }
            }
        }
    }
}
