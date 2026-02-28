package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Order {
    private int orderId;
    private Timestamp orderDate;
    private String customerName;
    private Double totalAmount;
    private String cashier;
    private OrderDetail[] orderDetails;
}
