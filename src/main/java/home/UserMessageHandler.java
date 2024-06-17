package home;

import home.message.IncomingMessage;
import home.message.OutgoingMessage;

public interface UserMessageHandler {
    OutgoingMessage handleMessage(IncomingMessage incomingMessage);
}