package home;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GroupSessionServiceImpl implements GroupSessionService {
    private final GroupSessionRepository repository;
    
    @Override
    public GroupSession createSession (String hostId, String groupSessionId) {
        GroupSession groupSession = new GroupSession(hostId, groupSessionId);
        repository.save(groupSession);
        return groupSession;
    }
    @Override
    public Optional<GroupSession> findSession(String groupSessionId) {
        return repository.find(groupSessionId);
    }

    @Override
    public void closeSession(String groupSessionId) {
        Optional<GroupSession> s= repository.find(groupSessionId);
        if(s.isPresent()) {
            repository.delete(groupSessionId);
        }
    }
    @Override
    public boolean joinSession(String clientId, String groupSessionId) {
        Optional<GroupSession>s= repository.find(groupSessionId);
        return s.map(groupSession -> groupSession.join(clientId)).orElse(false);
    }
    @Override
    public void leaveSession(String clientId, String groupSessionId) {
        Optional<GroupSession>s=repository.find(groupSessionId);
        s.ifPresent(groupSession -> groupSession.leave(clientId));
    }
}