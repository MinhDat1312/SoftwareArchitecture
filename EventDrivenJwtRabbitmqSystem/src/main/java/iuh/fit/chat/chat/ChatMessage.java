package iuh.fit.chat.chat;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String from;
    private String content;
}
