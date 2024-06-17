package home.message;

public record IncomingMessage(MessageType messageType, UserMessagePayload userMessagePayload, String senderId) {
}