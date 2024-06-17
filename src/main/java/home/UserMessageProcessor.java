package home;

import home.message.IncomingMessage;
import home.message.OutgoingMessage;

public interface UserMessageProcessor {
    public OutgoingMessage delegate (IncomingMessage incomingMessage);
}