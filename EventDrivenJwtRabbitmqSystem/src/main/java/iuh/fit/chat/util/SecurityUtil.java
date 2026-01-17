package iuh.fit.chat.util;

import com.nimbusds.jose.util.Base64;
import iuh.fit.chat.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SecurityUtil {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${jwt.secret}")
    private String jwtKey;
    @Value("${jwt.access-token}")
    private long jwtAccessToken;
    @Value("${jwt.refresh-token}")
    private long jwtRefreshToken;
    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createAccessToken(LoginResponse loginResponse) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtAccessToken, ChronoUnit.SECONDS);

        var user = loginResponse.getUsername();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(user)
                .claim("username", user)
                .build();

        JwsHeader header = JwsHeader.with(SecurityUtil.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String createRefreshToken(LoginResponse loginResponse){
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtRefreshToken, ChronoUnit.SECONDS);

        var user = loginResponse.getUsername();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(user)
                .claim("username", user)
                .build();

        JwsHeader header = JwsHeader.with(SecurityUtil.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public Jwt checkValidToken(String refreshToken) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM)
                .build();

        try{
            return decoder.decode(refreshToken);
        } catch (Exception e) {
            System.out.println(">>> JWT error: " + e.getMessage());
            throw e;
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}
