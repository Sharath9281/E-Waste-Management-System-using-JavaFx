package org.example.ewaste;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminportalController {

    @FXML
    private TextField search;

    @FXML
    private Button update_button;

    @FXML
    private Button insert_button;

    @FXML
    private Button delete_button;

    @FXML
    private Button cancel_button;

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

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initializeTable();
        loadProductData();
    }

    @FXML
    public void updateButtonClicked() {
        loadFXML("update.fxml");
    }

    @FXML
    public void insertButtonClicked() {
        loadFXML("insert.fxml");
    }

    @FXML
    public void deleteButtonClicked() {
        loadFXML("delete.fxml");
    }

    @FXML
    public void cancelButtonClicked() {
        loadFXML("Login.fxml");
    }

    private void initializeTable() {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productModelColumn.setCellValueFactory(new PropertyValueFactory<>("productModel"));
        damagedPartsColumn.setCellValueFactory(new PropertyValueFactory<>("damagedParts"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        view.setItems(productList);
    }


    private void loadProductData() {
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
                        productList.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database error. Please try again.");
        }
    }

    private void loadFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Your Application Title");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error loading " + fxmlFileName);
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
