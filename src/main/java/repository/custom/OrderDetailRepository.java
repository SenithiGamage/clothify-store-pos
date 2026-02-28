package repository.custom;

import model.OrderDetail;
import repository.SuperRepository;

public interface OrderDetailRepository extends SuperRepository {
    boolean save(OrderDetail orderDetail) throws Exception;
}
