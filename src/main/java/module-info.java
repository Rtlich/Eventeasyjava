module com.example.eventeasymaven {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.eventeasymaven to javafx.fxml;
    opens com.example.eventeasymaven.controllers to javafx.fxml;

    opens com.example.eventeasymaven.controllers.back to javafx.fxml;
    opens com.example.eventeasymaven.controllers.back.allocation to javafx.fxml;
    opens com.example.eventeasymaven.controllers.back.categoryAllocation to javafx.fxml;

    opens com.example.eventeasymaven.controllers.front to javafx.fxml;
    opens com.example.eventeasymaven.controllers.front.allocation to javafx.fxml;

    exports com.example.eventeasymaven;
    exports com.example.eventeasymaven.controllers;

    exports com.example.eventeasymaven.controllers.back;
    exports com.example.eventeasymaven.controllers.back.allocation;
    exports com.example.eventeasymaven.controllers.back.categoryAllocation;

    exports com.example.eventeasymaven.controllers.front;
    exports com.example.eventeasymaven.controllers.front.allocation;
}