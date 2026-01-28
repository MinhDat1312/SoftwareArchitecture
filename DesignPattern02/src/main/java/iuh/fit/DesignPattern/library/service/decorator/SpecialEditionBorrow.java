package iuh.fit.DesignPattern.library.service.decorator;

public class SpecialEditionBorrow extends BorrowDecorator {
    public SpecialEditionBorrow(Borrow borrow) {
        super(borrow);
    }

    public String borrowInfo() {
        return borrow.borrowInfo() + " + Phiên bản đặc biệt";
    }
}
