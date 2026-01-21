package iuh.fit.pattern02.decorator;

public class PayPalPayment implements Payment{
    @Override
    public double pay(double amount) {
        System.out.println("Paying by PayPal");
        return amount;
    }
}
