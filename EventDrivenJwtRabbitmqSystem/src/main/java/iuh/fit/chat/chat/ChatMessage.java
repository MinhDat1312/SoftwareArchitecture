package iuh.fit.chat.chat;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String from;
    private String content;
}
