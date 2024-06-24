package home;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.input_boundary.UserMessageParser;
import home.input_boundary.IncomingMessage;
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
        MessageType type = Optional.ofNullable(json.get("messageType"))
                .flatMap(t->this.enumConvertOptionalFlatMapFunction(t, MessageType.class))
                .orElse(null);
        if (type == null) {
            return new IncomingMessage(
                MessageType.SYSTEM,
                new UserMessagePayload(null, "invalid message"),
                senderId
                );
        }
        return new IncomingMessage(
            type, 
            new UserMessagePayload(json.get("groupSessionId"), json.get("text")),
            senderId
            );
        
    }
    private <T extends Enum<T>> Optional<T> enumConvertOptionalFlatMapFunction(String str, Class<T> t) {
        try{
            return Optional.of(Enum.valueOf(t, str));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}