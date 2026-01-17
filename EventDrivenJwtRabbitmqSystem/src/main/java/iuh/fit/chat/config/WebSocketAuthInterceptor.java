package iuh.fit.chat.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtDecoder decoder;

    @Override
    public Message<?> preSend(Message<?> msg, MessageChannel ch) {
        var acc = MessageHeaderAccessor.getAccessor(msg, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(acc.getCommand())) {
            HttpServletRequest req =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                            .getRequest();

            String token = Arrays.stream(req.getCookies())
                    .filter(c -> "JWT_TOKEN".equals(c.getName()))
                    .findFirst().orElseThrow().getValue();

            Jwt jwt = this.decoder.decode(token);
            acc.setUser(new UsernamePasswordAuthenticationToken(jwt.getSubject(), null));
        }
        return msg;
    }
}

