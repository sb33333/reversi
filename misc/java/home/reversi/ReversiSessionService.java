package home.reversi;

public interface ReversiSessionService {
    public UUID createSession (WebSocketSession session);
    public Optional<ReversiSession> findSession(UUID sessionId);
    public void closeSession(UUID sessionId);
    public boolean joinSession(WebSocketSession client, UUID sessionId);
    public void leaveSession(WebSocketSession client, UUID sessionId);
    
}