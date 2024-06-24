package home.input_boundary;

import home.message.MessageType;
import home.message.UserMessagePayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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