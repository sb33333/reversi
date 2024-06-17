package home.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions;
    private final GroupSessionService groupSessionService;
    
    // @Override
    // pbulic void rgisterWebSocketHandlers() {
    //     registry.addHandler(myWebSocketHandler(), "/server"))
    //     .setAllowedOrigins("*")
    //     .addInterceptors(myHandshakeInterceptor());
    // }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/server")
            .setAllowedOrigins("*")
            .addInterceptors(myHandshakeInterceptor());
    }
    @Bean
    public WebSocketHandler myWebSocketHandler () {
        return new MyWebSocketHandler2(
                userMessageParser(),
                userMessageProcessor(),
                sessions,
                userResponseMessageHander ()
        );
    }

    @Bean
    public HandshakeInterceptor myHandshakeInterceptor () {
        return new MyHandshakeInterceptor(sessions);
    }
    @Bean
    public UserMessageParser userMessageParser() {
        return new JsonUserMessageParser(objectMapper);
    }

    @Bean
    public UserMessageProcessor userMessageProcessor() {
        return new UserMessageProcessorImpl(userMessageHandlerMap());
    }

    @Bean
    public Map<String, UserMessageHandler> userMessageHandlerMap() {
        Map<String ,UserMessageHandler>map=new HashMap<>();
        map.put("systemUserMessageHandler", systemUserMessageHandler());
        map.put("chatUserMessageHandler", chatUserMessageHandler());
        map.put("gameUserMessageHandler", gameUserMessageHandler());
        return map;
    }
    @Bean
    public UserMessageHandler systemUserMessageHandler () {
        return new SystemUserMessageHandler(objectMapper, groupSessionService);
    }
    @Bean
    public UserMessageHandler chatUserMessageHandler () {
        return new ChatUserMessageHandler(objectMapper, groupSessionService);
    }
    @Bean
    public UserMessageHandler gameUserMessageHandler () {
        return new GameUserMessageHandler(objectMapper, groupSessionService);
    }
    @Bean
    public UserResponseMessageHander userResponseMessageHander () {return new JsonUserResponseMessageHandler(objectMapper);}
}