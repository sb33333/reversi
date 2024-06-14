package home.message.factory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import home.message.DefaultUserMessage;
import home.message.MessageType;
import home.message.UserMessage;
import home.message.UserMessagePayload;
import home.message.UserRole;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonUserMessageFactory implements UserMessageFactory {
    private final ObjectMapper objectMapper;

    @Override
    public UserMessage factoryMethod (String payload) throws Exception {
        Map<String, String>json = objectMapper.readValue(payload, Map.class);
        MessageType type = MessageType.valueOf(json.get("messageType"));
        UserMessagePayload userMessage = null;
        switch(type) {
            case SYSTEM:
                userMessage = new UserMessagePayload.Builder()
                    .userRole(UserRole.valueOf(json.get("userRole")))
                    .gameSessionId(Optional.ofNullable(json.get("gameSessionId")).map(UUID::fromString).orElse(null))
                    .text(json.get("text"))
                    .build();
                break;
            case CHAT:
                userMessage = new UserMessagePayload.Builder()
                    .gameSessionId(Optional.ofNullable(json.get("gameSessionId")).map(UUID::fromString).orElse(null))
                    .text(json.get("text"))
                    .build();
                break;
            case GAME:
                userMessage = new UserMessagePayload.Builder()
                    .gameSessionId(Optional.ofNullable(json.get("gameSessionId")).map(UUID::fromString).orElse(null))
                    .text(json.get("text"))
                    .build();
                break;
            default:
                userMessage = new UserMessagePayload.Builder()
                    .text("invalid message")
                    .build();
                return new DefaultUserMessage(MessageType.SYSTEM, userMessage);
        }
        return new DefaultUserMessage(type, userMessage);
    }
}