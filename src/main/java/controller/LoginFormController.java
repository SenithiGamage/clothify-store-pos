package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Employee;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    @FXML
    private Label lblForgotyourpassword;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private Label lblSignUp;

    EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (txtUsername != null) {
            txtUsername.requestFocus();
        }
    }

    @FXML
    public void btnLogInOnAction(ActionEvent actionEvent) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill all fields");
            return;
        }

        try {
            Employee employee = employeeService.login(username, password);

            if (employee != null) {
                String targetFxml = "Admin".equals(employee.getRole()) || "Manager".equals(employee.getRole())
                        ? "/view/dashboard.fxml"
                        : "/view/order_form.fxml";

                loadNewWindow(targetFxml, "Clothify Store - " + employee.getRole());
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid username or password.");
            }
        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadNewWindow(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();

        ((Stage) txtUsername.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    public void lblSignUpOnMouseClicked(MouseEvent mouseEvent) {
        try {
            loadNewWindow("/view/signup_form.fxml", "Clothify Store - Sign Up");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Cannot load the Sign Up page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
