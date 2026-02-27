package service.custom.impl;

import model.Order;
import repository.RepositoryFactory;
import repository.custom.OrderRepository;
import service.custom.OrderService;
import util.RepositoryType;

public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepo = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.ORDER);

    @Override
    public boolean placeOrder(Order order) throws Exception {
        return orderRepo.saveOrderWithDetails(order);
    }

    @Override
    public Order searchOrder(int orderId) throws Exception {
        return orderRepo.search(orderId);
    }

    @Override
    public double getTodaySales() throws Exception {
        return orderRepo.getTodaySales();
    }

    @Override
    public int getTotalOrders() throws Exception {
        return orderRepo.getTotalOrders();
    }
}
