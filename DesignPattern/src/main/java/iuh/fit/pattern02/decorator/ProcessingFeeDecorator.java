package iuh.fit.pattern02.decorator;

public class ProcessingFeeDecorator extends PaymentDecorator {
    public ProcessingFeeDecorator(Payment payment) {
        super(payment);
    }

    @Override
    public double pay(double amount) {
        double total = payment.pay(amount);
        System.out.println("Add processing fee 5%");
        return total * 1.05;
    }
}
