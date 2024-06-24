package home.message;

@Getter
@RequiredArgsConstructor
public class IncomingMessage {
    private final MessageType messageType;
    private final UserMessagePayload userMessagePayload;
    private final String senderId;
    @Override
    public String toString() {
        return "IncomingMessage{" +
                "messageType=" + messageType +
                ", userMessagePayload=" + userMessagePayload +
                ", senderId='" + senderId + '\'' +
                '}';
    }
}