package iuh.fit.DesignPattern.observer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Task implements Subject {

    private String title;
    private TaskStatus status;
    private List<Observer> observers = new ArrayList<>();

    public Task(String title) {
        this.title = title;
        this.status = TaskStatus.TODO;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
