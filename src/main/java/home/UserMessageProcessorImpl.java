package home;

import home.output_boundary.UserMessageHandler;
import home.output_boundary.UserMessageProcessor;
import home.input_boundary.IncomingMessage;
import home.message.MessageType;
import home.output_boundary.OutgoingMessage;
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
        
        MessageType type = incomingMessage.getMessageType();
        
        return switch (type) {
            case SYSTEM -> messageHandlerMap.get("systemUserMessageHandler").handleMessage(incomingMessage);
            case GAME, CHAT -> messageHandlerMap.get("chatUserMessageHandler").handleMessage(incomingMessage);
            default -> null;
        };
    }
}