package home;

import home.message.IncomingMessage;
import home.message.MessageType;
import home.message.OutgoingMessage;
import home.message.UserMessagePayload;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UserMessageProcessorImpl implements UserMessageProcessor {
    public UserMessageProcessorImpl (Map<String, UserMessageHandler> messageHandlerMap){
        super();
        this.messageHandlerMap = messageHandlerMap;
    }
    private final Map<String, UserMessageHandler> messageHandlerMap;
    
    @Override
    public OutgoingMessage delegate(IncomingMessage incomingMessage) {
        UserMessagePayload payload = incomingMessage.userMessagePayload();
        MessageType type = incomingMessage.messageType();
        log.info("UserMessagePayload:{}", payload);
        return switch (type) {
            case SYSTEM -> messageHandlerMap.get("systemUserMessageHandler").handleMessage(incomingMessage);
            case GAME -> messageHandlerMap.get("gameUserMessageHandler").handleMessage(incomingMessage);
            case CHAT -> messageHandlerMap.get("chatUserMessageHandler").handleMessage(incomingMessage);
            default -> null;
        };
    }
}