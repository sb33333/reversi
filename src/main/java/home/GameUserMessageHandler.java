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
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GameUserMessageHandler implements UserMessageHandler {
    private final GroupSessionService groupSessionService;

    @Override
    public OutgoingMessage handleMessage(IncomingMessage incomingMessage) {
        UserMessagePayload messagePayload =  incomingMessage.getUserMessagePayload();
        String groupSessionId = messagePayload.getGroupSessionId();
        MessageType messageType = incomingMessage.getMessageType();
        String senderId = incomingMessage.getSenderId();
        if(groupSessionId == null) {
            UserMessagePayload userMessagePayload = new UserMessagePayload(null, "group session id cannot be null.");
            return new OutgoingMessage(ResultStatus.INVALID, Set.of(senderId), messageType, userMessagePayload);
        }

        Optional<GroupSession> groupSession = groupSessionService.findSession(groupSessionId);

        return groupSession
                .map(gs -> new OutgoingMessage(
                        ResultStatus.SUCCESS,
                        gs.getGroupMemberIds().stream().filter(id -> !id.equals(senderId)).collect(Collectors.toSet()),
                        messageType,
                        new UserMessagePayload(groupSessionId, messagePayload.getText())
                ))
                .orElseGet(() -> new OutgoingMessage(
                        ResultStatus.INVALID,
                        Set.of(senderId),
                        messageType,
                        new UserMessagePayload(null, "session is not found")
                ));

    }
}