package home.message;

public interface UserMessage {
    public MessageType getMessageType();
    public UserMessagePayload getMessagePayload();
}