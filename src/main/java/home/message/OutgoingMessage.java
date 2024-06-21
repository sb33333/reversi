package home.message;

import java.util.Set;

public record OutgoingMessage(
        ResultStatus resultStatus,
        Set<String> receiverIds,
        MessageType messageType,
        UserMessagePayload userMessagePayload
) {
    @Override
    public String toString() {
        return "OutgoingMessage{" +
                "resultStatus=" + resultStatus +
                ", receiverIds=" + receiverIds +
                ", messageType=" + messageType +
                ", userMessagePayload=" + userMessagePayload +
                '}';
    }
}
