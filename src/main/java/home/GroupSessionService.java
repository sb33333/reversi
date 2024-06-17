package home;

import java.util.Optional;

public interface GroupSessionService {
    public GroupSession createSession (String hostId, String groupSessionId);
    public Optional<GroupSession> findSession(String groupSessionId);
    public void closeSession(String groupSessionId);
    public boolean joinSession(String clientId, String groupSessionId);
    public void leaveSession(String clientId, String groupSessionId);
    
}