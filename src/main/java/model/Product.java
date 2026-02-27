package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Product {
    private int productId;
    private String productCode;
    private String productName;
    private int supplierId;
    private Double unitPrice;
    private int qtyOnHand;
    private String description;
}
