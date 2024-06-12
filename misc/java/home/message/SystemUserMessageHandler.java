package home.message;

@RequiredArgsConstructor
public class SystemUserMessageHandler implements UserMessageHandler {
    private final ObjectMapper objectMapper;
    private final ReversiSessionService reversiSessionService;

    @Override
    public void handleMessage(WebSocketSession session, UserMessagePayload userMessagePayload) throws Exception {
        UserRole role = userMessagePaload.getUserRole();
        UUID reversiSessionId = userMessagePaload.getGameSessionUd();
    
        switch(role) {
            case HOST:
                UUID createdSessionId = reversiSessionService.createSession(session);
                ResponseMessage res=new ResponseMessage
                    .Builder(MessageType.SYSTEM, ResponseStatus.OK)
                    .response(createdSessionId.toString())
                    .build();
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(res)));
                break;
            case CLIENT:
                if (reversiSessionId == null) {
                    res = new ResponseMessage
                        .Builder(MessageType.SYSTEM, ResponseStatus.ERROR)
                        .response("session id cannot be null")
                        .build();
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(res)));
                } else {
                    Optional<ReversiSession> reversiSession = reversiSessionService.findSession(ReversiSessionId);
                    if(reversiSession.isPresent()) {
                        reversiSession.get().setClient(session);
                        res = new ResponseMessage
                            .Builder(MessageType.SYSTEM, ResponseStatus.OK)
                            .response(reversiSessionId.toString())
                            .build();
                    } else {
                        res = new ResponseMessage
                            .Builder(MessageType.SYSTEM, ResponseStatus.ERROR)
                            .response("session is not found")
                            .build();
                    }
                    session.sendMessage (new TextMessage(objectMapper.writeValueAsString(res)));
                }
                break;
            default: break;
        }
        
    }
}