package controller;

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
import model.Supplier;
import model.TM.SupplierTM;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    @FXML
    private JFXTextField txtPhoneNo;

    @FXML
    private TableColumn colContactPerson;

    @FXML
    private TableColumn colPhoneNo;

    @FXML
    private TableColumn colAddress;

    @FXML
    private JFXTextField txtContactPerson;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private TableColumn colSupplierName;

    @FXML
    private TextField txtSearchMenu;

    @FXML
    private JFXTextField txtSupplierId;

    @FXML
    private TableView tblSuppliers;

    @FXML
    private TableColumn colSupplierId;

    @FXML
    private TableColumn colEmail;

    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colContactPerson.setCellValueFactory(new PropertyValueFactory<>("ContactPerson"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));

        loadTableData();

        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields((Supplier) newValue);
            }
        });
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        try {
            Supplier supplier = new Supplier(
                    Integer.parseInt(txtSupplierId.getText()),
                    txtName.getText(),
                    txtContactPerson.getText(),
                    txtPhoneNo.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );

            if (supplierService.saveSupplier(supplier)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Added Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add supplier!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Supplier ID must be a valid number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            Supplier supplier = new Supplier(
                    Integer.parseInt(txtSupplierId.getText()),
                    txtName.getText(),
                    txtContactPerson.getText(),
                    txtPhoneNo.getText(),
                    txtEmail.getText(),
                    txtAddress.getText()
            );

            if (supplierService.updateSupplier(supplier)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update supplier!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Supplier ID must be a valid number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            int supplierId = Integer.parseInt(txtSupplierId.getText());

            if (supplierService.deleteSupplier(supplierId)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete supplier!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Supplier ID must be a valid number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        String searchValue = txtSearchMenu.getText();
        if (searchValue != null && !searchValue.trim().isEmpty()) {
            try {
                int supplierId = Integer.parseInt(searchValue);
                Supplier supplier = supplierService.searchSupplier(supplierId);

                if (supplier != null) {
                    populateFields(supplier);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Supplier not found!").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Search value must be a valid Supplier ID!").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a Supplier ID to search!").show();
        }
    }

    private void loadTableData() {
        try {
            List<Supplier> supplierList = supplierService.getAllSuppliers();
            List<SupplierTM> supplierTMList = new ArrayList<>();

            for (Supplier s : supplierList) {
                supplierTMList.add(new SupplierTM(
                        s.getSupplierId(),
                        s.getSupplierName(),
                        s.getContactPerson(),
                        s.getPhone(),
                        s.getEmail(),
                        s.getAddress()
                ));
            }

            ObservableList<SupplierTM> observableList = FXCollections.observableArrayList(supplierTMList);
            tblSuppliers.setItems(observableList);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load table data: " + e.getMessage()).show();
        }
    }

    private void populateFields(Supplier supplier) {
        txtSupplierId.setText(String.valueOf(supplier.getSupplierId()));
        txtName.setText(supplier.getSupplierName());
        txtContactPerson.setText(supplier.getContactPerson());
        txtPhoneNo.setText(supplier.getPhone());
        txtEmail.setText(supplier.getEmail());
        txtAddress.setText(supplier.getAddress());
    }

    private void clearFields() {
        txtSupplierId.clear();
        txtName.clear();
        txtContactPerson.clear();
        txtPhoneNo.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtSearchMenu.clear();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }
}
