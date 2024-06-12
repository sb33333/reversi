package home.message;

public interface UserMessageProcessor {
    public void delegate (WebSocketSession session, UserMessage userMessage) throws Exception;
}