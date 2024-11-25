package com.Project.Controllers;

import com.Project.Data.PersistentData;
import com.Project.Data.Product;
import com.Project.Utils.DatabaseUtils;
import com.Project.Utils.FxmlUtils;
import com.Project.Utils.SerializationUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginController {
    @FXML
    private Label lblError;

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    private final String usernameFile = "username.txt";

    @FXML
    private void OnRememberMe() {
        PersistentData.getInstance().rememberMeStatus = !PersistentData.getInstance().rememberMeStatus;
    }
    private int sellerId = 0;


    @FXML
    protected void onLoginButtonClick() {
        String username = txtLogin.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username and Password cannot be empty!");
            lblError.setStyle("-fx-text-fill: red;");
            lblError.setVisible(true);
            return;
        }

        if (validateCredentials(username, password)) {
            lblError.setText("Login successful!");
            lblError.setStyle("-fx-text-fill: green;");
            lblError.setVisible(true);
            try {


                // Loading the login page. Cut down on code duplication. At least this works better than android kotlin.
                Parent sellerPageRoot = FxmlUtils.getInstance().getFxmlLoader("/sellerData.fxml").load();
                Stage stage = (Stage) txtLogin.getScene().getWindow();

                // Have to download the data from offers into the database. Doesn¨t work yet.
                //downloadSellerOffersFromDB(sellerId);
                //downloadProductsNamesFromDb();


                stage.setScene(new Scene(sellerPageRoot));
                stage.setTitle("Seller Page");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                lblError.setText("Error loading seller page.");
                lblError.setStyle("-fx-text-fill: red;");
                lblError.setVisible(true);
            }
        } else {
            lblError.setText("Wrong login or password!");
            lblError.setStyle("-fx-text-fill: red;");
            lblError.setVisible(true);
        }
    }

    public void initialize() {

        String login = Optional.ofNullable(SerializationUtils.DeserializeFile(usernameFile)).orElse("").trim();
        txtLogin.setText(login);
    }

    // I'm seeing at lest a few possible ways to just hack a person with just CIF. But its local project.
    private boolean validateCredentials(String username, String password) {
        String query = "SELECT * FROM Sellers WHERE CIF = ?";
        Connection connection = DatabaseUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("plain_password");
                if (password.equals(storedPassword)) {
                    var seller = PersistentData.getInstance().getSeller();
                    seller.cif = resultSet.getString("CIF");
                    seller.name = resultSet.getString("name");
                    seller.businessName = resultSet.getString("business_name");
                    seller.phone = resultSet.getString("phone");
                    seller.email = resultSet.getString("email");
                    seller.password = resultSet.getString("plain_password");
                    seller.url = resultSet.getString("url");
                    seller.isPro = resultSet.getBoolean("pro");
                    sellerId = resultSet.getInt("seller_id");

                    if (PersistentData.getInstance().rememberMeStatus) {
                        SerializationUtils.SerializeFile(username, usernameFile);
                    } else {
                        SerializationUtils.ClearFile(usernameFile);
                    }
                    return true;
                } else {
                    return false;
                }
            }
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            lblError.setText("Database error. Please try again.");
        }
        return false;
    }

    private void downloadProductsNamesFromDb() {
        String query = "SELECT product_id, product_name FROM Products ORDER BY product_id";

        List<Product> productList = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtils.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                var product = new Product(productId, productName);
                productList.add(product);
            }
            PersistentData.getInstance().loadProducts(productList);
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            lblError.setText("Database error. Please try again.");
        }
    }

    private void downloadSellerOffersFromDB(String username) {
        String query = "SELECT product_id,offer_price,offer_start_date,offer_end_date " + "FROM seller_products " + "WHERE seller_id=? ORDER BY seller_product_id ";

        List<Product> productList = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtils.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                var product = new Product(productId, productName);
                productList.add(product);
            }
            PersistentData.getInstance().loadProducts(productList);
            DatabaseUtils.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            lblError.setText("Database error. Please try again.");
        }
    }
}
