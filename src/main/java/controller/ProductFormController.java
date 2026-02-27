package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import model.Supplier;
import service.ServiceFactory;
import service.custom.ProductService;
import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    @FXML
    private JFXTextField txtCode;

    @FXML
    private TableColumn colCode;

    @FXML
    private TextField txtSearchMenu;

    @FXML
    private JFXTextField txtProductId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtQuantity;

    @FXML
    private JFXComboBox cmbSupplierId;

    @FXML
    private TableView tblProducts;

    @FXML
    private TableColumn colProductId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colPrice;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colSupplier;

    ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        loadComboBoxes();
        loadTableData();

        tblProducts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields((Product) newValue);
            }
        });
    }

    private void populateFields(Product product) {
        txtProductId.setText(String.valueOf(product.getProductId()));
        txtCode.setText(product.getProductCode());
        txtName.setText(product.getProductName());
        cmbSupplierId.setItems(FXCollections.observableArrayList(product.getSupplierId()));
        txtPrice.setText(product.getUnitPrice().toString());
        txtQuantity.setText(String.valueOf(product.getQtyOnHand()));
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        try {
            Product product = new Product(
                    Integer.parseInt(txtProductId.getText()),
                    txtCode.getText(),
                    txtName.getText(),
                    (Integer) cmbSupplierId.getValue(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQuantity.getText()),
                    "" // Description is missing from FXML, defaulting to empty string
            );

            if (productService.saveProduct(product)) {
                new Alert(Alert.AlertType.INFORMATION, "Product Added Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add product!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Please ensure ID, Price, and Quantity are valid numbers!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            Product product = new Product(
                    Integer.parseInt(txtProductId.getText()),
                    txtCode.getText(),
                    txtName.getText(),
                    (Integer) cmbSupplierId.getValue(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQuantity.getText()),
                    ""
            );

            if (productService.updateProduct(product)) {
                new Alert(Alert.AlertType.INFORMATION, "Product Updated Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update product!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Please ensure ID, Price, and Quantity are valid numbers!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            int productId = Integer.parseInt(txtProductId.getText());

            if (productService.deleteProduct(productId)) {
                new Alert(Alert.AlertType.INFORMATION, "Product Deleted Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete product!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Product ID must be a valid number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        String searchValue = txtSearchMenu.getText();
        if (searchValue != null && !searchValue.trim().isEmpty()) {
            try {
                Product product = null;

                try {
                    int productId = Integer.parseInt(searchValue);
                    product = productService.searchProduct(productId);
                } catch (NumberFormatException e) {
                    product = productService.findByProductCode(searchValue);
                }

                if (product != null) {
                    populateFields(product);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Product not found!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a Product ID or Code to search!").show();
        }
    }

    private void loadTableData() {
        try {
            List<Product> productList = productService.getAllProducts();
            ObservableList<Product> observableList = FXCollections.observableArrayList(productList);
            tblProducts.setItems(observableList);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load table data: " + e.getMessage()).show();
        }
    }

    private void loadComboBoxes() {
        try {
            List<Supplier> supplierList = supplierService.getAllSuppliers();
            ObservableList<Integer> supplierIds = FXCollections.observableArrayList();

            for (Supplier supplier : supplierList) {
                supplierIds.add(supplier.getSupplierId());
            }
            cmbSupplierId.setItems(supplierIds);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load combo box data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtProductId.clear();
        txtCode.clear();
        txtName.clear();
        txtPrice.clear();
        txtQuantity.clear();
        cmbSupplierId.getSelectionModel().clearSelection();
        txtSearchMenu.clear();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }
}
