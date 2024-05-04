package com.example.eventeasy.controllers.back.reclamation;

import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.Reclamation;
import com.example.eventeasy.services.ReclamationService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Reclamation currentReclamation;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Reclamation> listReclamation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReclamation = ReclamationService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listReclamation);

        if (!listReclamation.isEmpty()) {
            for (Reclamation reclamation : listReclamation) {

                mainVBox.getChildren().add(makeReclamationModel(reclamation));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeReclamationModel(
            Reclamation reclamation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_RECLAMATION)));

            VBox innerContainer = ((VBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + reclamation.getDescription());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + reclamation.getDate());

            ((Text) innerContainer.lookup("#userText")).setText("User : " + reclamation.getUser().toString());

            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerReclamation(reclamation));


            TextField reponseTF = ((TextField) innerContainer.lookup("#reponseTF"));

            ((Button) innerContainer.lookup("#reponseButton")).setOnAction((ignored) -> repondreRec(reclamation, reponseTF.getText()));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }


    private void supprimerReclamation(Reclamation reclamation) {
        currentReclamation = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reclamation ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ReclamationService.getInstance().delete(reclamation.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.makeError("Could not delete reclamation");
                }
            }
        }
    }

    void repondreRec(Reclamation reclamation, String rep) {
        if (rep.isEmpty()) {
            AlertUtils.makeError("Veuillez saisir une réponse");
            return;
        }

        try {
            sendMail(reclamation.getUser().getEmail(), rep);
            AlertUtils.makeSuccessNotification("Reponse envoyée avec succès");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMail(String recipient, String rep) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        // Enable authentication
        properties.put("mail.smtp.auth", "true");
        // Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        // Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        // Set smtp port
        properties.put("mail.smtp.port", "587");

        // Your gmail address
        String myAccountEmail = "elitecars.app@gmail.com";
        // Your gmail password
        String password = "ctztijcbxtzmyxav";

        // Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        // Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recipient, rep);

        // Send mail
        if (message != null) {
            Transport.send(message);
            System.out.println("Mail sent successfully");
        } else {
            System.out.println("Error sending the mail");
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient, String rep) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Reponse à votre réclamation");
            String htmlCode = "<h1>Reponse à votre réclamation</h1> <br> <h2>Reponse : " + rep + "</h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
