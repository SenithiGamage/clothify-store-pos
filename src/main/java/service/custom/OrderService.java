package service.custom;

import model.Order;
import service.SuperService;

public interface OrderService extends SuperService {
    boolean placeOrder(Order order) throws Exception;
    Order searchOrder(int orderId) throws Exception;
    double getTodaySales() throws Exception;
    int getTotalOrders() throws Exception;
}

