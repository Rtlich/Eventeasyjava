package com.example.eventeasy.controllers;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.services.UserService;
import com.example.eventeasy.utils.AlertUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.captcha.Captcha;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField inputEmail;
    @FXML
    public PasswordField inputPassword;
    @FXML
    public Button btnSignup;

    @FXML
    public ImageView captchaIV;
    @FXML
    public TextField captchaTF;

    Captcha captcha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCaptcha();
    }

    public void signUp(ActionEvent ignored) {
        MainApp.getInstance().loadSignup();
    }

    public void login(ActionEvent ignored) {
        if (!captcha.isCorrect(captchaTF.getText())) {
            AlertUtils.makeInformation("Captcha invalide");
            return;
        }

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


    public void resetCaptcha(ActionEvent ignored) {
        setCaptcha();
        captchaTF.clear();
    }


    void setCaptcha() {
        captcha = new Captcha.Builder(250, 200)
                .addText()
                .addBackground()
                .addNoise()
                .addBorder()
                .build();

        Image image = SwingFXUtils.toFXImage(captcha.getImage(), null);
        captchaIV.setImage(image);
    }
}
