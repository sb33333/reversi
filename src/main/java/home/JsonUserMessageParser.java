package home;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.message.IncomingMessage;
import home.message.MessageType;
import home.message.UserMessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JsonUserMessageParser implements UserMessageParser {
    private final ObjectMapper objectMapper;

    @Override
    public IncomingMessage parse(String payload, String senderId) throws Exception {
        Map<String, String>json = objectMapper.readValue(payload, Map.class);
        log.info("{}", json);
        MessageType type = Optional
                .ofNullable(json.get("messageType"))
                .map(MessageType::valueOf)
                .orElse(null);
        if (type == null) {
            UserMessagePayload userMessage = new UserMessagePayload(null, null, "invalid message");
            return new IncomingMessage(MessageType.SYSTEM, userMessage, senderId);
        } else {
            return new IncomingMessage(
                type, 
                new UserMessagePayload(Optional.ofNullable(json.get("userRole")).map(UserRole::valueOf).orElse(null), json.get("groupSessionId"), json.get("text")),
                senderId
                );
        }
    }
}