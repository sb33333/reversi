package home;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import home.message.MessageType;
import home.message.OutgoingMessage;
import home.message.ResultStatus;
import home.message.UserMessagePayload;
import lombok.RequiredArgsConstructor;

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
                    new UserMessagePayload(
                        s.getSessionId(),
                        "leave session")
                    );
            })
            .orElseGet(() -> {
                return new OutgoingMessage(ResultStatus.ERROR,
                Set.of(),
                MessageType.SYSTEM,
                new UserMessagePayload(null, "session is not found"));
            });
    }
}