package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.commands.*;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@WebSocket
public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    GameService gameService = new GameService();
    AuthService authService = new AuthService();

    public WebsocketHandler() throws ResponseException, DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand input = new Gson().fromJson(message, UserGameCommand.class);
        switch(input.getCommandType()){
            case CONNECT -> {
                Connect player = new Gson().fromJson(message, Connect.class);
                connectPlayer(player, session);
            }
            case MAKE_MOVE -> {
                MakeMove move = new Gson().fromJson(message, MakeMove.class);
                makeMove(move, session);
            }
            case LEAVE -> {
                Leave player = new Gson().fromJson(message, Leave.class);
                leaveGame(player, session);
            }
            case RESIGN -> {
                Resign player = new Gson().fromJson(message, Resign.class);
                resign(player, session);
            }
        }
    }

    private void connectPlayer(Connect player, Session session){
        try{
            String authToken = player.getAuthToken();
            int gameId = player.getGameID();
            ChessGame.TeamColor color = player.getTeamColor();
            AuthData user = authService.getAuthData(authToken);
            if(user == null){
                throw new Exception("Error: Unauthorized");
            }
            GameData game = gameService.getGame(gameId);
            if(game == null){
                throw new Exception("Error: Game does not exist");
            }
            if(verifyPlayer(game, color, user.username())){
                connections.addGame(gameId);
                connections.addPlayer(gameId, authToken, user.username(), color, session);
                LoadGame loadedGame = new LoadGame(game.game(), color);
                var stringLoadGame = new Gson().toJson(loadedGame);
                connections.sendToConnection(session, stringLoadGame);
                Notification notification;
                if(color != null){
                    notification = new Notification("Notification: " + user.username() +
                            " has joined the game as " + color);
                }
                else{
                    notification = new Notification("Notification: " + user.username() +
                            " has joined the game as an observer");
                }
                String stringNotification = new Gson().toJson(notification);
                connections.broadcastMessage(gameId, authToken, stringNotification);
            }
            else{
                throw new Exception("Error: cannot steal another user's spot");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void makeMove(MakeMove move, Session session) throws IOException {
        try{
            String authToken = move.getAuthToken();
            int gameId = move.getGameID();
            ChessMove desiredMove = move.getChessMove();
            UserConnections user = connections.getPlayer(gameId, authToken);
            ChessGame game = gameService.getGame(gameId).game();

            if(user.color == null){
                throw new Exception("Error: You cannot move as an observer");
            }
            if(user.color != game.getTeamTurn()){
                throw new Exception("Error: It is not your turn");
            }
            if(game.isInCheckmate(user.color) || game.isInStalemate(user.color)){
                throw new Exception("Error: The game has ended. No more moves can be made.");
            }

            ChessPosition start = desiredMove.getStartPosition();
            ChessPosition end = desiredMove.getEndPosition();
            ChessBoard board = game.getBoard();
            ChessPiece piece = board.getPiece(start);

            if(piece.getTeamColor() != user.color){
                throw new Exception("Error: This is not your piece");
            }

            Collection<ChessMove> validMoves = game.validMoves(start);
            if(!validMoves.contains(desiredMove)){
                throw new Exception("Error: That is not a valid move");
            }

            game.makeMove(desiredMove);

            LoadGame updatedGame = new LoadGame(game, user.color);
            String stringUpdatedGame = new Gson().toJson(updatedGame);
            connections.sendToConnection(session, stringUpdatedGame);
            connections.broadcastMessage(gameId, authToken, stringUpdatedGame);

            String opposingColor = Objects.equals(user.color.toString(), "WHITE") ? "white" : "black";

            String moveNotification = user.username + " moved " + piece + " from " + start +
                    " to " + end;
            Notification notification = new Notification(moveNotification);
            String stringNotification = new Gson().toJson(notification);
            connections.broadcastMessage(gameId, authToken, stringNotification);

            if(game.isInCheck(game.getTeamTurn())){
                String check = opposingColor + " is in check";
                Notification checkNotification = new Notification(check);
                String stringCheckNotification = new Gson().toJson(checkNotification);
                connections.broadcastMessage(gameId, authToken, stringCheckNotification);
            }
            else{
                String endGame = "";
                if(game.isInCheckmate(game.getTeamTurn())){
                    endGame = "checkmate";
                }
                else if(game.isInStalemate(game.getTeamTurn())){
                    endGame = "stalemate";
                }
                if(!endGame.isEmpty()){
                    String gameOver = opposingColor + "is in " + endGame + ". Game Over";
                    Notification gameOverNotification = new Notification(gameOver);
                    String stringGameOver = new Gson().toJson(gameOverNotification);
                    connections.broadcastMessage(gameId, authToken, stringGameOver);
                }
            }

            GameData gameAfterMove = gameService.getGame(gameId);
            GameData sendToUpdate = new GameData(gameAfterMove.gameID(), gameAfterMove.whiteUsername(),
                    gameAfterMove.blackUsername(), gameAfterMove.gameName(), game);
            gameService.updateGame(sendToUpdate);
        } catch (Exception e) {
            Error message = new Error(e.getMessage());
            String error = new Gson().toJson(message);
            connections.sendToConnection(session, error);
        }
    }

    public void leaveGame(Leave player, Session session) throws IOException {
        try{
            AuthData userInfo = authService.getAuthData(player.getAuthToken());
            String authToken = userInfo.authToken();
            int gameId = player.getGameID();
            String username = userInfo.username();
            UserConnections user = connections.getPlayer(gameId, authToken);

            if (user.color == null) {
                connections.removePlayer(gameId, authToken);
                Notification notification = new Notification(username + " has left the game as an observer.");
                String notifString = new Gson().toJson(notification);
                connections.broadcastMessage(gameId, authToken, notifString);
            }
            else{
                connections.removePlayer(gameId, authToken);
                GameData game = gameService.getGame(gameId);
                GameData removePlayer = null;
                if(user.color == ChessGame.TeamColor.WHITE){
                    removePlayer = new GameData(gameId, null, game.blackUsername(),
                            game.gameName(), game.game());
                }
                else if (user.color == ChessGame.TeamColor.BLACK){
                    removePlayer = new GameData(gameId, game.whiteUsername(), null,
                            game.gameName(), game.game());
                }
                gameService.updateGame(removePlayer);
                Notification notification = new Notification(username + " has left the game as " + user.color);
                String notifString = new Gson().toJson(notification);
                connections.broadcastMessage(gameId, authToken, notifString);
            }
        } catch (Exception e) {
            Error error = new Error(e.getMessage());
            String errorString = new Gson().toJson(error);
            connections.sendToConnection(session, errorString);
        }
    }

    public void resign(Resign player, Session session) throws IOException {
        try{
            AuthData userInfo = authService.getAuthData(player.getAuthToken());
            String authToken = player.getAuthToken();
            int gameId = player.getGameID();
            String username = userInfo.username();

            GameData game = gameService.getGame(gameId);
            String winner = Objects.equals(username, game.whiteUsername()) ? game.blackUsername() : game.whiteUsername();

            UserConnections user = connections.getPlayer(gameId, authToken);

            if(user.color != null){
                Notification notification = new Notification(winner + " has won!\n" +
                        username + " has resigned from the game as " + user.color + ".\n" +
                        "Type 3 to leave the game.\n");
                String notifString = new Gson().toJson(notification);
                connections.broadcastMessage(gameId, authToken, notifString);
            }
        } catch (Exception e) {
            Error error = new Error(e.getMessage());
            String errorString = new Gson().toJson(error);
            connections.sendToConnection(session, errorString);
        }
    }

    private boolean verifyPlayer(GameData game, ChessGame.TeamColor color, String actualUsername){
        String expectedUsername = null;
        switch(color){
            case WHITE -> expectedUsername = game.whiteUsername();
            case BLACK -> expectedUsername = game.blackUsername();
            case null -> {
                return true;
            }
        }
        return expectedUsername != null && expectedUsername.equals(actualUsername);
    }
}
