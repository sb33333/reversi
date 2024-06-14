package home.reversi;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReversiSessionServiceImpl implements ReversiSessionService {
    private final ReversiSessionRepository repository;
    
    @Override
    public UUID createSession (WebSocketSession session) {
        ReversiSession reversiSession = new ReversiSession(session);
        repository.create(reversiSession);
        return reversiSession.getSessionId();
    }
    @Override
    public Optional<ReversiSession> findSession(UUID sessionId) {
        return repository.select(sessionId);
    }
    @Override
    public void closeSession(UUID sessionId) {
        Optional<ReversiSession> s= repository.select(sessionId);
        if(s.isPresent()) {
            s.get().close();
            repository.delete(sessionId);
        }
    }
    @Override
    public boolean joinSession(WebSocketSession client, UUID sessionId) {
        Optional<ReversiSession>s= repository.select(sessionId);
        if(s.isPresent()){
            s.get().setClient(client);
            return true;
        }
        return false;
    }
    @Override
    public void leaveSession(WebSocketSession client, UUID sessionId) {
        Optional<ReversiSession>s=repository.select(sessionId);
        if(s.isPresent()) {
            s.get().leave(client);
        }
    }
}