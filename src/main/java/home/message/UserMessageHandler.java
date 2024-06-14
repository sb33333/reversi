package home.message;

import org.springframework.web.socket.WebSocketSession;

public interface UserMessageHandler {
    void handleMessage(WebSocketSession session, UserMessagePayload userMessagePayload) throws Exception;
}