module com.mycompany.polloshermanos {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires java.base;

    // App principal
    opens com.mycompany.polloshermanos to javafx.fxml;

    // Controllers (FXMLLoader)
    opens com.mycompany.polloshermanos.controllers to javafx.fxml;

    // Objetos / modelos para tablas JavaFX
    opens com.mycompany.polloshermanos.objects to javafx.base;

    exports com.mycompany.polloshermanos;
    exports com.mycompany.polloshermanos.controllers;
    exports com.mycompany.polloshermanos.objects;
}