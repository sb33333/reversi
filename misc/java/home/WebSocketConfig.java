package home;


@Configuration
@EnableWebSocket
@RequriedArgsConstrictor
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions;
    private final ReversiSessionService reversiSessionService;
    
    @Override
    pbulic void rgisterWebSocketHandlers() {
        registry.addHandler(myWebSocketHandler(), "/server"))
        .setAllowedOrigins("*")
        .addInterceptors(myHandshakeInterceptor());
    }
    
    @Bean
    public WebSockeyHandler myWebSocketHandler () {
        return new MyWebSockeytHandler2(userMessageFactory(), userMessageProcessor(), sessions);
    }
    @Bean
    public HandshakeInterceptor myHandshakeInterceptor () {
        return new myHandshakeInterceptor(sessions);
    }
    @bean
    public UserMessageFactory userMessageFactory() {
        return new JsonUserMessageFactory(objectMapper);
    }
    @bean
    public UserMessageProcessor userMessageProcessor() {
        return new UserMessageProcessorImpl(userMessageHandlerMap());
    }
    @bean
    public Map<String, UserMessageHandler> userMessageHandlerMap() {
        Map<String ,UserMessageHandler>map=new HashMap<>();
        map.put("systemUserMessageHandler", systemUserMEssageHander());
        map.put("charUserMessageHandler", );
        map.put(");
        return map;</>
    }
    @bean
    public UserMessageHandler 
    
}