package home.message;

public record IncomingMessage(MessageType messageType, UserMessagePayload userMessagePayload, String senderId) {
    @Override
    public String toString() {
        return "IncomingMessage{" +
                "messageType=" + messageType +
                ", userMessagePayload=" + userMessagePayload +
                ", senderId='" + senderId + '\'' +
                '}';
    }
}