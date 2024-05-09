package org.example.ewaste;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserportalController implements Initializable {

    @FXML
    private Button submit_button;

    @FXML
    private TableView<Product> view;

    @FXML
    private TableColumn<Product, String> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> productModelColumn;

    @FXML
    private TableColumn<Product, String> damagedPartsColumn;

    @FXML
    private TableColumn<Product, String> amountColumn;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ewaste";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ram@1865";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        fetchDataFromDatabase();
    }

    private void initializeTable() {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productModelColumn.setCellValueFactory(new PropertyValueFactory<>("productModel"));
        damagedPartsColumn.setCellValueFactory(new PropertyValueFactory<>("damagedParts"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }


    private void fetchDataFromDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM products";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Product product = new Product(
                                resultSet.getString("product_id"),
                                resultSet.getString("product_name"),
                                resultSet.getString("product_model"),
                                resultSet.getString("product_damage"),
                                resultSet.getString("product_amount")
                        );
                        view.getItems().add(product);
                    }
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

    @FXML
    private void handleSubmitButton() {
        // Show a message when submit is clicked
        showAlert("Submit Button Clicked", "Thank you for using the application!");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
