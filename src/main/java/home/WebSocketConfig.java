package home;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import home.message.ChatUserMessageHandler;
import home.message.GameUserMessageHandler;
import home.message.SystemUserMessageHandler;
import home.message.UserMessageHandler;
import home.message.UserMessageProcessor;
import home.message.UserMessageProcessorImpl;
import home.message.factory.JsonUserMessageFactory;
import home.message.factory.UserMessageFactory;
import home.reversi.ReversiSessionService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions;
    private final ReversiSessionService reversiSessionService;
    
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
        return new MyWebSocketHandler2(userMessageFactory(), userMessageProcessor(), sessions);
    }

    @Bean
    public HandshakeInterceptor myHandshakeInterceptor () {
        return new MyHandshakeInterceptor(sessions);
    }
    @Bean
    public UserMessageFactory userMessageFactory() {
        return new JsonUserMessageFactory(objectMapper);
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
        return new SystemUserMessageHandler(objectMapper, reversiSessionService);
    }
    @Bean
    public UserMessageHandler chatUserMessageHandler () {
        return new ChatUserMessageHandler(objectMapper, reversiSessionService);
    }
    @Bean
    public UserMessageHandler gameUserMessageHandler () {
        return new GameUserMessageHandler(objectMapper, reversiSessionService);
    }
}