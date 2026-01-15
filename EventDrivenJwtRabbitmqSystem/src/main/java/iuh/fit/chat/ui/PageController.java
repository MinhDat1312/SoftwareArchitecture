package iuh.fit.chat.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    String login() { return "login"; }

    @GetMapping("/register")
    String register() { return "register"; }

    @GetMapping("/")
    String home() { return "redirect:/chat"; }
    
    @GetMapping("/chat")
    String chat() { return "chat"; }
}
