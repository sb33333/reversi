package home;

import home.message.OutgoingMessage;

public interface ConnectionClosingHandler {
    public OutgoingMessage discard(String groupSessionMemberId);
}