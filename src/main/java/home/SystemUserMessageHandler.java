package home;

import home.group_session.GroupSession;
import home.group_session.GroupSessionService;
import home.message.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class SystemUserMessageHandler implements UserMessageHandler {
    
    private final GroupSessionService groupSessionService;

    @Override
    public  OutgoingMessage handleMessage(IncomingMessage incomingMessage) {
        UserMessagePayload messagePayload =  incomingMessage.userMessagePayload();
        
        String groupSessionId = messagePayload.groupSessionId();
        MessageType messageType = incomingMessage.messageType();
        String senderId = incomingMessage.senderId();

        String newGroupSessionId = (groupSessionId == null || groupSessionId.isEmpty()) ? UUID.randomUUID().toString() : groupSessionId;
        Optional<GroupSession> optional = groupSessionService.findSession(newGroupSessionId);
        if(optional.isPresent()) {
            GroupSession groupSession = optional.get();
            groupSession.join(senderId);
            return new OutgoingMessage(
                ResultStatus.SUCCESS,
                    Set.of(senderId),
                    messageType,
                    new UserMessagePayload(null, newGroupSessionId+":join session")
            );
        } else {
            GroupSession groupSession = groupSessionService.createSession(senderId, newGroupSessionId);
            return new OutgoingMessage(
                ResultStatus.SUCCESS,
                Set.of(senderId),
                messageType,
                new UserMessagePayload(null, groupSession.getSessionId() + ":session created")
            );
        }
    }
}