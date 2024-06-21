package home.group_session;

import java.util.Optional;

public interface GroupSessionService {
    public GroupSession createSession (String creatorId, String groupSessionId);
    public Optional<GroupSession> findSession(String groupSessionId);
    
    public boolean joinSession(String clientId, String groupSessionId);
    public void leaveSession(GroupSession groupSession, String groupSessionMemberId);
    public Optional<GroupSession> findJoinedSession(String groupSessionMemberId);
    
}