package repository.custom.impl;

import db.DbConnection;
import model.Product;
import repository.custom.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public boolean save(Product p) throws Exception {
        String sql = "INSERT INTO products (product_code, product_name, supplier_id, unit_price, qty_on_hand, description) " +
                "VALUES (?,?,?,?,?,?,?)";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, p.getProductCode());
            pstm.setString(2, p.getProductName());
            pstm.setInt(3, p.getSupplierId());
            pstm.setDouble(4, p.getUnitPrice());
            pstm.setInt(5, p.getQtyOnHand());
            pstm.setString(6, p.getDescription());
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Product p) throws Exception {
        String sql = "UPDATE products SET product_name=?, supplier_id=?, unit_price=?, qty_on_hand=?, description=? " +
                "WHERE product_id=?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, p.getProductName());
            pstm.setInt(2, p.getSupplierId());
            pstm.setDouble(3, p.getUnitPrice());
            pstm.setInt(4, p.getQtyOnHand());
            pstm.setString(5, p.getDescription());
            pstm.setInt(6, p.getProductId());
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM products WHERE product_id = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public Product search(Integer id) throws Exception {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setProductCode(rs.getString("product_code"));
                    p.setProductName(rs.getString("product_name"));
                    p.setSupplierId(rs.getInt("supplier_id"));
                    p.setUnitPrice(rs.getDouble("unit_price"));
                    p.setQtyOnHand(rs.getInt("qty_on_hand"));
                    p.setDescription(rs.getString("description"));
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> getAll() throws Exception {
        String sql = "SELECT * FROM products";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductCode(rs.getString("product_code"));
                p.setProductName(rs.getString("product_name"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setUnitPrice(rs.getDouble("unit_price"));
                p.setQtyOnHand(rs.getInt("qty_on_hand"));
                p.setDescription(rs.getString("description"));
                list.add(p);
            }
            return list;
        }
    }

    @Override
    public Product findByProductCode(String code) throws Exception {
        String sql = "SELECT * FROM products WHERE product_code = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, code);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setProductCode(rs.getString("product_code"));
                    p.setProductName(rs.getString("product_name"));
                    p.setSupplierId(rs.getInt("supplier_id"));
                    p.setUnitPrice(rs.getDouble("unit_price"));
                    p.setQtyOnHand(rs.getInt("qty_on_hand"));
                    p.setDescription(rs.getString("description"));
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateQuantity(int productId, int newQty) throws Exception {
        String sql = "UPDATE products SET qty_on_hand = ? WHERE product_id = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, newQty);
            pstm.setInt(2, productId);
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public int getLowStockCount(int threshold) throws Exception {
        String sql = "SELECT COUNT(*) FROM products WHERE qty_on_hand <= ?";
        Connection connection = db.DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, threshold);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
