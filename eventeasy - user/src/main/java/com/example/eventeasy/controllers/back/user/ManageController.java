package com.example.eventeasy.controllers.back.user;


import com.example.eventeasy.entities.User;
import com.example.eventeasy.entities.UserRole;
import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.services.UserService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public TextField emailTF;
    @FXML
    public TextField passwordTF;
    @FXML
    public TextField fnameTF;
    @FXML
    public TextField lnameTF;
    @FXML
    public TextField phonenumberTF;
    @FXML
    public ComboBox<String> roleCB;
    @FXML
    public CheckBox enabledCB;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;
    @FXML
    public VBox passwordHbox;

    User currentUser;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentUser = ShowAllController.currentUser;

        roleCB.getItems().add("User");
        roleCB.getItems().add("Admin");

        if (currentUser != null) {
            topText.setText("Modifier user");
            btnAjout.setText("Modifier");

            try {
                emailTF.setText(currentUser.getEmail());
                passwordHbox.setVisible(false);
                fnameTF.setText(currentUser.getFname());
                lnameTF.setText(currentUser.getLname());
                phonenumberTF.setText(String.valueOf(currentUser.getPhonenumber()));
                roleCB.setValue(currentUser.getRole().equals(UserRole.Admin) ? "Admin" : "User");
                enabledCB.setSelected(currentUser.getEnabled());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter user");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            User user = new User(
                    emailTF.getText(),
                    currentUser == null ? passwordTF.getText() : "",
                    fnameTF.getText(),
                    lnameTF.getText(),
                    Integer.parseInt(phonenumberTF.getText()),
                    roleCB.getValue().equalsIgnoreCase("admin") ? UserRole.Admin : UserRole.User,
                    enabledCB.isSelected()
            );

            if (currentUser == null) {
                if (UserService.getInstance().add(user)) {
                    AlertUtils.makeSuccessNotification("User ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_USER);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                user.setPassword(currentUser.getPassword());
                user.setId(currentUser.getId());
                if (UserService.getInstance().edit(user)) {
                    AlertUtils.makeSuccessNotification("User modifié avec succés");
                    ShowAllController.currentUser = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_USER);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

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

        if (currentUser == null) {
            if (passwordTF.getText().isEmpty()) {
                AlertUtils.makeInformation("Mot de passe ne doit pas etre vide");
                return false;
            }


        }




        if (fnameTF.getText().isEmpty() || !Pattern.matches("[a-zA-Z]+",  fnameTF.getText())) {
            AlertUtils.makeInformation("Le nom ne doit pas être vide et doit contenir uniquement des lettres.");
            return false;
        }
        if (!isValidPassword(passwordTF.getText())) {
            AlertUtils.makeInformation("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
            return false;
        }

        if (lnameTF.getText().isEmpty() || !Pattern.matches("[a-zA-Z]+",  lnameTF.getText())) {
            AlertUtils.makeInformation("Le prénom ne doit pas être vide et doit contenir uniquement des lettres.");
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
    private boolean isValidPassword(String password) {
        // Vérifie si le mot de passe contient au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }
}