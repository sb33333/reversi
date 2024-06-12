package home;

@Slf4j
@requiredArgsConstructor
public class MyHandshakeInterceptor implements HandshakeInterceptor{
    private final Set<WebSocketSession> sessions;
    
    @Override
    public boolean beforeHandsake() {
        log.info("{}", request.getRemoteAddress());
        return true;
    }
}