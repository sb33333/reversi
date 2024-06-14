package home.message;

import java.util.UUID;

import lombok.Getter;

@Getter
public class UserMessagePayload {
    private UserRole userRole;
    private UUID gameSessionId;
    private String text;
    private UserMessagePayload(Builder builder) {
        super();
        this.userRole = builder.userRole;
        this.gameSessionId = builder.gameSessionId;
        this.text = builder.text;
    }
    
    @Override
    public String toString() {
        return "UserMessagePayload [userRole=" + userRole + ", gameSessionId=" + gameSessionId + ", text=" + text + "]";
    }
    public static class Builder {
        private UserRole userRole;
        private UUID gameSessionId;
        private String text;
        public Builder() {}
        public Builder userRole(UserRole userRole) {
            this.userRole = userRole;
            return this;
        }
        public Builder gameSessionId(UUID gameSessionId) {
            this.gameSessionId = gameSessionId;
            return this;
        }
        public Builder text(String text) {
            this.text = text;
            return this;
        }
        public UserMessagePayload build() {
            return new UserMessagePayload(this);
        }
    }
}