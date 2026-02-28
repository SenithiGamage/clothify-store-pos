package model.TM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CartTM {
    private String item;
    private Integer qtyOnHand;
    private Double unitPrice;
    private Double total;
}
