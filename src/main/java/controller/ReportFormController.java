package controller;

import com.jfoenix.controls.JFXComboBox;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ReportFormController implements Initializable {

    @FXML
    private JFXComboBox cmbReportType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbReportType.setItems(FXCollections.observableArrayList(
                "Employee Report",
                "Supplier Report",
                "Product Stock Report"
        ));
    }

    @FXML
    public void btnGenerateReportOnAction(ActionEvent actionEvent) {
        Object selectedValue = cmbReportType.getValue();
        if (selectedValue == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a report type first!").show();
            return;
        }

        String selectedReport = selectedValue.toString();

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            InputStream reportStream = null;

            switch (selectedReport) {
                case "Employee Report":
                    reportStream = getClass().getResourceAsStream("/reports/employee-report.jrxml");
                    break;
                case "Supplier Report":
                    reportStream = getClass().getResourceAsStream("/reports/supplier-report.jrxml");
                    break;
                case "Product Stock Report":
                    reportStream = getClass().getResourceAsStream("/reports/product-report.jrxml");
                    break;
            }

            if (reportStream == null) {
                new Alert(Alert.AlertType.ERROR, "Report template not found! Make sure it is inside the resources/reports folder.").show();
                return;
            }

            JasperDesign design = JRXmlLoader.load(reportStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error generating report: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }
}