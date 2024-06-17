package home;

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
    public GroupSession update(GroupSession groupSession) {
        store.put(groupSession.getSessionId(), groupSession);
        return groupSession;
        
    }
    
}