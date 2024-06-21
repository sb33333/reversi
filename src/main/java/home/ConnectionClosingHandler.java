package home;

public interface ConnectionClosingHandler {
    public OutgoingMessage discard(String groupSessionMemberId);
}