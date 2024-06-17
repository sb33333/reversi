package home.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppConfig{
    @Bean
    public Set<WebSocketSession>webSocketSessions() {
        return Collections.synchronizedSet(new HashSet<>());
    }
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}