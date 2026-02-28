package repository.custom.impl;

import db.DbConnection;
import model.Order;
import model.OrderDetail;
import repository.custom.OrderRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public boolean save(Order order) throws Exception {
        String sql = "INSERT INTO orders (order_date, customer_name, total_amount, cashier) VALUES (?,?,?,?)";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstm.setTimestamp(1, order.getOrderDate());
            pstm.setString(2, order.getCustomerName());
            pstm.setDouble(3, order.getTotalAmount());
            pstm.setString(4, order.getCashier());

            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0) {
                try (java.sql.ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedOrderId = generatedKeys.getInt(1);

                        for (OrderDetail detail : order.getOrderDetails()) {
                            detail.setOrderId(generatedOrderId);
                        }
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean update(Order order) throws Exception {
        String sql = "UPDATE orders SET customer_name=?, total_amount=?, cashier=?, WHERE order_id=?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, order.getCustomerName());
            pstm.setDouble(2, order.getTotalAmount());
            pstm.setString(3, order.getCashier());
            pstm.setInt(5, order.getOrderId());
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public Order search(Integer id) throws Exception {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    o.setOrderDate(rs.getTimestamp("order_date"));
                    o.setCustomerName(rs.getString("customer_name"));
                    o.setTotalAmount(rs.getDouble("total_amount"));
                    o.setCashier(rs.getString("cashier"));
                    return o;
                }
            }
        }
        return null;
    }

    @Override
    public List<Order> getAll() throws Exception {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            List<Order> list = new ArrayList<>();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setCustomerName(rs.getString("customer_name"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setCashier(rs.getString("cashier"));
                list.add(o);
            }
            return list;
        }
    }

    @Override
    public int generateNextOrderId() throws Exception {
        String sql = "SELECT MAX(order_id) FROM orders";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getInt(1) + 1;
            }
            return 1;
        }
    }

    @Override
    public boolean saveOrderWithDetails(Order order) throws Exception {
        Connection conn = DbConnection.getInstance().getConnection();
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try {
            String orderSql = "INSERT INTO orders (order_date, customer_name, total_amount, cashier) " +
                    "VALUES (?,?,?,?)";
            try (PreparedStatement pstm = conn.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstm.setTimestamp(1, order.getOrderDate());
                pstm.setString(2, order.getCustomerName());
                pstm.setDouble(3, order.getTotalAmount());
                pstm.setString(4, order.getCashier());
                pstm.executeUpdate();

                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);

                        String detailSql = "INSERT INTO order_details (order_id, product_id, qty, unit_price, subtotal) " +
                                "VALUES (?,?,?,?,?)";
                        try (PreparedStatement detailPstm = conn.prepareStatement(detailSql)) {
                            for (OrderDetail detail : order.getOrderDetails()) {
                                detailPstm.setInt(1, orderId);
                                detailPstm.setInt(2, detail.getProductId());
                                detailPstm.setInt(3, detail.getQty());
                                detailPstm.setDouble(4, detail.getUnitPrice());
                                detailPstm.setDouble(5, detail.getSubTotal());
                                detailPstm.addBatch();
                            }
                            detailPstm.executeBatch();
                        }
                    }
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommit);
        }
    }

    @Override
    public double getTodaySales() throws Exception {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE DATE(order_date) = CURRENT_DATE()";
        Connection connection = db.DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    @Override
    public int getTotalOrders() throws Exception {
        String sql = "SELECT COUNT(*) FROM orders";
        Connection connection = db.DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
