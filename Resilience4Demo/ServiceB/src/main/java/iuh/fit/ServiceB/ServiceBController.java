package iuh.fit.ServiceB;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/b")
public class ServiceBController {

    private final Random random = new Random();

    @GetMapping("/data")
    public String getData() throws InterruptedException {

//        Test Circuit Breaker
//        System.out.println("Service B CALLED");
//        throw new RuntimeException("Service B FAILED");

//        Test Retry
//        throw new RuntimeException("Service B FAILED");

//        Test Rate Limiter
//        System.out.println("Service B CALLED");
//        return "Data from Service B";


//        Test Bulkhead
        System.out.println("Service B processing...");
        Thread.sleep(30000);
        return "Data from Service B";
    }
}

