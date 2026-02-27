package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import service.ServiceFactory;
import service.custom.OrderService;
import service.custom.ProductService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeContentController implements Initializable {

    @FXML
    private Label lblSalesToday;

    @FXML
    private Label lblTotalOrders;

    @FXML
    private Label lblItemsLow;

    OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        try {
            double salesToday = orderService.getTodaySales();
            int totalOrders = orderService.getTotalOrders();
            int itemsLow = productService.getLowStockCount(10);

            lblSalesToday.setText(String.format("%,.2f", salesToday));
            lblTotalOrders.setText(String.valueOf(totalOrders));
            lblItemsLow.setText(String.valueOf(itemsLow));

        } catch (Exception e) {
            System.err.println("Failed to load dashboard stats: " + e.getMessage());
            e.printStackTrace();
        }
    }
}