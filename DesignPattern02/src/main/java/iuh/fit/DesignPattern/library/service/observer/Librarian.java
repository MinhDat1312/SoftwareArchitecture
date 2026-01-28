package iuh.fit.DesignPattern.library.service.observer;

public class Librarian implements Observer {
    @Override
    public void update(String message) {
        System.out.println("📢 Librarian nhận thông báo: " + message);
    }
}

