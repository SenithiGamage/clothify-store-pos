package repository.custom;

import model.Order;
import repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    int generateNextOrderId() throws Exception;
    boolean saveOrderWithDetails(Order order) throws Exception;
    double getTodaySales() throws Exception;
    int getTotalOrders() throws Exception;
}
