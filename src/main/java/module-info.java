module com.example.eventeasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javax.mail.api;
    requires jbcrypt;
    requires java.sql;

    opens com.example.eventeasy to javafx.fxml;
    opens com.example.eventeasy.entities to javafx.fxml;
    opens com.example.eventeasy.controllers to javafx.fxml;

    opens com.example.eventeasy.controllers.front to javafx.fxml;
    opens com.example.eventeasy.controllers.front.reclamation to javafx.fxml;
    opens com.example.eventeasy.controllers.front.note to javafx.fxml;

    opens com.example.eventeasy.controllers.back to javafx.fxml;
    opens com.example.eventeasy.controllers.back.reclamation to javafx.fxml;
    opens com.example.eventeasy.controllers.back.note to javafx.fxml;

    exports com.example.eventeasy;
    exports com.example.eventeasy.entities;
    exports com.example.eventeasy.controllers;

    exports com.example.eventeasy.controllers.front;
    exports com.example.eventeasy.controllers.front.reclamation;
    exports com.example.eventeasy.controllers.front.note;

    exports com.example.eventeasy.controllers.back;
    exports com.example.eventeasy.controllers.back.reclamation;
    exports com.example.eventeasy.controllers.back.note;
}