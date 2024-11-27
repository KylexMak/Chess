package websocket.commands;

public class Redraw extends UserGameCommand{
    public Redraw(String authToken, Integer gameID) {
        super(CommandType.REDRAW, authToken, gameID);
    }
}
