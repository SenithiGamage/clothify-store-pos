package model.TM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EmployeeTM {
    private Integer employeeId;
    private String employeeName;
    private String Role;
    private String phone;
    private String email;
    private Double salary;
}
