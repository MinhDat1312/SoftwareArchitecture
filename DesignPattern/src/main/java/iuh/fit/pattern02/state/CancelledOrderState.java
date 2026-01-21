package iuh.fit.pattern02.state;

import iuh.fit.pattern02.model.Order;

public class CancelledOrderState implements OrderState{
    @Override
    public void handle(Order order) {
        System.out.println("Order cancelled. Processing refund...");
    }
}
