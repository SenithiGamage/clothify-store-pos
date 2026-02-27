package model.TM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ProductTM {
    private Integer productId;
    private String code;
    private String name;
    private Integer supplierId;
    private Double price;
    private Integer qty;
}
