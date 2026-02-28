package repository.custom.impl;

import db.DbConnection;
import model.OrderDetail;
import repository.custom.OrderDetailRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDetailRepositoryImpl implements OrderDetailRepository {
    @Override
    public boolean save(OrderDetail orderDetail) throws Exception {
        String sql = "INSERT INTO order_details (order_id, product_id, qty, unit_price, total) VALUES (?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, orderDetail.getOrderId());
            pstm.setInt(2, orderDetail.getProductId());
            pstm.setInt(3, orderDetail.getQty());
            pstm.setDouble(4, orderDetail.getUnitPrice());
            pstm.setDouble(5, orderDetail.getSubTotal());

            return pstm.executeUpdate() > 0;
        }
    }
}
