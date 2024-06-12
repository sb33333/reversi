package home.message;

@Slf4j
public class UserMessageProcessorImpl implements UserMessageProcessor {
    public UserMessageProcessorImpl (Map<String, UserMessageHandler> messageHandlerMap){
        super();
        this.messageHandlerMap = messageHandlerMap;
    }
    private final Map<String, UserMessageHandler> messageHandlerMap;
    
    @Override
    public void delegate(WebsocketSession session, UserMessage userMessage) throws Exception {
        UserMessagePayload payload = usermessage.getMessagePayload();
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