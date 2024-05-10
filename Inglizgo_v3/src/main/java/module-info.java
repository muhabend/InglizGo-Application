module com.example.inglizgo_v3 {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
     requires com.dlsc.formsfx;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires javafx.swing;


// Add FontAwesomeFX as an automatic module
    requires fontawesomefx;
    // Adjust the module name if different


    opens com.example.inglizgo_v3 to javafx.fxml;
    exports com.example.inglizgo_v3;
}