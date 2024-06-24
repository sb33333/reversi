package home.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserMessagePayload {
    private final String groupSessionId;
    private final String text;

    @Override
    public String toString() {
        return "UserMessagePayload{" +
                "groupSessionId='" + groupSessionId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}