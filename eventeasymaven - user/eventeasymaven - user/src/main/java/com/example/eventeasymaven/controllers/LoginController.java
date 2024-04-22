package com.example.eventeasymaven.controllers;

import com.example.eventeasymaven.MainApp;
import com.example.eventeasymaven.entities.User;
import com.example.eventeasymaven.services.UserService;
import com.example.eventeasymaven.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField inputEmail;
    @FXML
    public PasswordField inputPassword;
    @FXML
    public Button btnSignup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    public void signUp(ActionEvent ignored) {
        MainApp.getInstance().loadSignup();
    }

    public void login(ActionEvent ignored) {
        User user = UserService.getInstance().checkUser(inputEmail.getText(), inputPassword.getText());

        if (user != null) {
            if (user.getEnabled()) {
                MainApp.getInstance().login(user);
            } else {
                AlertUtils.makeError("Votre compte est bloqué");
            }
        } else {
            AlertUtils.makeError("Identifiants invalides");
        }
    }

    @FXML
    public void forgotPassword(ActionEvent actionEvent) {
        MainApp.getInstance().loadForgotFirst();
    }
}
