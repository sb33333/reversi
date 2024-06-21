package home;

import home.group_session.GroupSession;
import home.group_session.GroupSessionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryGroupSessionRepository implements GroupSessionRepository {
    private Map<String, GroupSession>store = new ConcurrentHashMap<>();
    
    @Override
    public GroupSession save(GroupSession groupSession){
        store.put(groupSession.getSessionId(), groupSession);
        return groupSession;
    }
    @Override
    public Optional<GroupSession> find(String groupSessionId) {
        return Optional.ofNullable(store.get(groupSessionId));
    }

    @Override
    public void delete (String groupSessionId) {
        store.remove(groupSessionId);
    }

    @Override
    public List<GroupSession> findAll () {
        return store.values().stream().toList();
    }

    @Override
    public Optional<GroupSession> findSessionByGroupMemberId(String groupMemberId) {
        String _groupMemberId = (groupMemberId == null || groupMemberId.isBlank())?"":groupMemberId;
        return store.values().stream()
                .filter(groupSession -> groupSession.getGroupMemberIds().contains(_groupMemberId))
                .findFirst();
    }
    
}