package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrderDetail {
    private int detailId;
    private int orderId;
    private int productId;
    private int qty;
    private double unitPrice;
    private double subTotal;
}
