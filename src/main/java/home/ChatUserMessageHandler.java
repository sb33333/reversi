package home;


import home.group_session.GroupSession;
import home.group_session.GroupSessionService;
import home.message.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChatUserMessageHandler implements UserMessageHandler {

    private final GroupSessionService groupSessionService;

    @Override
    public OutgoingMessage handleMessage(IncomingMessage incomingMessage) {
        UserMessagePayload messagePayload =  incomingMessage.userMessagePayload();
        String groupSessionId = messagePayload.groupSessionId();
        MessageType messageType = incomingMessage.messageType();
        String senderId = incomingMessage.senderId();

        if(groupSessionId == null) {
            return new OutgoingMessage(
                ResultStatus.INVALID,
                Set.of(senderId),
                messageType,
                new UserMessagePayload(null, "group sessionid cannot be null")
                );
        }
        
        Optional<GroupSession> groupSession = groupSessionService.findSession(groupSessionId);
        return groupSession.map(session -> new OutgoingMessage(
                ResultStatus.SUCCESS,
                session.groupMemberIdStream().filter(id -> !id.equals(senderId)).collect(Collectors.toUnmodifiableSet()),
                messageType,
                new UserMessagePayload(groupSessionId, messagePayload.text())
        )).orElseGet(() -> new OutgoingMessage(
                ResultStatus.INVALID,
                Set.of(senderId),
                messageType,
                new UserMessagePayload(null, "session is not found")
        ));
    }
}