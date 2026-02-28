package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnOrder;

    @FXML
    private JFXButton btnProducts;

    @FXML
    private JFXButton btnSuppliers;

    @FXML
    private JFXButton btnEmployees;

    @FXML
    private JFXButton btnReports;

    @FXML
    private Label lblTime;

    @FXML
    private AnchorPane dashRoot;

    @FXML
    private Label lblDate;

    private Parent homeContent = null;
    private Parent orderContent = null;
    private Parent productsContent = null;
    private Parent suppliersContent = null;
    private Parent employeesContent = null;
    private Parent reportsContent = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
        loadHomeContent();
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        lblDate.setText(sdf.format(date));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblTime.setText(now.format(formatter));
        }), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private Parent loadContent(String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                return null;
            }
            return FXMLLoader.load(resource);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading view: " + fxmlPath).show();
            return null;
        }
    }

    private void setView(Parent content) {
        if (content != null) {
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(content);
        }
    }

    @FXML
    public void btnDashboardOnAction(ActionEvent actionEvent) {
        homeContent = loadContent("/view/home_content.fxml");

        setView(homeContent);
        setActiveButton(btnDashboard);
    }

    @FXML
    public void btnOrderOnAction() {
        if (orderContent == null) {
            orderContent = loadContent("/view/order_form.fxml");
        }
        setView(orderContent);
        setActiveButton(btnOrder);
    }

    @FXML
    public void btnProductsOnAction() {
        if (productsContent == null) {
            productsContent = loadContent("/view/products_form.fxml");
        }
        setView(productsContent);
        setActiveButton(btnProducts);
    }

    @FXML
    public void btnSuppliersOnAction() {
        if (suppliersContent == null) {
            suppliersContent = loadContent("/view/suppliers_form.fxml");
        }
        setView(suppliersContent);
        setActiveButton(btnSuppliers);
    }

    @FXML
    public void btnEmployeesOnAction() {
        if (employeesContent == null) {
            employeesContent = loadContent("/view/employees_form.fxml");
        }
        setView(employeesContent);
        setActiveButton(btnEmployees);
    }

    @FXML
    public void btnReportsOnAction() {
        if (reportsContent == null) {
            reportsContent = loadContent("/view/reports_form.fxml");
        }
        setView(reportsContent);
        setActiveButton(btnReports);
    }

    @FXML
    public void btnLogOutOnAction() {
        try {
            URL resource = getClass().getResource("/view/login_form.fxml");
            if (resource == null) {
                throw new IOException("Cannot find login_form.fxml");
            }
            Parent loginRoot = FXMLLoader.load(resource);
            Stage stage = new Stage();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Clothify Store - Login");
            stage.show();

            ((Stage) dashRoot.getScene().getWindow()).close();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot load login screen: " + e.getMessage()).show();
        }
    }

    private void loadHomeContent() {
        btnDashboardOnAction(null);
    }

    private void setActiveButton(JFXButton activeButton) {
        String defaultStyle = "-fx-background-color: #D9D9D9; -fx-text-fill: #1e0101;";
        String activeStyle = "-fx-background-color: #7D96C0; -fx-text-fill: #1e0101;";

        btnDashboard.setStyle(defaultStyle);
        btnOrder.setStyle(defaultStyle);
        btnProducts.setStyle(defaultStyle);
        btnSuppliers.setStyle(defaultStyle);
        btnEmployees.setStyle(defaultStyle);
        btnReports.setStyle(defaultStyle);

        if (activeButton != null) {
            activeButton.setStyle(activeStyle);
        }
    }

}
