package com.example.eventeasy.controllers.front.user;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.entities.UserRole;
import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.services.UserService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EditProfileController implements Initializable {

    @FXML
    public TextField emailTF;
    @FXML
    public TextField fnameTF;
    @FXML
    public TextField lnameTF;
    @FXML
    public TextField phonenumberTF;
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
            emailTF.setText(currentUser.getEmail());

            fnameTF.setText(currentUser.getFname());
            lnameTF.setText(currentUser.getLname());
            phonenumberTF.setText(String.valueOf(currentUser.getPhonenumber()));

        } catch (NullPointerException ignored) {
            System.out.println("NullPointerException");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            User user = new User(
                    emailTF.getText(), currentUser.getPassword(),
                    fnameTF.getText(),
                    lnameTF.getText(),
                    Integer.parseInt(phonenumberTF.getText()),
                    UserRole.User,
                    true
            );

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
        if (emailTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Email ne doit pas etre vide");
            return false;
        }
        if (!Pattern.compile("^(.+)@(.+)$").matcher(emailTF.getText()).matches()) {
            AlertUtils.makeInformation("Email invalide");
            return false;
        }

        if (fnameTF.getText().isEmpty()) {
            AlertUtils.makeInformation("First name ne doit pas etre vide");
            return false;
        }

        if (lnameTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Last name ne doit pas etre vide");
            return false;
        }

        if (phonenumberTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Telephone ne doit pas etre vide");
            return false;
        }

        if (phonenumberTF.getText().length() != 8) {
            AlertUtils.makeInformation("Telephone doit avoir 8 chiffres");
            return false;
        }

        try {
            Integer.parseInt(phonenumberTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("Telephone doit etre un nombre");
            return false;
        }

        return true;
    }
}