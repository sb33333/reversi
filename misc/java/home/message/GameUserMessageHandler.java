package home.message;

@RequiredArgsConstructor
public class GameUserMessageHandler implements UserMessageHandler {
    private final ObjectMapper objectMapper;
    private final ReversiSessionService reversiSessionService;

    @Override
    public void handleMessage(WebSocketSession session, UserMessagePayload userMessagePayload) throws Exception {
        UUID reversiSessionId = userMessagePaload.getGameSessionUd();
        ResponseMessage res= null;
        if(reversiSessionId == null) {
            res=new ResponseMessage.Builder(MessageType.GAME, ResponseStatus.ERROR)
                .response("session id cannot be null")
                .build();
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(res)));
            return;
        }
        Optional<ReversiSession> reversiSession = reversiSessionService.findSession(reversiSessionId);;
        if(reversiSession.isPresent()) {
            res = new ResponseMessage.Builder(MessageType.GAME, ResponseStatus.OK)
                .response(userMessagePayload.getText())
                .build();
            WebSocketMessage<String> gameMessage = new TextMessage(objectMapper.writeValueAsString(res));
            Set<WebSocketSession> messageTargets = reversiSession.get().usersStream()
                .filter(u ->!u.getId().equals(session.getId())).collect(Collectors.toSet());
                for (WebSocketSession messageTarget:messageTargets) {
                    messageTarget.sendMessage(gameMessage);
                }
            return;
        } else {
            res = new ResponseMessage
                .Builder(MessageType.GAME, ResponseStatus.ERROR)
                .response("session is not found")
                .build();
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(res)));
            return;
        }
    }
}