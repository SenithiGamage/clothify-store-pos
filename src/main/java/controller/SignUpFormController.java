package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Employee;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpFormController implements Initializable {

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXComboBox<String> cmbRole;

    EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Cashier", "Manager"));
    }

    @FXML
    public void btnRegisterOnAction(ActionEvent actionEvent) {
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String role = cmbRole.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Username, Password, and Role are mandatory fields!");
            return;
        }

        try {
            Employee employee = new Employee(0, fullName, role, "", email, 0.0, username, password);

            boolean isRegistered = employeeService.saveEmployee(employee);

            if (isRegistered) {
                navigateToLogin();
                showAlert(Alert.AlertType.INFORMATION, "Account created successfully! Please log in.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration failed. Username might already exist.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void lblBackToLoginOnMouseClicked(MouseEvent mouseEvent) {
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login_form.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Clothify Store - Login");
            stage.show();

            ((Stage) txtUsername.getScene().getWindow()).close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Cannot load the Login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }
}