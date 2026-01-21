package iuh.fit.pattern02.state;

import iuh.fit.pattern02.model.Order;

public class NewOrderState implements OrderState {

    @Override
    public void handle(Order order) {
        System.out.println("Checking order information...");
        order.setState(new ProcessingOrderState());
    }
}
