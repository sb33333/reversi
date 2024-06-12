package home.message;

public class DefaultUserMessage implements UserMessage {
    private MessageType messageType;
    private UserMessagePayload payload;

    public DefaultUserMessage(MessageType messageType, UserMessagePayload payload) {
        this.messageType = messageType;
        this.payload = payload;
    }
    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }
    @Override
    public UserMessagePayload getMessagePayload {
        return this.payload;
    }
}