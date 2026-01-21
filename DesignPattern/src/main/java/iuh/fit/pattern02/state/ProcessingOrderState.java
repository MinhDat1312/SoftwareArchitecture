package iuh.fit.pattern02.state;

import iuh.fit.pattern02.model.Order;

public class ProcessingOrderState implements OrderState {
    @Override
    public void handle(Order order) {
        System.out.println("Packing and shipping order...");
        order.setState(new DeliveredOrderState());
    }
}
