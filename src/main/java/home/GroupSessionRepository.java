package home;

import java.util.Optional;

public interface GroupSessionRepository {
    public GroupSession save(GroupSession groupSession);
    public Optional<GroupSession> find(String groupSessionId);
    public List<GroupSession>findAll();
    public void delete(String groupSessionId);
    public Optional<GroupSession>findSessionByGroupMemberId(String groupMemberId);
    
}