package iuh.fit.pattern02.model;

import iuh.fit.pattern02.decorator.Payment;
import iuh.fit.pattern02.state.NewOrderState;
import iuh.fit.pattern02.state.OrderState;
import iuh.fit.pattern02.strategy.TaxStrategy;

public class Order {

    private OrderState state;
    private TaxStrategy taxStrategy;
    private Payment payment;
    private double baseAmount;

    public Order(double baseAmount, TaxStrategy taxStrategy, Payment payment) {
        this.state = new NewOrderState();
        this.baseAmount = baseAmount;
        this.taxStrategy = taxStrategy;
        this.payment = payment;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void nextState() {
        state.handle(this);
    }

    public double calculateTotal() {
        double tax = taxStrategy.calculateTax(baseAmount);
        return baseAmount + tax;
    }

    public void checkout() {
        double total = calculateTotal();
        double finalAmount = payment.pay(total);
        System.out.println("Final payment amount: " + finalAmount);
    }
}

