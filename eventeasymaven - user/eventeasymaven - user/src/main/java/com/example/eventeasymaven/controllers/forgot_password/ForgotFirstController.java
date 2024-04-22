package com.example.eventeasymaven.controllers.forgot_password;

import com.example.eventeasymaven.MainApp;
import com.example.eventeasymaven.services.UserService;
import com.example.eventeasymaven.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotFirstController implements Initializable {

    static String email;

    @FXML
    public TextField inputEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void backToLogin(ActionEvent actionEvent) {
        MainApp.getInstance().loadLogin();
    }

    @FXML
    public void next(ActionEvent actionEvent) {
        email = inputEmail.getText();

        if (email.isEmpty()) {
            AlertUtils.makeInformation("Email ne doit pas etre vide");
        } else {
            if (UserService.getInstance().checkExist(email)) {
                ForgotSecondController.resetCode = new Random().nextInt((9999 - 1000) + 1) + 1000;
                System.out.println("Reset code : " + ForgotSecondController.resetCode);

                try {
                    sendMail(email);
                    AlertUtils.makeInformation("Un code de confirmation a été envoyé a votre email");
                } catch (Exception e) {
                    AlertUtils.makeError("Email error : " + e.getMessage());
                }

                MainApp.getInstance().loadForgotSecond();
            } else {
                AlertUtils.makeInformation("Email n'existe pas");
            }
        }
    }

    public static void sendMail(String recepient) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        //Enable authentication
        properties.put("mail.smtp.auth", "true");
        //Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        //Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //Set smtp port
        properties.put("mail.smtp.port", "587");

        //Your gmail address
        String myAccountEmail = "elite.cars.app@gmail.com";
        //Your gmail password
        String password = "cijlztvkextzjvia";

        //Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        //Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recepient);

        //Send mail
        if (message != null) {
            Transport.send(message);
            System.out.println("Mail sent successfully");
        } else {
            System.out.println("Error sending the mail");
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Réinitialisation de mot de passe");
            String htmlCode = "<h1>Notification</h1> <br/> " +
                    "Votre code de réinitialisation est : " +
                    "<h2><b>" + ForgotSecondController.resetCode + "</b></h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
