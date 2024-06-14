package home.message;

import org.springframework.web.socket.WebSocketSession;

public interface UserMessageProcessor {
    public void delegate (WebSocketSession session, UserMessage userMessage) throws Exception;
}