package home.reversi;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryReversiSessionRepository implements ReversiSessionRepository {
    private Map<UUID, ReversiSession>store = new ConcurrentHashMap<>();
    
    @Override
    public ReversiSession create(ReversiSession reversiSession){
        store.put(reversiSession.getSessionId(), reversiSession);
        return reversiSession;
    }
    @Override
    public Optional<ReversiSession>select(UUID sessionId) {
        return Optional.ofNullable(store.get(sessionId));
    }
    @Override
    public void delete (UUID sessionId) {
        store.remove(sessionId);
    }

    @Override
    public ReversiSession update(ReversiSession reversiSession) {
        store.put(reversiSession.getSessionId(), reversiSession);
        return reversiSession;
        
    }
    
}