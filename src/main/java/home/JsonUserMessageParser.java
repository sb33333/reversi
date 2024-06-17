package home;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.message.IncomingMessage;
import home.message.MessageType;
import home.message.UserMessagePayload;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JsonUserMessageParser implements UserMessageParser {
    private final ObjectMapper objectMapper;

    @Override
    public IncomingMessage parse(String payload, String senderId) throws Exception {
        Map<String, String>json = objectMapper.readValue(payload, Map.class);
        MessageType type = Optional
                .ofNullable(json.get("messageType"))
                .map(MessageType::valueOf)
                .orElse(null);
        if (type == null) {
            UserMessagePayload userMessage = new UserMessagePayload(null, null, "invalid message");
            return new IncomingMessage(MessageType.SYSTEM, userMessage, senderId);
        } else {
            UserMessagePayload userMessage = new UserMessagePayload(UserRole.valueOf(json.get("userRole")), json.get("gameSessionId"), json.get("text"));
            return new IncomingMessage(type, userMessage, senderId);
        }
    }
}