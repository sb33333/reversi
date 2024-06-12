package home.message;

public interface UserMessageHandler {
    void handleMessage(WebSocketSession session, UserMessagePayload userMessagePayload) throws Exception;
}