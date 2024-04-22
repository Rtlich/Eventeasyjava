package com.example.eventeasymaven.controllers.front.user;

import com.example.eventeasymaven.MainApp;
import com.example.eventeasymaven.entities.User;
import com.example.eventeasymaven.entities.UserRole;
import com.example.eventeasymaven.controllers.front.MainWindowController;
import com.example.eventeasymaven.services.UserService;
import com.example.eventeasymaven.utils.AlertUtils;
import com.example.eventeasymaven.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EditFieldController implements Initializable {

    @FXML
    public TextField textField;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentUser = MainApp.getSession();

        topText.setText("Modifier mon profil");
        btnAjout.setText("Modifier");

        try {
            switch (MyProfileController.fieldToEdit) {
                case "email":
                    topText.setText("Modifier email");
                    textField.setPromptText("Email");
                    textField.setText(currentUser.getEmail());
                    break;
                case "fname":
                    topText.setText("Modifier first name");
                    textField.setPromptText("First name");
                    textField.setText(currentUser.getFname());
                    break;
                case "lname":
                    topText.setText("Modifier last name");
                    textField.setPromptText("Last name");
                    textField.setText(currentUser.getLname());
                    break;
                case "phonenumber":
                    topText.setText("Modifier phone number");
                    textField.setPromptText("Phone number");
                    textField.setText(String.valueOf(currentUser.getPhonenumber()));
                    break;
            }
        } catch (NullPointerException ignored) {
            System.out.println("NullPointerException");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            User user = new User(
                    currentUser.getEmail(),
                    currentUser.getPassword(),
                    currentUser.getFname(),
                    currentUser.getLname(),
                    currentUser.getPhonenumber(),
                    UserRole.User,
                    true
            );

            switch (MyProfileController.fieldToEdit) {
                case "email":
                    user.setEmail(textField.getText());
                    break;
                case "fname":
                    user.setFname(textField.getText());
                    break;
                case "lname":
                    user.setLname(textField.getText());
                    break;
                case "phonenumber":
                    user.setPhonenumber(Integer.parseInt(textField.getText()));
                    break;
            }

            user.setId(currentUser.getId());
            if (UserService.getInstance().edit(user)) {
                MainApp.setSession(user);
                AlertUtils.makeSuccessNotification("Profile modifié avec succés");
            } else {
                AlertUtils.makeError("Could not edit user");
            }


            MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MY_PROFILE);
        }
    }


    private boolean controleDeSaisie() {
        switch (MyProfileController.fieldToEdit) {
            case "email":
                if (!Pattern.compile("^(.+)@(.+)$").matcher(textField.getText()).matches()) {
                    AlertUtils.makeInformation("Email invalide");
                    return false;
                }
                break;
            case "fname":
                if (textField.getText().isEmpty()) {
                    AlertUtils.makeError("First name invalide");
                    return false;
                }
                break;
            case "lname":
                if (textField.getText().isEmpty()) {
                    AlertUtils.makeError("Last name invalide");
                    return false;
                }
                break;
            case "phonenumber":
                if (textField.getText().length() != 8) {
                    AlertUtils.makeError("Phone number invalide");
                    return false;
                }

                if (textField.getText().isEmpty()) {
                    AlertUtils.makeError("Phone number invalide");
                    return false;
                }

                try {
                    Integer.parseInt(textField.getText());
                } catch (NumberFormatException ignored) {
                    AlertUtils.makeInformation("Telephone doit etre un nombre");
                    return false;
                }

                break;
        }
        return true;
    }
}