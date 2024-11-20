package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import ui.BoardDrawer;
import websocket.commands.*;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketClient extends Endpoint {
    public Session session;
    public NotificationHandler notificationHandler;

    public void connectGame(AuthData authToken, Integer gameId, ChessGame.TeamColor color) throws Exception {
        Connect player = new Connect(authToken.authToken(), gameId, color);
        var message = new Gson().toJson(player);
        send(message);
    }

    public void leaveGame(AuthData authToken, Integer gameId) throws Exception{
        Leave exit = new Leave(authToken.authToken(), gameId);
        var message = new Gson().toJson(exit);
        send(message);
    }

    public void makeMove(AuthData authToken, Integer gameId, ChessMove move) throws Exception{
        MakeMove nextMove = new MakeMove(authToken.authToken(), gameId, move);
        var message = new Gson().toJson(nextMove);
        send(message);
    }

    public void resignGame(AuthData authToken, Integer gameId) throws Exception{
        Resign lose = new Resign(authToken.authToken(), gameId);
        var message = new Gson().toJson(lose);
        send(message);
    }

    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }

    public WebsocketClient(String url, String authToken, Integer gameId, NotificationHandler notificationHandler) {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>(){

                @Override
                public void onMessage(String s) {
                    ServerMessage serverMessage = new Gson().fromJson(s, ServerMessage.class);
                    var type = serverMessage.getServerMessageType();
                    switch (type){
                        case LOAD_GAME -> {
                            LoadGame game = new Gson().fromJson(s, LoadGame.class);
                            ChessGame.TeamColor color = game.getTeamColor();
                            BoardDrawer.printBoard(game.getGame().getBoard(), String.valueOf(color));
                        }
                        case NOTIFICATION -> {
                            Notification notification = new Gson().fromJson(s, Notification.class);
                            System.out.println(notification.getNotificationMessage());
                        }
                        case ERROR -> {
                            Error error = new Gson().fromJson(s, Error.class);
                            System.out.println(error.getErrorMessage());
                        }
                    }
                }
            });
        }
        catch (DeploymentException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
