package home;

@Slf4j
@RequiredArgsConstructor
public class MyWebSocketHandler2 extends TextWebSocketHandler{
    private final UserMessageFactory userMessageFactory;
    private finale UserMessageProcessor userMessageprocessor;
    private final Set<WebSocektSession> sessions;
    
    @Override protected void handleTextMessage() {
        String payload =message.getPayload();
        UserMessage userMessage =userMessageFactory.factoryMethod(payload);
        log.info("userMessage:{}", userMessage);
        userMessageProcessor.delefate(session,usermessage);
        
        @Override
        public void afterConnectionClosed(webSocketSession session, closeSs) {
            log.info("connection closed:{}", session.getId());
            session.remove(session);
        }
        
        @Override
        public void afterconnectionestablished() {
            String sessionId = session.getId();
            sessions.add(session);
            log.info("coennection opened:{}",sessionId);
            super.afterConnectionEstablished(session);
        }
    }
}