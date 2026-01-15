package iuh.fit.chat.chat;

import iuh.fit.chat.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatConsumer {

    private final ChatSseService sseService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receive(ChatMessage msg) {
        log.info("💬 {} : {}", msg.getFrom(), msg.getContent());
        
        // Broadcast tin nhắn đến tất cả clients qua SSE
        sseService.broadcastMessage(msg);
    }
}
