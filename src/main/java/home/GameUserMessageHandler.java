package home;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.group_session.GroupSession;
import home.group_session.GroupSessionService;
import home.message.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GameUserMessageHandler implements UserMessageHandler {
    private final ObjectMapper objectMapper;
    private final GroupSessionService groupSessionService;

    @Override
    public OutgoingMessage handleMessage(IncomingMessage incomingMessage) {
        UserMessagePayload messagePayload =  incomingMessage.userMessagePayload();
        String groupSessionId = messagePayload.groupSessionId();
        MessageType messageType = incomingMessage.messageType();
        String senderId = incomingMessage.senderId();
        if(groupSessionId == null) {
            UserMessagePayload userMessagePayload = new UserMessagePayload(null, "group session id cannot be null.");
            return new OutgoingMessage(ResultStatus.INVALID, Set.of(senderId), messageType, userMessagePayload);
        }

        Optional<GroupSession> groupSession = groupSessionService.findSession(groupSessionId);
        if(groupSession.isEmpty()) {
            UserMessagePayload userMessagePayload = new UserMessagePayload(null, "session is not found");
            return new OutgoingMessage(ResultStatus.INVALID, Set.of(senderId), messageType, userMessagePayload);
        }
        Set<String> receivers = groupSession.get().groupMemberIdStream().filter(id -> !id.equals(senderId)).collect(Collectors.toSet());
        UserMessagePayload userMessagePayload = new UserMessagePayload(groupSessionId, messagePayload.text());
        return new OutgoingMessage(ResultStatus.SUCCESS, receivers, messageType, userMessagePayload);
    }
}