package home;

@RequiredArgsConstructor
public class ConnectionClosingHandlerImpl implements ConnectionClosingHandler {
    private final GroupSessionService groupSessionService;
    @Override
    public OutgoingMessage discard(String groupSessionMemberId) {
        Optional<GroupSession> discardGroupSession = groupSessionService.findJoinedSession(groupSessionMemberId);
        return discardGroupSession
            .map(s->{
                Set<string> groupSessionMemberIds =s.getGroupMemberIds();
                groupSessionnService.leaveSession(s, groupSessionMemberId);
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