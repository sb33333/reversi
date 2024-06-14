package home.message.factory;

import home.message.UserMessage;

public interface UserMessageFactory {
    UserMessage factoryMethod(String payload) throws Exception;
}