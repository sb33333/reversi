package home;

import java.util.Optional;

public interface GroupSessionRepository {
    public GroupSession save(GroupSession groupSession);
    public Optional<GroupSession> find(String groupSessionId);
    public void delete(String groupSessionId);
    public GroupSession update(GroupSession groupSession);
    
}