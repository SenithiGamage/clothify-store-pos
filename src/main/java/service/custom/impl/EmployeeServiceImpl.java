package service.custom.impl;

import model.Employee;
import repository.RepositoryFactory;
import repository.custom.EmployeeRepository;
import service.custom.EmployeeService;
import util.RepositoryType;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository empRepo = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.EMPLOYEE);

    @Override
    public Employee login(String username, String password) throws Exception {
        return empRepo.login(username, password);
    }

    @Override
    public boolean saveEmployee(Employee emp) throws Exception {
        return empRepo.save(emp);
    }

    @Override
    public boolean updateEmployee(Employee emp) throws Exception {
        return empRepo.update(emp);
    }

    @Override
    public boolean deleteEmployee(int empId) throws Exception {
        return empRepo.delete(empId);
    }

    @Override
    public Employee searchEmployee(int empId) throws Exception {
        return empRepo.search(empId);
    }

    @Override
    public List<Employee> getAllEmployees() throws Exception {
        return empRepo.getAll();
    }

    @Override
    public List<Employee> searchEmployeesByName(String namePattern) throws Exception {
        return empRepo.searchByName(namePattern);
    }
}
