package home.message;

import home.UserRole;

public record UserMessagePayload(
        
        String groupSessionId,
        String text
) {
    @Override
    public String toString() {
        return ''
    }
}