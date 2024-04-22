package com.example.eventeasymaven.controllers.forgot_password;

import com.example.eventeasymaven.MainApp;
import com.example.eventeasymaven.services.UserService;
import com.example.eventeasymaven.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgotThirdController implements Initializable {

    @FXML
    public TextField inputPassword;
    @FXML
    public TextField inputPasswordConfirmation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void backToLogin(ActionEvent actionEvent) {
        MainApp.getInstance().loadLogin();
    }

    @FXML
    public void next(ActionEvent actionEvent) {
        if (inputPassword.getText().isEmpty()) {
            AlertUtils.makeInformation("Mot de passe ne doit pas etre vide");
            return;
        }

        if (inputPasswordConfirmation.getText().isEmpty()) {
            AlertUtils.makeInformation("Confirmation du mot de passe ne doit pas etre vide");
            return;
        }

        if (!inputPassword.getText().equals(inputPasswordConfirmation.getText())) {
            AlertUtils.makeInformation("Mot de passe et confirmation doivent etre identique");
            return;
        }

        if (UserService.getInstance().updatePassword(ForgotFirstController.email, inputPassword.getText())) {
            AlertUtils.makeInformation("Mot de passe modifié avec success");
            MainApp.getInstance().loadLogin();
        } else {
            AlertUtils.makeError("Error");
        }
    }
}
