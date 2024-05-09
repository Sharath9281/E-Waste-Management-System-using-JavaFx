package org.example.ewaste;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField retype_password;

    @FXML
    private Button login_button;

    @FXML
    private Button signin_button;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ewaste";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ram@1865";

    @FXML
    public void switchadminlogintoadminport() {
        try {
            // Check if all fields are filled
            if (username.getText().isEmpty() || password.getText().isEmpty() || retype_password.getText().isEmpty()) {
                showErrorAlert("All fields must be filled.");
                return;
            }

            // Check if password and retype password match
            if (!password.getText().equals(retype_password.getText())) {
                showErrorAlert("Password and retype password do not match.");
                return;
            }

            // Check if username ends with "@srp.in" to redirect to admin portal
            if (username.getText().endsWith("@srp.in")) {
                // Additional logic for admin login (e.g., redirect to admin portal)
                if (isValidAdminLogin(username.getText(), password.getText())) {
                    // Admin login successful, load adminportal.fxml
                    loadFXML("adminportal.fxml");
                } else {
                    showErrorAlert("Invalid admin credentials");
                }
            } else {
                // Normal user login logic
                if (isValidUserLogin(username.getText(), password.getText())) {
                    // User login successful, load userportal.fxml
                    loadFXML("userportal.fxml");
                } else {
                    showErrorAlert("Invalid user credentials");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database error. Please try again.");
        }
    }

    @FXML
    public void signinButtonClicked() {
        // Redirect to register.fxml when the Sign In button is clicked
        loadFXML("register.fxml");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Your Application Title");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) login_button.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error loading " + fxmlFileName);
        }
    }

    // Helper method to check admin credentials in the database
    private boolean isValidAdminLogin(String username, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM user_credentials WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // If the result set has at least one row, credentials are valid
                }
            }
        }
    }

    // Helper method to check user credentials in the database
    private boolean isValidUserLogin(String username, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM user_credentials WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // If the result set has at least one row, credentials are valid
                }
            }
        }
    }
}
