package home;

import home.group_session.GroupSession;
import home.group_session.GroupSessionService;
import home.input_boundary.IncomingMessage;
import home.output_boundary.UserMessageHandler;
import home.message.*;
import home.output_boundary.OutgoingMessage;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class SystemUserMessageHandler implements UserMessageHandler {
    
    private final GroupSessionService groupSessionService;

    @Override
    public OutgoingMessage handleMessage(IncomingMessage incomingMessage) {
        UserMessagePayload messagePayload =  incomingMessage.getUserMessagePayload();
        
        String groupSessionId = messagePayload.getGroupSessionId();
        MessageType messageType = incomingMessage.getMessageType();
        String senderId = incomingMessage.getSenderId();

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