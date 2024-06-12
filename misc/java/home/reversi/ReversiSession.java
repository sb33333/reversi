package home.reversi;

@Slf4j

public class ReversiSession {
    @Getter private WebSocketSession host;
    @Getter private UUID sessionId;
    @Getter @Setter private WebSocketSession client;
    public String getHostSessionId() {
        return this.host.getId();
    }
    public ReversiSession(WebSocketSession host) {
        super();
        this.host = host;
        this.sessionId = UUID.randomUUID();
    }
    public void close() {
        host = null;
        if (client != null) {
            if(client.isOpen())
                try {
                    client.close();
                } catch (IOException e) {
                    log.error("catch error closing websocket {}", client.getId());
                    e.printStackTrace();
                }
        }
        client = null;
        sessionId = null;
    }

    public void leave (WebSocketSession client) {
        if(this.client != null && this.client.getId().equals(client.getId())) {
            if(client.isOpen())
                try {
                    client.close();
                } catch (IOException e) {
                    log.error("catch error closing websocket {}", client.getId());
                    e.printStackTrace();
                }
            this.client = null;
        }
    }
    @Override
    public int hashCode() {
        return 0; //IDE sessionId
    }
    @Override
    public boolean equals(Object obj) {
        return false; // IDE
    }
    public Stream<WebSocketSession> usersStream() {
        return Stream.of(host, client).filter(user ->user!= null);
    }
}