package websocket.messages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
    private final ChessGame game;
    private final ChessGame.TeamColor color;

    public LoadGame(ChessGame game, ChessGame.TeamColor color) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.color = color;
    }

    public ChessGame getGame(){
        return game;
    }

    public ChessGame.TeamColor getTeamColor(){
        return color;
    }
}
