package com.Project.Controllers;

import com.Project.Data.PersistentData;
import com.Project.Data.Product;
import com.Project.Utils.FxmlUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OfferController {

    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private DatePicker fromDatePicker, toDatePicker;
    @FXML
    private TextField discountField;

    @FXML
    public void initialize() {
        populateProductComboBox();
    }

    private void populateProductComboBox() {
        List<Product> productList = PersistentData.getInstance().getProducts();

        List<String> productNames = productList.stream()
                .map(Product::name)
                .toList();

        productComboBox.getItems().addAll(productNames);
    }

    @FXML
    private void OnAddOfferPressed() {
        String product = productComboBox.getValue();
        String fromDate = (fromDatePicker.getValue() != null) ? fromDatePicker.getValue().toString() : null;
        String toDate = (toDatePicker.getValue() != null) ? toDatePicker.getValue().toString() : null;
        String discount = discountField.getText();

        if (product == null || fromDate == null || toDate == null || discount.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled!", AlertType.ERROR);
            return;
        }

        showAlert("Success", "Offer added successfully!", AlertType.INFORMATION);
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void OnLogoutClicked(){
        try {
            PersistentData.getInstance().ClearSeller();
            PersistentData.getInstance().loadProducts(new ArrayList<>());
            PersistentData.getInstance().loadOrders(new ArrayList<>());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) discountField.getScene().getWindow();

            Scene loginScene = new Scene(loginRoot);
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the login screen.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void OnUserClicked() throws IOException {
        Parent sellerPageRoot = FxmlUtils.getInstance().getFxmlLoader("/sellerData.fxml").load();
        Stage stage = (Stage) discountField.getScene().getWindow();
        stage.setScene(new Scene(sellerPageRoot));
        stage.setTitle("Seller Page");
        stage.show();
    }
}
