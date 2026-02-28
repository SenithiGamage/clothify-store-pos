package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.TM.CartTM;
import service.ServiceFactory;
import service.custom.OrderService;
import service.custom.ProductService;
import util.ServiceType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXComboBox cmbItemCode;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private TableView tblCart;

    @FXML
    private TableColumn colItem;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colPrice;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TextField txtSearchMenu;

    @FXML
    private Label lblSubTotal;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblTax;

    private ObservableList<CartTM> cartObservableList = FXCollections.observableArrayList();

    OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tblCart.setItems(cartObservableList);

        loadItemCodes();

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillItemDetails((String) newValue);
            }
        });

    }

    @FXML
    public void btnAllOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnMenOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnKidsOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnWomenOnAction(ActionEvent actionEvent) {
    }

    private void loadItemCodes() {
        try {
            List<Product> products = productService.getAllProducts();
            ObservableList<String> codes = FXCollections.observableArrayList();
            for (Product p : products) {
                codes.add(p.getProductCode());
            }
            cmbItemCode.setItems(codes);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load product codes: " + e.getMessage()).show();
        }
    }

    private void fillItemDetails(String productCode) {
        try {
            Product product = productService.findByProductCode(productCode);
            if (product != null) {
                txtItemName.setText(product.getProductName());
                txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
                txtStock.setText(String.valueOf(product.getQtyOnHand()));
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error fetching product details: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String itemCode = cmbItemCode.getValue().toString();
        String name = txtItemName.getText();

        if (itemCode == null || txtQty.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an item and enter a quantity!").show();
            return;
        }

        try {
            int qtyToBuy = Integer.parseInt(txtQty.getText());
            int qtyOnHand = Integer.parseInt(txtStock.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());

            if (qtyToBuy > qtyOnHand) {
                new Alert(Alert.AlertType.WARNING, "Not enough stock!").show();
                return;
            }
            if (qtyToBuy <= 0) {
                new Alert(Alert.AlertType.WARNING, "Quantity must be greater than zero!").show();
                return;
            }

            double total = qtyToBuy * unitPrice;

            boolean isAlreadyInCart = false;
            for (CartTM cartItem : cartObservableList) {
                if (cartItem.getItem().equals(itemCode)) {
                    int newQty = cartItem.getQtyOnHand() + qtyToBuy;
                    if (newQty > qtyOnHand) {
                        new Alert(Alert.AlertType.WARNING, "Cannot exceed quantity on hand!").show();
                        return;
                    }
                    cartItem.setQtyOnHand(newQty);
                    cartItem.setTotal(newQty * unitPrice);
                    isAlreadyInCart = true;
                    break;
                }
            }

            if (!isAlreadyInCart) {
                cartObservableList.add(new CartTM(itemCode, qtyToBuy, unitPrice, total));
            }

            tblCart.refresh();
            calNetTotal();

            cmbItemCode.getSelectionModel().clearSelection();
            txtItemName.clear();
            txtUnitPrice.clear();
            txtStock.clear();
            txtQty.clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid number for quantity!").show();
        }
    }

    private void calNetTotal() {
        double total = 0.0;
        for(CartTM cartTM : cartObservableList){
            total += cartTM.getTotal();
        }
        double tax = total * 0.08; // 8% tax
        double subTotal = total - tax;

        lblSubTotal.setText(String.format("%,.2f", subTotal));
        lblTax.setText(String.format("%,.2f", tax));
        lblTotal.setText(String.format("%,.2f", total));
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        if (cartObservableList.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty!").show();
            return;
        }

        try {
            ArrayList<OrderDetail> orderDetailsList = new ArrayList<>();

            for (CartTM cart : cartObservableList) {
                Product product = productService.findByProductCode(cart.getItem());

                orderDetailsList.add(new OrderDetail(
                        0,
                        0,
                        product.getProductId(),
                        cart.getQtyOnHand(),
                        cart.getUnitPrice(),
                        cart.getTotal()
                ));
            }

            OrderDetail[] orderDetailsArray = orderDetailsList.toArray(new OrderDetail[0]);

            Order order = new Order(
                    0,
                    java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
                    "Walk-in Customer",
                    Double.parseDouble(lblTotal.getText().replace(",", "")),
                    "Current Cashier",
                    orderDetailsArray
            );

            if (orderService.placeOrder(order)) {

                for (CartTM cartItem : cartObservableList) {
                    Product product = productService.findByProductCode(cartItem.getItem());

                    int remainingStock = product.getQtyOnHand() - cartItem.getQtyOnHand();

                    productService.updateStock(product.getProductId(), remainingStock);
                }

                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!").show();
                cartObservableList.clear();
                tblCart.refresh();
                calNetTotal();
                btnClearOnAction(null);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to place order!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        cmbItemCode.getSelectionModel().clearSelection();

        txtItemName.clear();
        txtUnitPrice.clear();
        txtStock.clear();
        txtQty.clear();
        txtSearchMenu.clear();
    }
}
