package com.example.eventeasymaven.controllers;

import com.example.eventeasymaven.MainApp;
import com.example.eventeasymaven.entities.User;
import com.example.eventeasymaven.entities.UserRole;
import com.example.eventeasymaven.services.UserService;
import com.example.eventeasymaven.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {

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
    public Button btnAjout;
    @FXML
    public Text topText;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        topText.setText("Inscription");
        btnAjout.setText("S'inscrire");
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {


            User user = new User(
                    emailTF.getText(),
                    passwordTF.getText(),
                    fnameTF.getText(),
                    lnameTF.getText(),
                    Integer.parseInt(phonenumberTF.getText()),
                    UserRole.User,
                    true
            );

            if (UserService.getInstance().add(user)) {
                AlertUtils.makeSuccessNotification("Inscription effectué avec succés");
                MainApp.getInstance().loadLogin();
            } else {
                AlertUtils.makeError("Existe");
            }
        }
    }


    @FXML
    public void connexion(ActionEvent ignored) {
        MainApp.getInstance().loadLogin();
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

        if (passwordTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Mot de passe ne doit pas etre vide");
            return false;
        }
        if (!isValidPassword(passwordTF.getText())) {
            AlertUtils.makeInformation("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
            return false;
        }

        if (fnameTF.getText().isEmpty() || !Pattern.matches("[a-zA-Z]+",  fnameTF.getText())) {
            AlertUtils.makeInformation("Le nom ne doit pas être vide et doit contenir uniquement des lettres.");
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