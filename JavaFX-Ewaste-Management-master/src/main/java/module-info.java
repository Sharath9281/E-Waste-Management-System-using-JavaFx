module org.example.ewaste {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.ewaste to javafx.fxml;
    exports org.example.ewaste;
}