module com.example.eventeasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires itextpdf;

    opens com.example.eventeasy to javafx.fxml;
    opens com.example.eventeasy.controllers to javafx.fxml;

    opens com.example.eventeasy.controllers.back to javafx.fxml;
    opens com.example.eventeasy.controllers.back.allocation to javafx.fxml;
    opens com.example.eventeasy.controllers.back.categoryAllocation to javafx.fxml;

    opens com.example.eventeasy.controllers.front to javafx.fxml;
    opens com.example.eventeasy.controllers.front.allocation to javafx.fxml;
    opens com.example.eventeasy.controllers.front.categoryAllocation to javafx.fxml;

    exports com.example.eventeasy;
    exports com.example.eventeasy.controllers;

    exports com.example.eventeasy.controllers.back;
    exports com.example.eventeasy.controllers.back.allocation;
    exports com.example.eventeasy.controllers.back.categoryAllocation;

    exports com.example.eventeasy.controllers.front;
    exports com.example.eventeasy.controllers.front.allocation;
    exports com.example.eventeasy.controllers.front.categoryAllocation;
}