package repository.custom.impl;

import db.DbConnection;
import model.Supplier;
import repository.custom.SupplierRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {
    @Override
    public boolean save(Supplier s) throws Exception {
        String sql = "INSERT INTO suppliers (supplier_name, contact_person, phone, email, address) VALUES (?,?,?,?,?)";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, s.getSupplierName());
            pstm.setString(2, s.getContactPerson());
            pstm.setString(3, s.getPhone());
            pstm.setString(4, s.getEmail());
            pstm.setString(5, s.getAddress());
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Supplier s) throws Exception {
        String sql = "UPDATE suppliers SET supplier_name=?, contact_person=?, phone=?, email=?, address=? WHERE supplier_id=?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, s.getSupplierName());
            pstm.setString(2, s.getContactPerson());
            pstm.setString(3, s.getPhone());
            pstm.setString(4, s.getEmail());
            pstm.setString(5, s.getAddress());
            pstm.setInt(6, s.getSupplierId());
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public Supplier search(Integer id) throws Exception {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Supplier s = new Supplier();
                    s.setSupplierId(rs.getInt("supplier_id"));
                    s.setSupplierName(rs.getString("supplier_name"));
                    s.setContactPerson(rs.getString("contact_person"));
                    s.setPhone(rs.getString("phone"));
                    s.setEmail(rs.getString("email"));
                    s.setAddress(rs.getString("address"));
                    return s;
                }
            }
        }
        return null;
    }

    @Override
    public List<Supplier> getAll() throws Exception {
        String sql = "SELECT * FROM suppliers";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            List<Supplier> list = new ArrayList<>();
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("supplier_id"));
                s.setSupplierName(rs.getString("supplier_name"));
                s.setContactPerson(rs.getString("contact_person"));
                s.setPhone(rs.getString("phone"));
                s.setEmail(rs.getString("email"));
                s.setAddress(rs.getString("address"));
                list.add(s);
            }
            return list;
        }
    }

    @Override
    public List<Supplier> searchByNameOrContact(String text) throws Exception {
        String sql = "SELECT * FROM suppliers WHERE supplier_name LIKE ? OR contact_person LIKE ? OR phone LIKE ?";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            String pattern = "%" + text + "%";
            pstm.setString(1, pattern);
            pstm.setString(2, pattern);
            pstm.setString(3, pattern);
            try (ResultSet rs = pstm.executeQuery()) {
                List<Supplier> list = new ArrayList<>();
                while (rs.next()) {
                    Supplier s = new Supplier();
                    s.setSupplierId(rs.getInt("supplier_id"));
                    s.setSupplierName(rs.getString("supplier_name"));
                    s.setContactPerson(rs.getString("contact_person"));
                    s.setPhone(rs.getString("phone"));
                    s.setEmail(rs.getString("email"));
                    s.setAddress(rs.getString("address"));
                    list.add(s);
                }
                return list;
            }
        }
    }
}
