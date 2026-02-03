package iuh.fit.ServiceA;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class ServiceAService {
    private final ServiceBClient serviceBClient;

    public ServiceAService(ServiceBClient serviceBClient) {
        this.serviceBClient = serviceBClient;
    }

    @Bulkhead(name = "serviceBBulkhead", fallbackMethod = "fallback")
//    @Retry(name = "serviceBRetry")
//    @CircuitBreaker(name = "serviceBCB")
//    @RateLimiter(name = "serviceBRateLimiter")
    public String callServiceB() {
        System.out.println("Calling Service B...");
        return this.serviceBClient.getData();
    }

    public String fallback(Exception ex) {
        System.out.println("Fallback executed: " + ex.getMessage());
        return "Fallback response from Service A";
    }
}

