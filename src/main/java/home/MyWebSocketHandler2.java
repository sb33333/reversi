package home;

import home.message.IncomingMessage;
import home.message.OutgoingMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class MyWebSocketHandler2 extends TextWebSocketHandler{
    private final UserMessageParser userMessageParser;
    private final UserMessageProcessor userMessageProcessor;
    private final Set<WebSocketSession> sessions;
    private final UserResponseMessageHander userResponseMessageHander;
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection closed:{}", session.getId());
        sessions.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.add(session);
        log.info("connection opened:{}",sessionId);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String senderId = session.getId();
        IncomingMessage incomingMessage = userMessageParser.parse(payload, senderId);
        log.info("userMessage:{}", incomingMessage);
        OutgoingMessage outgoingMessage = userMessageProcessor.delegate(incomingMessage);
        if (outgoingMessage == null) return;

        Set<String> receivers = outgoingMessage.receiverIds();
        for (WebSocketSession receiver : sessions) {
            if (receivers.contains(receiver.getId())) {
                userResponseMessageHander.response(receiver, outgoingMessage);
            }
        }

    }

}