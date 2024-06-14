package home;

import java.util.Set;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import home.message.UserMessage;
import home.message.UserMessageProcessor;
import home.message.factory.UserMessageFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MyWebSocketHandler2 extends TextWebSocketHandler{
    private final UserMessageFactory userMessageFactory;
    private final UserMessageProcessor userMessageProcessor;
    private final Set<WebSocketSession> sessions;
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection closed:{}", session.getId());
        sessions.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.add(session);
        log.info("coennection opened:{}",sessionId);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        UserMessage userMessage =userMessageFactory.factoryMethod(payload);
        log.info("userMessage:{}", userMessage);
        userMessageProcessor.delegate(session, userMessage);
        super.handleTextMessage(session, message);
    }
    
}