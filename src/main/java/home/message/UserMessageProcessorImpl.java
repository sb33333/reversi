package home.message;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMessageProcessorImpl implements UserMessageProcessor {
    public UserMessageProcessorImpl (Map<String, UserMessageHandler> messageHandlerMap){
        super();
        this.messageHandlerMap = messageHandlerMap;
    }
    private final Map<String, UserMessageHandler> messageHandlerMap;
    
    @Override
    public void delegate(WebSocketSession session, UserMessage userMessage) throws Exception {
        UserMessagePayload payload = userMessage.getMessagePayload();
        MessageType type = userMessage.getMessageType();
        log.info("UserMessagePayload:{}", payload);
        switch(type) {
            case SYSTEM:
                messageHandlerMap.get("systemUserMessageHandler").handleMessage(session, payload); break;
            case GAME:
                messageHandlerMap.get("gamwUserMessageHandler").handleMessage(session, payload); break;
            case CHAT:
                messageHandlerMap.get("chatUserMessageHandler").handleMessage(session, payload); break;
            default: break;
        }
    }
}