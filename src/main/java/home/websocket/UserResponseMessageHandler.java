package home.websocket;

import home.output_boundary.OutgoingMessage;
import org.springframework.web.socket.WebSocketSession;

public interface UserResponseMessageHandler {
    /**
     * send message to {@link org.springframework.web.socket.WebSocketSession}
     */
    void response(WebSocketSession webSocketSession, OutgoingMessage outgoingMessage) throws Exception;
}
