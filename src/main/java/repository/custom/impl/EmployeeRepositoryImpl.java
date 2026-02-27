package repository.custom.impl;

import db.DbConnection;
import model.Employee;
import repository.custom.EmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    public Employee login(String username, String password) throws Exception {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employees WHERE username = ? AND password = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, username);
        pstm.setString(2, password);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new Employee(
                    resultSet.getInt("employee_Id"),
                    resultSet.getString("employee_name"),
                    resultSet.getString("role"),
                    "",
                    resultSet.getString("email"),
                    0.0,
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
        }
        return null;
    }

    @Override
    public boolean save(Employee emp) throws Exception {
        String sql = "INSERT INTO employees (employee_name, role, phone, email, salary, username, password) VALUES (?,?,?,?,?,?,?)";
        Connection conn = DbConnection.getInstance().getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, emp.getEmployeeName());
            pstm.setString(2, emp.getRole());
            pstm.setString(3, emp.getPhone());
            pstm.setString(4, emp.getEmail());
            pstm.setDouble(5, emp.getSalary());
            pstm.setString(6, emp.getUsername());
            pstm.setString(7, emp.getPassword());
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Employee emp) throws Exception {
        String sql = "UPDATE employees SET " +
                "employee_name = ?, " +
                "role = ?, " +
                "phone = ?, " +
                "email = ?, " +
                "salary = ?, " +
                "username = ?, " +
                "password = ? " +
                "WHERE employee_id = ?";

        Connection conn = DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, emp.getEmployeeName());
            pstm.setString(2, emp.getRole());
            pstm.setString(3, emp.getPhone());
            pstm.setString(4, emp.getEmail());
            pstm.setDouble(5, emp.getSalary());
            pstm.setString(6, emp.getUsername());
            pstm.setString(7, emp.getPassword());
            pstm.setInt(8, emp.getEmployeeId());

            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        Connection conn = DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public Employee search(Integer id) throws Exception {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";

        Connection conn = DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getInt("employee_id"));
                    emp.setEmployeeName(rs.getString("employee_name"));
                    emp.setRole(rs.getString("role"));
                    emp.setPhone(rs.getString("phone"));
                    emp.setEmail(rs.getString("email"));
                    emp.setSalary(rs.getDouble("salary"));
                    emp.setUsername(rs.getString("username"));
                    emp.setPassword(rs.getString("password"));
                    return emp;
                }
            }
        }
        return null;
    }

    @Override
    public List<Employee> getAll() throws Exception {
        String sql = "SELECT * FROM employees";

        Connection conn = DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            List<Employee> list = new ArrayList<>();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setEmployeeName(rs.getString("employee_name"));
                emp.setRole(rs.getString("role"));
                emp.setPhone(rs.getString("phone"));
                emp.setEmail(rs.getString("email"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                list.add(emp);
            }
            return list;
        }
    }

    @Override
    public List<Employee> searchByName(String namePattern) throws Exception {
        String sql = "SELECT * FROM employees WHERE employee_name LIKE ? ORDER BY employee_name";

        Connection conn = DbConnection.getInstance().getConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, "%" + namePattern + "%");

            try (ResultSet rs = pstm.executeQuery()) {
                List<Employee> list = new ArrayList<>();
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getInt("employee_id"));
                    emp.setEmployeeName(rs.getString("employee_name"));
                    emp.setRole(rs.getString("role"));
                    emp.setPhone(rs.getString("phone"));
                    emp.setEmail(rs.getString("email"));
                    emp.setSalary(rs.getDouble("salary"));
                    emp.setUsername(rs.getString("username"));
                    emp.setPassword(rs.getString("password"));
                    list.add(emp);
                }
                return list;
            }
        }
    }
}
