package iuh.fit.DesignPattern.library.service.decorator;

public class BasicBorrow implements Borrow{
    @Override
    public String borrowInfo() {
        return "Mượn sách cơ bản";
    }
}
