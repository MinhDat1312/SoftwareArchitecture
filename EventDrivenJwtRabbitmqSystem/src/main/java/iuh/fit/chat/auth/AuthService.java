package iuh.fit.chat.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final JwtEncoder encoder;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> users = new HashMap<>();

    public void register(String u, String p) {
        if (this.users.containsKey(u)) {
            log.error("User already exists");
            return;
        }

        String hashedPassword = this.passwordEncoder.encode(p);
        this.users.put(u, hashedPassword);
    }

    public String login(String u, String p) {
        String storedHash = this.users.get(u);

        if (storedHash == null || !this.passwordEncoder.matches(p, storedHash)) {
            throw new RuntimeException("Invalid username or password");
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(u)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
