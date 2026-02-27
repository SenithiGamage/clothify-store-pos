package repository.custom;

import model.Employee;
import repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    List<Employee> searchByName(String namePattern) throws Exception;
    Employee login(String username, String password) throws Exception;

}
