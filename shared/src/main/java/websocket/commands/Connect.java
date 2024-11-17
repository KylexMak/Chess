package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand{
    private final ChessGame.TeamColor color;

    public Connect(String authToken, Integer gameID, ChessGame.TeamColor color) {
        super(CommandType.CONNECT, authToken, gameID);
        this.color = color;
    }

    public ChessGame.TeamColor getTeamColor(){
        return color;
    }
}
