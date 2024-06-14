package home.reversi;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

public interface ReversiSessionService {
    public UUID createSession (WebSocketSession session);
    public Optional<ReversiSession> findSession(UUID sessionId);
    public void closeSession(UUID sessionId);
    public boolean joinSession(WebSocketSession client, UUID sessionId);
    public void leaveSession(WebSocketSession client, UUID sessionId);
    
}