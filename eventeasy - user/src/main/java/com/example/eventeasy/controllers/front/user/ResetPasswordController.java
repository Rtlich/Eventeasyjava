package com.example.eventeasy.controllers.front.user;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.services.UserService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {

    @FXML
    public TextField oldPasswordTF;

    @FXML
    public TextField newPasswordTF;

    User currentUser;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentUser = MainApp.getSession();
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            if (UserService.getInstance().checkUser(currentUser.getEmail(), oldPasswordTF.getText()) != null) {
                if (UserService.getInstance().checkUser(currentUser.getEmail(), newPasswordTF.getText()) != null) {
                    AlertUtils.makeError("New password should be different from the old one");
                    return;
                } else {
                    currentUser.setPassword(newPasswordTF.getText());

                    UserService.getInstance().updatePassword(currentUser.getEmail(), newPasswordTF.getText());

                    MainApp.setSession(currentUser);
                    AlertUtils.makeSuccessNotification("Mot de passe modifié avec succés");
                }
            } else {
                AlertUtils.makeError("Old password incorrect");
                return;
            }


            MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MY_PROFILE);
        }
    }


    private boolean controleDeSaisie() {
        if (newPasswordTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Password ne doit pas etre vide");
            return false;
        }

        if (oldPasswordTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Password ne doit pas etre vide");
            return false;
        }

        return true;
    }
}