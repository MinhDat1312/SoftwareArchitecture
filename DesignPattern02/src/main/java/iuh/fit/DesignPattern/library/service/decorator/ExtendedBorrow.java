package iuh.fit.DesignPattern.library.service.decorator;

public class ExtendedBorrow extends BorrowDecorator {

    public ExtendedBorrow(Borrow borrow) {
        super(borrow);
    }

    public String borrowInfo() {
        return borrow.borrowInfo() + " + Gia hạn thời gian";
    }
}