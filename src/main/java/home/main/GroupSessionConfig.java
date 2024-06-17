package home.main;

import home.GroupSessionRepository;
import home.GroupSessionService;
import home.GroupSessionServiceImpl;
import home.MemoryGroupSessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupSessionConfig {
    @Bean
    public GroupSessionRepository groupSessionRepository() {
        return new MemoryGroupSessionRepository();
    }
    @Bean
    public GroupSessionService reversiSessionService() {
        return new GroupSessionServiceImpl(groupSessionRepository());
    }
}