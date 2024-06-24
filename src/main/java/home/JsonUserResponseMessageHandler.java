package home;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.output_boundary.OutgoingMessage;
import home.websocket.UserResponseMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
public class JsonUserResponseMessageHandler implements UserResponseMessageHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void response(WebSocketSession webSocketSession, OutgoingMessage outgoingMessage) throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(outgoingMessage);
        webSocketSession.sendMessage(new TextMessage(jsonPayload));
    }
}
