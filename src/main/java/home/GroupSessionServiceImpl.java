package home;

import home.group_session.GroupSession;
import home.group_session.GroupSessionRepository;
import home.group_session.GroupSessionService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GroupSessionServiceImpl implements GroupSessionService {
    private final GroupSessionRepository repository;
    
    @Override
    public GroupSession createSession (String creatorId, String groupSessionId) {
        GroupSession groupSession = new GroupSession(creatorId, groupSessionId);
        repository.save(groupSession);
        return groupSession;
    }
    @Override
    public Optional<GroupSession> findSession(String groupSessionId) {
        return repository.find(groupSessionId);
    }

    
    private void closeSession(GroupSession groupSession) {
        repository.delete(groupSession.getSessionId());
        groupSession.close();
    }
    
    @Override
    public boolean joinSession(String clientId, String groupSessionId) {
        Optional<GroupSession>s= repository.find(groupSessionId);
        return s.map(groupSession -> groupSession.join(clientId)).orElse(false);
    }
    @Override
    public void leaveSession(GroupSession groupSession, String groupSessionMemberId) {
        groupSession.leave(groupSessionMemberId);
        if(groupSession.getGroupMemberIds().isEmpty()) this.closeSession(groupSession);
        return;
    }
    @Override
    public Optional<GroupSession> findJoinedSession(String groupSessionMemberId) {
        return repository.findSessionByGroupMemberId(groupSessionMemberId);
    }
}