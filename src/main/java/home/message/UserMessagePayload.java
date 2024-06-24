package home.message;

@Getter
@RequiredArgsConstrucor
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