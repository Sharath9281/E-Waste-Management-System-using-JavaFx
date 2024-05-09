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

public class RegisterController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField retype_password;

    @FXML
    private Button register_button;

    @FXML
    private Button login_button;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ewaste";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ram@1865";

    // Reference to the current stage
    private Stage currentStage;

    public void setCurrentStage(Stage stage) {
        this.currentStage = stage;
    }

    @FXML
    public void registerButtonClicked() {
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

            // Check if the username already exists in the database
            if (isUsernameExists(username.getText())) {
                showErrorAlert("Username already exists. Please choose a different username.");
                return;
            }

            // Insert new user into the database
            insertUser(username.getText(), password.getText(), retype_password.getText());

            // Registration successful, show alert and load login.fxml
            showInfoAlert("Registration successful. Please log in.");
            loadFXML("login.fxml");
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database error. Please try again.");
        }
    }

    @FXML
    public void loginButtonClicked() {
        // Redirect to login.fxml when the Log In button is clicked
        loadFXML("login.fxml");
        // Close the current stage
        if (currentStage != null) {
            currentStage.close();
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

    // Helper method to check if the username already exists in the database
    private boolean isUsernameExists(String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM user_credentials WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // If the result set has at least one row, username already exists
                }
            }
        }
    }

    // Helper method to insert a new user into the database
    private void insertUser(String username, String password, String retype_password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO user_credentials (username, password, retype_password) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, retype_password);
                statement.executeUpdate(); // Execute the insert query
            }
        }
    }
}
