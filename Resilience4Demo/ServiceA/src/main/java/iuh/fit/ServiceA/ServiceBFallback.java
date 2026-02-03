package iuh.fit.ServiceA;

import org.springframework.stereotype.Component;

@Component
public class ServiceBFallback implements ServiceBClient {
    @Override
    public String getData() {
        return "Feign fallback after retries";
    }
}
