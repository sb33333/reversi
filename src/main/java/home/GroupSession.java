package home;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Getter
public class GroupSession {
    
    private final String sessionId;
    
    private Set<String> groupMemberIds = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupSession that = (GroupSession) o;
        return Objects.equals(sessionId, that.sessionId);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(sessionId);
    }

    public void close() {
        this.groupMemberIds.clear();
        this.groupMemberIds = null;
    }

    public Set<String> getGroupMemberIds() {
        return Set.copyOf(this.groupMemberIds);
    }

    
    public boolean join (String clientId) {
        return this.groupMemberIds.add(clientId);
    }

    public void leave (String clientId) {
        this.groupMemberIds.remove(clientId);
    }

    public Stream<String> groupMemberIdStream () {
        return this.groupMemberIds.stream().filter(id->id!=null);
    }

}