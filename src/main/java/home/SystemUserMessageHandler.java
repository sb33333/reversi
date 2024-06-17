package home;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.message.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class SystemUserMessageHandler implements UserMessageHandler {
    private final ObjectMapper objectMapper;
    private final GroupSessionService groupSessionService;

    @Override
    public  OutgoingMessage handleMessage(IncomingMessage incomingMessage) {
        UserMessagePayload messagePayload =  incomingMessage.userMessagePayload();
        UserRole role = messagePayload.userRole();
        String groupSessionId = messagePayload.groupSessionId();
        MessageType messageType = incomingMessage.messageType();
        String senderId = incomingMessage.senderId();

        switch(role) {
            case HOST:
                String newGroupSessionId = (groupSessionId == null || groupSessionId.isEmpty()) ? UUID.randomUUID().toString() : groupSessionId;
                GroupSession groupSession = groupSessionService.createSession(senderId, newGroupSessionId);
                UserMessagePayload userMessagePayload = new UserMessagePayload(role, groupSession.getSessionId(), null);
                return new OutgoingMessage(ResultStatus.SUCCESS, Set.of(senderId), messageType, userMessagePayload);
            case CLIENT:
                if (groupSessionId == null || groupSessionId.isEmpty()) {
                    return new OutgoingMessage(
                            ResultStatus.INVALID,
                            Set.of(senderId),
                            messageType,
                            new UserMessagePayload(role, null, "group session id cannot be null.")
                    );
                }

                Optional<GroupSession> reversiSession = groupSessionService.findSession(groupSessionId);
                if(reversiSession.isEmpty()) {
                    return new OutgoingMessage(
                            ResultStatus.INVALID,
                            Set.of(senderId),
                            messageType,
                            new UserMessagePayload(role, null, "session is not found")
                    );
                }
                reversiSession.get().join(senderId);
                return new OutgoingMessage(
                        ResultStatus.SUCCESS,
                        Set.of(senderId),
                        messageType,
                        new UserMessagePayload(role, groupSessionId, null)
                );
            default:
                return new OutgoingMessage(
                        ResultStatus.ERROR,
                        Set.of(senderId),
                        messageType,
                        new UserMessagePayload(role, null, "User role is not valid")
                );
        }
        
    }
}