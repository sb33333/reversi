package home.message.factory;

public interface UserMessageFactory {
    UserMessage factoryMethod(String payload) throw Exception;
}