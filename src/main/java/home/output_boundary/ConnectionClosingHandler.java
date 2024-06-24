package home.output_boundary;

public interface ConnectionClosingHandler {
    OutgoingMessage discard(String groupSessionMemberId);
}