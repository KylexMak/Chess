package websocket.messages;

public class Error extends ServerMessage{
    private final String errorMessage;

    public Error(String error) {
        super(ServerMessageType.ERROR);
        this.errorMessage = error;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
