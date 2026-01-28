package iuh.fit.DesignPattern.library.service.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationService {

    private final List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void notifyAll(String message) {
        observers.forEach(o -> o.update(message));
    }
}