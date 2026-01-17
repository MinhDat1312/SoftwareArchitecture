package iuh.fit.chat.auth;

import iuh.fit.chat.dto.LoginRequest;
import iuh.fit.chat.dto.LoginResponse;
import iuh.fit.chat.util.SecurityUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${jwt.access-token}")
    private long jwtAccessToken;
    @Value("${jwt.refresh-token}")
    private long jwtRefreshToken;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    public static final List<LoginResponse> users = new ArrayList<>();

    public LoginResponse register(LoginRequest request) {
        if(request.getUsername() == null || request.getUsername().isEmpty()) {
            return null;
        }

        LoginResponse user = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(request.getUsername()))
                .findFirst()
                .orElse(null);
        if(user != null) {
            return null;
        }

        String hashPassword = this.passwordEncoder.encode(request.getPassword());
        LoginResponse response = new LoginResponse();
        response.setUsername(request.getUsername());
        response.setPassword(hashPassword);

        users.add(response);

        return response;
    }

    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = this.authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse res = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(loginRequest.getUsername()))
                .findFirst()
                .orElse(null);
        if(res == null) {
            return null;
        }

        String accessToken = this.securityUtil.createAccessToken(res);
        String refreshToken = this.securityUtil.createRefreshToken(res);

        res.setRefreshToken(refreshToken);

        ResponseCookie accessCookie = ResponseCookie
                .from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false) // for dev
                .sameSite("Lax") // for dev
                .path("/")
                .maxAge(jwtAccessToken)
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // for dev
                .sameSite("Lax") // for dev
                .path("/")
                .maxAge(jwtRefreshToken)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return res;
    }

    public LoginResponse refresh(String refreshToken, HttpServletResponse response) {
        if(refreshToken.equalsIgnoreCase("missingValue")) {
            return null;
        }

        Jwt jwt = this.securityUtil.checkValidToken(refreshToken);
        String username = jwt.getSubject();

        LoginResponse currentUser = users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
        if(currentUser == null){
            return null;
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                currentUser.getUsername(),
                currentUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String newAccessToken = this.securityUtil.createAccessToken(currentUser);
        String newRefreshToken = this.securityUtil.createRefreshToken(currentUser);

        currentUser.setRefreshToken(newRefreshToken);

        ResponseCookie newAccessCookie = ResponseCookie
                .from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false) // for dev
                .sameSite("Lax") // for dev
                .path("/")
                .maxAge(jwtAccessToken)
                .build();

        ResponseCookie newRefreshCookie = ResponseCookie
                .from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(false) // for dev
                .sameSite("Lax") // for dev
                .path("/")
                .maxAge(jwtRefreshToken)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshCookie.toString());

        return currentUser;
    }

    public void logout(String accessToken, String refreshToken, HttpServletResponse response) {
        users.stream()
                .filter(u -> u.getRefreshToken() != null && u.getRefreshToken().equalsIgnoreCase(refreshToken))
                .findFirst().ifPresent(currentUser -> currentUser.setRefreshToken(null));

        ResponseCookie deleteAccessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false) // for dev
                .sameSite("Lax") // for dev
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteRefreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // for dev
                .sameSite("Lax") // for dev
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString());
    }
}

