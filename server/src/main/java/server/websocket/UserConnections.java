package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class UserConnections {
    public String authToken;
    public String username;
    public ChessGame.TeamColor color;
    public Session session;

    public UserConnections(String authToken, String username, Session session, ChessGame.TeamColor color){
        this.authToken = authToken;
        this.username = username;
        this.color = color;
        this.session = session;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
