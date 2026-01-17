package iuh.fit.chat.auth;

import iuh.fit.chat.dto.LoginRequest;
import iuh.fit.chat.dto.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = this.service.register(loginRequest);
        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse res = this.service.login(loginRequest, response);
        if(res == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refreshToken", defaultValue = "missingValue") String refreshToken,
            HttpServletResponse response
    ) {
        LoginResponse res = this.service.refresh(refreshToken, response);
        if(res == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("accessToken") String accessToken,
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        this.service.logout(accessToken, refreshToken, response);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

