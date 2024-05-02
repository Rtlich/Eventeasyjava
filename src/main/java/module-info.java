module com.example.eventeasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.eventeasy to javafx.fxml;
    opens com.example.eventeasy.entities to javafx.fxml;
    opens com.example.eventeasy.controllers to javafx.fxml;
    opens com.example.eventeasy.controllers.back to javafx.fxml;
    opens com.example.eventeasy.controllers.front to javafx.fxml;
    opens com.example.eventeasy.controllers.front.bookingL to javafx.fxml;
    opens com.example.eventeasy.controllers.front.lieu to javafx.fxml;
    opens com.example.eventeasy.controllers.back.bookingL to javafx.fxml;
    opens com.example.eventeasy.controllers.back.categoryL to javafx.fxml;
    opens com.example.eventeasy.controllers.back.lieu to javafx.fxml;

    exports com.example.eventeasy;
    exports com.example.eventeasy.entities;
    exports com.example.eventeasy.controllers;
    exports com.example.eventeasy.controllers.back;
    exports com.example.eventeasy.controllers.front;
    exports com.example.eventeasy.controllers.front.bookingL;
    exports com.example.eventeasy.controllers.front.lieu;
    exports com.example.eventeasy.controllers.back.bookingL;
    exports com.example.eventeasy.controllers.back.categoryL;
    exports com.example.eventeasy.controllers.back.lieu;
}