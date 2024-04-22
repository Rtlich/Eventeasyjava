module com.example.eventeasymaven {
    requires javafx.controls;
    requires javafx.fxml;
    requires javax.mail.api;
    requires jbcrypt;
    requires java.sql;

    opens com.example.eventeasymaven to javafx.fxml;
    opens com.example.eventeasymaven.controllers to javafx.fxml;
    opens com.example.eventeasymaven.controllers.forgot_password to javafx.fxml;

    opens com.example.eventeasymaven.controllers.back to javafx.fxml;
    opens com.example.eventeasymaven.controllers.back.user to javafx.fxml;

    opens com.example.eventeasymaven.controllers.front to javafx.fxml;
    opens com.example.eventeasymaven.controllers.front.user to javafx.fxml;

    exports com.example.eventeasymaven;
    exports com.example.eventeasymaven.controllers;
    exports com.example.eventeasymaven.controllers.forgot_password;

    exports com.example.eventeasymaven.controllers.back;
    exports com.example.eventeasymaven.controllers.back.user;

    exports com.example.eventeasymaven.controllers.front;
    exports com.example.eventeasymaven.controllers.front.user;
}