package iuh.fit.chat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messaging;

    @MessageMapping("/chat.send")
    public void send(ChatMessage msg, Principal principal) {
        msg.setFrom(principal.getName());
        this.messaging.convertAndSend("/topic/chat", msg);
    }
}
