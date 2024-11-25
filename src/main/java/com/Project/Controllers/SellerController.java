package com.Project.Controllers;

import com.Project.Data.PersistentData;
import com.Project.Utils.DatabaseUtils;
import com.Project.Utils.EncryptionUtils;
import com.Project.Utils.FxmlUtils;
import com.Project.Utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SellerController {

    @FXML
    private TextField cifField, nameField, businessNameField, phoneField, emailField, passwordField;

    public void initialize() {
        var seller = PersistentData.getInstance().getSeller();

        if (seller != null) {
            cifField.setText(seller.cif);
            nameField.setText(seller.name);
            businessNameField.setText(seller.businessName);
            phoneField.setText(seller.phone);
            emailField.setText(seller.email);
            passwordField.setText("");
        }
    }


    @FXML
    private void onSubmitButtonPressed() {

        String name = Optional.ofNullable(nameField.getText()).orElse("").trim();
        String businessName = Optional.ofNullable(businessNameField.getText()).orElse("").trim();
        String phone = Optional.ofNullable(phoneField.getText()).orElse("").trim();
        String email = Optional.ofNullable(emailField.getText()).orElse("").trim();
        String password = Optional.ofNullable(passwordField.getText()).orElse("").trim();

        if (name.isEmpty() || businessName.isEmpty() || phone.isEmpty()) {
            showAlert("Validation Error", "Name, Business Name, Phone, and Email are required!", AlertType.ERROR);
            return;
        }

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        var seller = PersistentData.getInstance().getSeller();

        if (!name.equals(seller.name) && ValidationUtils.isValidName(name)) {
            fieldsToUpdate.put("name", name);
            seller.name = name;
        }

        if (!businessName.equals(seller.businessName)) {
            fieldsToUpdate.put("business_name", businessName);
            seller.businessName = businessName;
        }

        if (!phone.equals(seller.phone) && ValidationUtils.isValidPhone(phone)) {
            fieldsToUpdate.put("phone", phone);
            seller.phone = phone;
        }

        if (!email.isEmpty() && !email.equals(seller.email) && ValidationUtils.isValidEmail(email)) {
            fieldsToUpdate.put("email", email);
            seller.email = email;
        }

        if (!password.isEmpty()) {
            if (!password.equals(seller.password)) {
                String hashedEnteredPassword = EncryptionUtils.hashWithMD5(password);
                fieldsToUpdate.put("password", hashedEnteredPassword);
                fieldsToUpdate.put("plain_password", password);
                seller.password = password;
            }
        }

        if (!fieldsToUpdate.isEmpty()) {
            UpdateSeller(seller.cif, fieldsToUpdate);
            showAlert("Success", "Seller data saved successfully!", AlertType.INFORMATION);
        } else {
            showAlert("Information", "No changes detected.", AlertType.INFORMATION);
        }

        passwordField.clear();
    }

    public void UpdateSeller(String sellerId, Map<String, Object> fieldsToUpdate) {
        Connection connection = null;
        try {
            connection = DatabaseUtils.getConnection();
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder("UPDATE sellers SET ");
            List<Object> parameters = new ArrayList<>();

            fieldsToUpdate.forEach((field, value) -> {
                sql.append(field).append(" = ?, ");
                parameters.add(value);
            });

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE CIF = ?");
            parameters.add(sellerId);

            try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
                for (int i = 0; i < parameters.size(); i++) {
                    stmt.setObject(i + 1, parameters.get(i));
                }
                stmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.err.println("Transaction rolled back due to an error.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore default state
                    DatabaseUtils.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    @FXML
    private void OnLogoutClicked() {
        try {
            PersistentData.getInstance().ClearSeller();
            PersistentData.getInstance().loadProducts(new ArrayList<>());
            PersistentData.getInstance().loadOrders(new ArrayList<>());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) cifField.getScene().getWindow();

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
    private void OnAddOfferClicked() throws IOException {

        Parent offerRoot = FxmlUtils.getInstance().getFxmlLoader("/addOffer.fxml").load();
        Stage stage = (Stage) cifField.getScene().getWindow();

        Scene offerScene = new Scene(offerRoot);
        stage.setScene(offerScene);
        stage.setTitle("Offers");
        stage.show();
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
