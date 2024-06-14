package home;

import java.util.UUID;

import home.message.MessageType;
import home.message.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public  class UserMessage2 {
    private MessageType messageType;
    private UserRole userRole;
    private UUID userSession;
    private String text;

    @Override
    public String toString() {
        return "UserMessage2 [messageType=" + messageType + ", userRole=" + userRole + ", userSession=" + userSession
                + ", text=" + text + "]";
    }
}