package home;

import home.group_session.GroupSession;
import home.group_session.GroupSessionService;
import home.message.MessageType;
import home.output_boundary.ConnectionClosingHandler;
import home.output_boundary.OutgoingMessage;
import home.message.ResultStatus;
import home.message.UserMessagePayload;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ConnectionClosingHandlerImpl implements ConnectionClosingHandler {
    private final GroupSessionService groupSessionService;
    @Override
    public OutgoingMessage discard(String groupSessionMemberId) {
        Optional<GroupSession> discardGroupSession = groupSessionService.findJoinedSession(groupSessionMemberId);
        return discardGroupSession
            .map(s->{
                Set<String> groupSessionMemberIds =s.getGroupMemberIds();
                groupSessionService.leaveSession(s, groupSessionMemberId);
                return new OutgoingMessage(
                    ResultStatus.SUCCESS,
                    groupSessionMemberIds.stream().filter(id->!id.equals(groupSessionMemberId)).collect(Collectors.toUnmodifiableSet()),
                    MessageType.SYSTEM,
                    new UserMessagePayload(s.getSessionId(), "leave session")
                    );
            })
            .orElseGet(() -> new OutgoingMessage(ResultStatus.ERROR,
                Set.of(),
                MessageType.SYSTEM,
                new UserMessagePayload(null, "session is not found"))
            );
    }
}