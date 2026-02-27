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
import model.Employee;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colPhone;

    @FXML
    private TableColumn colSalary;

    @FXML
    private JFXTextField txtPhone;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtRole;

    @FXML
    private TextField txtSearchMenu;

    @FXML
    private JFXTextField txtEmployeeId;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private TableView tblEmployees;

    @FXML
    private TableColumn colEmployeeId;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colRole;

    EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        loadTableData();

        tblEmployees.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields((Employee) newValue);
            }
        });
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        try {
            Employee employee = new Employee(
                    0,
                    txtName.getText(),
                    txtRole.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    Double.parseDouble(txtSalary.getText()),
                    txtUsername.getText(),
                    txtPassword.getText()
            );
            
            boolean isAdded = employeeService.saveEmployee(employee);

            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Employee Added Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add employee!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Employee ID must be a valid number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void loadTableData() {
        try {
            List<Employee> employeeList = employeeService.getAllEmployees();
            ObservableList<Employee> observableList = FXCollections.observableArrayList(employeeList);
            tblEmployees.setItems(observableList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load table data: " + e.getMessage()).show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            int empId = Integer.parseInt(txtEmployeeId.getText());

            Employee existingEmployee = employeeService.searchEmployee(empId);

            if (existingEmployee == null) {
                new Alert(Alert.AlertType.WARNING, "Employee not found!").show();
                return;
            }

            Employee updatedEmployee = new Employee(
                    empId,
                    txtName.getText(),
                    txtRole.getText(),
                    txtPhone.getText(),
                    txtEmail.getText(),
                    Double.parseDouble(txtSalary.getText()),
                    txtUsername.getText(),
                    txtPassword.getText()
            );

            boolean isUpdated = employeeService.updateEmployee(updatedEmployee);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Employee Updated Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update employee!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Employee ID and Salary must be valid numbers!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            int employeeId = Integer.parseInt(txtEmployeeId.getText());
            boolean isDeleted = employeeService.deleteEmployee(employeeId);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Employee Deleted Successfully!").show();
                loadTableData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete employee!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Employee ID must be a valid number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        String searchValue = txtSearchMenu.getText();

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            try {
                int employeeId = Integer.parseInt(searchValue);
                Employee employee = employeeService.searchEmployee(employeeId);

                if (employee != null) {
                    populateFields(employee);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Employee not found!").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Search value must be a valid Employee ID number!").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter an Employee ID to search!").show();
        }
    }

    private void clearFields() {
        txtEmployeeId.clear();
        txtName.clear();
        txtRole.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtSalary.clear();
        txtUsername.clear();
        txtPassword.clear();
        txtSearchMenu.clear();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void populateFields(Employee employee) {
        txtEmployeeId.setText(String.valueOf(employee.getEmployeeId()));
        txtName.setText(employee.getEmployeeName());
        txtRole.setText(employee.getRole());
        txtPhone.setText(employee.getPhone());
        txtEmail.setText(employee.getEmail());
        txtSalary.setText(String.valueOf(employee.getSalary()));
        txtUsername.setText(employee.getUsername());
        txtPassword.setText(employee.getPassword());
    }
}
