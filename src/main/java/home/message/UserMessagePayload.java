package home.message;

public record UserMessagePayload(
        
        String groupSessionId,
        String text
) {

    @Override
    public String toString() {
        return "UserMessagePayload{" +
                "groupSessionId='" + groupSessionId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}