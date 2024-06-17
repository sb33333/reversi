package home.message;

import home.UserRole;

public record UserMessagePayload(
        UserRole userRole,
        String groupSessionId,
        String text
) {
    @Override
    public String toString() {
        return "UserMessagePayload [userRole=" + userRole + ", gameSessionId=" + groupSessionId + ", text=" + text + "]";
    }
}