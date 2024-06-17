package home.message;

import java.util.Set;

public record OutgoingMessage(
        ResultStatus resultStatus,
        Set<String> receiverIds,
        MessageType messageType,
        UserMessagePayload userMessagePayload
) {
}
