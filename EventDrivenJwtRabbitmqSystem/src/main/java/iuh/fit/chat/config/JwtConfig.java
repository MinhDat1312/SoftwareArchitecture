package iuh.fit.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String jwtKey;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM)
                .build();
    }

    private SecretKey getSecretKey() {
        // Đảm bảo key đủ dài cho HS512 (64 bytes)
        byte[] keyBytes = jwtKey.getBytes(StandardCharsets.UTF_8);
        
        // Nếu key ngắn hơn 64 bytes, padding thêm
        if (keyBytes.length < 64) {
            byte[] paddedKey = new byte[64];
            System.arraycopy(keyBytes, 0, paddedKey, 0, keyBytes.length);
            // Fill phần còn lại với 0
            for (int i = keyBytes.length; i < 64; i++) {
                paddedKey[i] = 0;
            }
            keyBytes = paddedKey;
        }
        
        return new SecretKeySpec(keyBytes, 0, 64, "HmacSHA512");
    }
}

