module com.example.eventeasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.sql;
    requires simplecaptcha;
    requires javafx.swing;
    requires java.mail;

    opens com.example.eventeasy to javafx.fxml;
    opens com.example.eventeasy.entities to javafx.fxml;
    opens com.example.eventeasy.controllers to javafx.fxml;
    opens com.example.eventeasy.controllers.forgot_password to javafx.fxml;

    opens com.example.eventeasy.controllers.back to javafx.fxml;
    opens com.example.eventeasy.controllers.back.user to javafx.fxml;

    opens com.example.eventeasy.controllers.front to javafx.fxml;
    opens com.example.eventeasy.controllers.front.user to javafx.fxml;

    exports com.example.eventeasy;
    exports com.example.eventeasy.entities;
    exports com.example.eventeasy.controllers;
    exports com.example.eventeasy.controllers.forgot_password;

    exports com.example.eventeasy.controllers.back;
    exports com.example.eventeasy.controllers.back.user;

    exports com.example.eventeasy.controllers.front;
    exports com.example.eventeasy.controllers.front.user;
}