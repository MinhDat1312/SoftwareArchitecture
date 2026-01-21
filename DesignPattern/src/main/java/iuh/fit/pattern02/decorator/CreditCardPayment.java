package iuh.fit.pattern02.decorator;

public class CreditCardPayment implements Payment {
    @Override
    public double pay(double amount) {
        System.out.println("Paying by Credit Card");
        return amount;
    }
}
