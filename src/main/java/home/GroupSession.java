package home;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Getter
public class GroupSession {
    private final String hostId;
    private final String sessionId;
    @Setter private String clientId;


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

    public boolean join (String clientId) {
        if (this.clientId != null) return false;
        this.clientId = clientId;
        return true;
    }
    public void leave (String clientId) {
        if (this.clientId != null && this.clientId.equals(clientId)) {
            this.clientId = null;
        }
    }

    public Stream<String> groupMemberIdStream () {
        return Stream.of(hostId, clientId);
    }

//    public void close() {
//        host = null;
//        if (client != null) {
//            if(client.isOpen())
//                try {
//                    client.close();
//                } catch (IOException e) {
//                    log.error("catch error closing websocket {}", client.getId());
//                    e.printStackTrace();
//                }
//        }
//        client = null;
//        sessionId = null;
//    }
//
//    public void leave (WebSocketSession client) {
//        if(this.client != null && this.client.getId().equals(client.getId())) {
//            if(client.isOpen())
//                try {
//                    client.close();
//                } catch (IOException e) {
//                    log.error("catch error closing websocket {}", client.getId());
//                    e.printStackTrace();
//                }
//            this.client = null;
//        }
//    }

}