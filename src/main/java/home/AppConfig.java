package home;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

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