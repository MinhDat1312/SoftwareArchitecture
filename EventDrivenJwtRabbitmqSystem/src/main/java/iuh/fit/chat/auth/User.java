package iuh.fit.chat.auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String password;
}
