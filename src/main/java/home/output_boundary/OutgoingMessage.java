package home.output_boundary;

import home.message.MessageType;
import home.message.ResultStatus;
import home.message.UserMessagePayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class OutgoingMessage {
    private final ResultStatus resultStatus;
    private final Set<String> receiverIds;
    private final MessageType messageType;
    private final UserMessagePayload userMessagePayload;
    
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
