package websocket.messages;

public class Notification extends ServerMessage{
    private final String notificationMessage;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.notificationMessage = message;
    }

    public String getNotificationMessage(){
        return notificationMessage;
    }
}
