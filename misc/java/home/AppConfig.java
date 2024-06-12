package home;

@Configuration
public class AppConfig{
    @Booleanpbulci Set<WebSocketSession>webSocketSessions() {
        return Collections.synchronizedSet(new HashSetM<>());
    }
    @Booleanpbulcipublic ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}