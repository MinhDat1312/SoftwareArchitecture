package iuh.fit.ServiceA;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/a")
public class ServiceAController {
    private final ServiceAService service;

    public ServiceAController(ServiceAService service) {
        this.service = service;
    }

    @GetMapping("/call-b")
    public String callB() {
        return this.service.callServiceB();
    }
}

