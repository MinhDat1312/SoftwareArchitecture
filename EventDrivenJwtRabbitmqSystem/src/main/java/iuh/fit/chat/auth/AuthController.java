package iuh.fit.chat.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        this.service.register(username, password);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        String token = this.service.login(username, password);
        
        // Lưu JWT vào cookie
        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Set true nếu dùng HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600); // 1 giờ
        response.addCookie(jwtCookie);
        
        // Lưu username vào cookie (không cần HttpOnly vì JS cần đọc)
        Cookie userCookie = new Cookie("USERNAME", username);
        userCookie.setPath("/");
        userCookie.setMaxAge(3600);
        response.addCookie(userCookie);
        
        return "redirect:/chat";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Xóa cookies
        Cookie jwtCookie = new Cookie("JWT_TOKEN", null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        
        Cookie userCookie = new Cookie("USERNAME", null);
        userCookie.setPath("/");
        userCookie.setMaxAge(0);
        response.addCookie(userCookie);
        
        return "redirect:/login";
    }
}
