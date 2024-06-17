package home;

import home.message.OutgoingMessage;
import org.springframework.web.socket.WebSocketSession;

public interface UserResponseMessageHander {
    public void response (WebSocketSession webSocketSession, OutgoingMessage outgoingMessage) throws Exception;
}
