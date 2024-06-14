package home.reversi;

import java.util.Optional;
import java.util.UUID;

public interface ReversiSessionRepository {
    public ReversiSession create(ReversiSession reversiSession);
    public Optional<ReversiSession> select(UUID sessionId);
    public void delete(UUID sessionId);
    public ReversiSession update(ReversiSession reversiSession);
    
}