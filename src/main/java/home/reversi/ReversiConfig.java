package home.reversi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReversiConfig {
    @Bean
    public ReversiSessionRepository reversiSessionRepository() {
        return new MemoryReversiSessionRepository();
    }
    @Bean
    public ReversiSessionService reversiSessionService() {
        return new ReversiSessionServiceImpl(reversiSessionRepository());
    }
}