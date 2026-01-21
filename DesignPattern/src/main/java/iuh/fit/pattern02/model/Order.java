package iuh.fit.pattern02.model;

import iuh.fit.pattern02.state.NewOrderState;
import iuh.fit.pattern02.state.OrderState;

public class Order {

    private OrderState state;

    public Order() {
        this.state = new NewOrderState();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void process() {
        state.handle(this);
    }
}

