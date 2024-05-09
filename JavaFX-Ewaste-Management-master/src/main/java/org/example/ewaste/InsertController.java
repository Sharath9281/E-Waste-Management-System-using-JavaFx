package org.example.ewaste;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertController {

    @FXML
    private TextField product_name;

    @FXML
    private TextField product_model;

    @FXML
    private TextField product_damage;

    @FXML
    private TextField product_amount;

    @FXML
    private Button insert_button;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ewaste";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ram@1865";

    @FXML
    public void insertButtonClicked() {
        try {
            // Check if all fields are filled
            if (product_name.getText().isEmpty() || product_model.getText().isEmpty()
                    || product_damage.getText().isEmpty() || product_amount.getText().isEmpty()) {
                showErrorAlert("All fields must be filled.");
                return;
            }

            // Insert new product into the database
            insertProduct(product_name.getText(), product_model.getText(),
                    product_damage.getText(), product_amount.getText());

            // Insert successful, show alert and return to adminportal.fxml
            showInfoAlert("Product inserted successfully.");
            loadFXML("adminportal.fxml");
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database error. Please try again.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sign In");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error loading " + fxmlFileName);
        }
    }

    // Helper method to insert a new product into the database
    private void insertProduct(String productName, String productModel,
                               String productDamage, String productAmount) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO products (product_name, product_model, product_damage, product_amount) " +
                    "VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, productName);
                statement.setString(2, productModel);
                statement.setString(3, productDamage);
                statement.setString(4, productAmount);
                statement.executeUpdate(); // Execute the insert query
            }
        }
    }
}
