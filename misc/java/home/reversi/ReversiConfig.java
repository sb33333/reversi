package home.reversi;

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