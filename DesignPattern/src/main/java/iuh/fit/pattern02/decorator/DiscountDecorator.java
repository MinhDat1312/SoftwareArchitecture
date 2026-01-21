package iuh.fit.pattern02.decorator;

public class DiscountDecorator extends PaymentDecorator{
    public DiscountDecorator(Payment payment) {
        super(payment);
    }

    @Override
    public double pay(double amount) {
        double total = payment.pay(amount);
        System.out.println("Apply discount 10%");
        return total * 0.9;
    }
}
