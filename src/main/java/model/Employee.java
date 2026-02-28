package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Employee {
    private int employeeId;
    private String employeeName;
    private String role;
    private String phone;
    private String email;
    private Double salary;
    private String username;
    private String password;
}
