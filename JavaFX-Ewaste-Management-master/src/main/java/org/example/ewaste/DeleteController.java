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

public class DeleteController {

    @FXML
    private TextField product_id;

    @FXML
    private Button delete_button;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ewaste";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ram@1865";

    @FXML
    public void deleteButtonClicked() {
        String productId = product_id.getText().trim();

        if (productId.isEmpty()) {
            showErrorAlert("Please enter a Product ID.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM products WHERE product_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, productId);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    showInfoAlert("Product deleted successfully!");
                    returnToAdminPortal();
                } else {
                    showErrorAlert("Product with ID " + productId + " not found.");
                }
            }
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
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void returnToAdminPortal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminportal.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Your Application Title");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (window)
            Stage currentStage = (Stage) delete_button.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error loading adminportal.fxml");
        }
    }
}
