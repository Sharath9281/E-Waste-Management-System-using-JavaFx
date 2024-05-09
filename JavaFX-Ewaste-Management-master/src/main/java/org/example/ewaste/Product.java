package org.example.ewaste;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {

    private final StringProperty productId;
    private final StringProperty productName;
    private final StringProperty productModel;
    private final StringProperty damagedParts;
    private final StringProperty amount;

    public Product(String productId, String productName, String productModel, String damagedParts, String amount) {
        this.productId = new SimpleStringProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.productModel = new SimpleStringProperty(productModel);
        this.damagedParts = new SimpleStringProperty(damagedParts);
        this.amount = new SimpleStringProperty(amount);
    }

    public String getProductId() {
        return productId.get();
    }

    public StringProperty productIdProperty() {
        return productId;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public String getProductModel() {
        return productModel.get();
    }

    public StringProperty productModelProperty() {
        return productModel;
    }

    public String getDamagedParts() {
        return damagedParts.get();
    }

    public StringProperty damagedPartsProperty() {
        return damagedParts;
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }
}
