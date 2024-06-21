package home.main;

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

import home.ChatUserMessageHandler;
import home.ConnectionClosingHandler;
import home.ConnectionClosingHandlerImpl;
import home.group_session.GroupSessionService;
import home.JsonUserMessageParser;
import home.JsonUserResponseMessageHandler;
import home.MyHandshakeInterceptor;
import home.MyWebSocketHandler2;
import home.SystemUserMessageHandler;
import home.UserMessageHandler;
import home.UserMessageParser;
import home.UserMessageProcessor;
import home.UserMessageProcessorImpl;
import home.UserResponseMessageHander;
import lombok.RequiredArgsConstructor;

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
                userResponseMessageHander (),
                connectionClosingHandler()
        );
    }

    @Bean
    public HandshakeInterceptor myHandshakeInterceptor () {
        return new MyHandshakeInterceptor();
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
        
        return map;
    }
    @Bean
    public UserMessageHandler systemUserMessageHandler () {
        return new SystemUserMessageHandler(groupSessionService);
    }
    @Bean
    public UserMessageHandler chatUserMessageHandler () {
        return new ChatUserMessageHandler(groupSessionService);
    }
    
    @Bean
    public UserResponseMessageHander userResponseMessageHander () {return new JsonUserResponseMessageHandler(objectMapper);
        
    }
    @Bean
    public ConnectionClosingHandler connectionClosingHandler() {
        return new ConnectionClosingHandlerImpl(groupSessionService);
    }
}