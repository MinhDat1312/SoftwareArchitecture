package iuh.fit.chat.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import iuh.fit.chat.component.AuthenticationEntryPointCustom;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@Slf4j
public class SecurityConfig {

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
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                log.error(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return request -> {
            if (request.getCookies() != null) {
                return Arrays.stream(request.getCookies())
                        .filter(cookie -> "accessToken".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);
            }

            return null;
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, AuthenticationEntryPointCustom authenticationEntryPointCustom
    ) throws Exception {
        String[] whiteList = {
                "/auth/login",
                "/auth/register",
                "/auth/refresh",
        };


        http
                .csrf(c -> c.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests( request ->
                        request.requestMatchers(whiteList).permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .bearerTokenResolver(bearerTokenResolver())
                                .authenticationEntryPoint(authenticationEntryPointCustom)
                )
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .formLogin(f -> f.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}

