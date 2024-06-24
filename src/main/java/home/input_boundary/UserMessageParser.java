package home.input_boundary;

/**
 * Parse and convert message from user to {@link IncomingMessage}.
 */
public interface UserMessageParser {

    /**
     * @param payload message body
     * @param senderId Identify {@link org.springframework.web.socket.WebSocketSession}
     */
    IncomingMessage parse(String payload, String senderId) throws Exception;
}