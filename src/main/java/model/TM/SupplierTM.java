package model.TM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class SupplierTM {
    private Integer supplierId;
    private String supplierName;
    private String ContactPerson;
    private String Phone;
    private String Email;
    private String Address;
}
