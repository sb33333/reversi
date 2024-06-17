package home;

import home.message.IncomingMessage;

public interface UserMessageParser {
    IncomingMessage parse(String payload, String senderId) throws Exception;
}