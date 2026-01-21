package iuh.fit.pattern02.state;

import iuh.fit.pattern02.model.Order;

public interface OrderState {
    void handle(Order order);
}
