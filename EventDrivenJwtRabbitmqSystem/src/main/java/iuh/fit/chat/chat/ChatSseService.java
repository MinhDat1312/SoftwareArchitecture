package iuh.fit.chat.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatSseService {

    // Lưu trữ các SSE emitters cho mỗi user
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitter.onCompletion(() -> {
            log.info("SSE completed for user: {}", username);
            emitters.remove(username);
        });
        
        emitter.onTimeout(() -> {
            log.info("SSE timeout for user: {}", username);
            emitters.remove(username);
        });
        
        emitter.onError((e) -> {
            log.error("SSE error for user: {}", username, e);
            emitters.remove(username);
        });
        
        emitters.put(username, emitter);
        log.info("User {} subscribed to SSE. Total subscribers: {}", username, emitters.size());
        
        return emitter;
    }

    public void broadcastMessage(ChatMessage message) {
        log.info("Broadcasting message from {} to {} subscribers", message.getFrom(), emitters.size());
        
        emitters.forEach((username, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data(message));
                log.debug("Sent message to user: {}", username);
            } catch (IOException e) {
                log.error("Failed to send message to user: {}", username, e);
                emitters.remove(username);
            }
        });
    }

    public void removeEmitter(String username) {
        emitters.remove(username);
        log.info("Removed emitter for user: {}", username);
    }
}
