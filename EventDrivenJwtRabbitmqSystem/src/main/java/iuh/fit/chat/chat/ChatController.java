package iuh.fit.chat.chat;

import iuh.fit.chat.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final RabbitTemplate rabbit;
    private final ChatSseService sseService;

    @PostMapping("/send")
    public void send(@RequestBody ChatMessage msg) {
        this.rabbit.convertAndSend(RabbitMQConfig.QUEUE, msg);
    }
    
    @GetMapping("/stream")
    public SseEmitter stream(Authentication authentication) {
        String username = authentication.getName();
        return sseService.subscribe(username);
    }
}