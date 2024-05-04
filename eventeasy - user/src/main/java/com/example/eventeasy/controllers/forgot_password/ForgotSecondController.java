package com.example.eventeasy.controllers.forgot_password;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgotSecondController implements Initializable {

    static int resetCode;

    @FXML
    public TextField inputResetCode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void backToLogin(ActionEvent actionEvent) {
        MainApp.getInstance().loadLogin();
    }

    @FXML
    public void next(ActionEvent actionEvent) {
        if (inputResetCode.getText().isEmpty()) {
            AlertUtils.makeInformation("Code ne doit pas etre vide");
        } else {
            if (inputResetCode.getText().equalsIgnoreCase(String.valueOf(resetCode))) {
                MainApp.getInstance().loadForgotThird();
            } else {
                AlertUtils.makeInformation("Code incorrect");
            }
        }
    }
}
