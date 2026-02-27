package service.custom;

import model.Employee;
import service.SuperService;

import java.util.List;

public interface EmployeeService extends SuperService {
    Employee login(String username, String password) throws Exception;
    boolean saveEmployee(Employee emp) throws Exception;
    boolean updateEmployee(Employee emp) throws Exception;
    boolean deleteEmployee(int empId) throws Exception;
    Employee searchEmployee(int empId) throws Exception;
    List<Employee> getAllEmployees() throws Exception;
    List<Employee> searchEmployeesByName(String namePattern) throws Exception;
}
