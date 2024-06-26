package home.websocket;

import home.input_boundary.UserMessageParser;
import home.output_boundary.ConnectionClosingHandler;
import home.output_boundary.UserMessageProcessor;
import home.input_boundary.IncomingMessage;
import home.output_boundary.OutgoingMessage;
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
    private final UserResponseMessageHandler userResponseMessageHandler;
    private final ConnectionClosingHandler connectionClosingHandler;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection closed:{}", session.getId());
        sessions.remove(session);
        OutgoingMessage outgoingMessage = connectionClosingHandler.discard(session.getId());
        log.info("outgoingMessage:{}", outgoingMessage);
        if(outgoingMessage == null) return;
        else this.sendResponseMessage(outgoingMessage);
        // 
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.add(session);
        log.info("connection opened:{}",sessionId);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        IncomingMessage incomingMessage = userMessageParser.parse(message.getPayload(), session.getId());
        log.info("userMessage:{}", incomingMessage);
        OutgoingMessage outgoingMessage = userMessageProcessor.delegate(incomingMessage);
        if (outgoingMessage == null) return;
        else this.sendResponseMessage(outgoingMessage);
        

    }

    private void sendResponseMessage(OutgoingMessage outgoingMessage) throws Exception {
        Set<String> receivers = outgoingMessage.getReceiverIds();
        for (WebSocketSession receiver:sessions) {
            if(receivers.contains(receiver.getId())) {
                userResponseMessageHandler.response(receiver, outgoingMessage);
            }
        }

    }

}