package home;

import home.message.MessageType;
import org.junit.jupiter.api.Test;

public class HelloTest {
    @Test
    public void test1() {
        String test = null;
        MessageType type = MessageType.valueOf(test);
        System.out.println(type);
    }
}
