package com.example.eventeasy.controllers.front.event;

import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.entities.CategoryE;
import com.example.eventeasy.entities.Event;
import com.example.eventeasy.services.EventService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public TextField titleTF;
    @FXML
    public TextField emailTF;
    @FXML
    public TextField phoneTF;
    @FXML
    public DatePicker dateDP;

    @FXML
    public ComboBox<CategoryE> categoryCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Event currentEvent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (CategoryE category : EventService.getInstance().getAllCategorys()) {
            categoryCB.getItems().add(category);
        }

        currentEvent = ShowAllController.currentEvent;

        if (currentEvent != null) {
            topText.setText("Modifier event");
            btnAjout.setText("Modifier");

            try {
                titleTF.setText(currentEvent.getTitle());
                emailTF.setText(currentEvent.getEmail());
                phoneTF.setText(String.valueOf(currentEvent.getPhone()));
                dateDP.setValue(currentEvent.getDate());
                categoryCB.setValue(currentEvent.getCategory());
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter event");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {
        if (controleDeSaisie()) {
            Event event = new Event();
            event.setTitle(titleTF.getText());
            event.setEmail(emailTF.getText());
            event.setPhone(Integer.parseInt(phoneTF.getText()));
            event.setDate(dateDP.getValue());
            event.setCategory(categoryCB.getValue());

            if (currentEvent == null) {
                if (EventService.getInstance().add(event)) {
                    AlertUtils.makeSuccessNotification("Event ajouté avec succès");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_EVENT);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                event.setId(currentEvent.getId());
                if (EventService.getInstance().edit(event)) {
                    AlertUtils.makeSuccessNotification("Event modifié avec succès");
                    ShowAllController.currentEvent = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_EVENT);
                } else {
                    AlertUtils.makeError("Error");
                }
            }
        }
    }

    private boolean controleDeSaisie() {
        if (titleTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Veuillez remplir tous les champs");
            return false;
        }

        // Vérification que le titre ne contient que des caractères alphabétiques
        if (!Pattern.matches("[a-zA-Z]+", titleTF.getText())) {
            AlertUtils.makeInformation("Le titre ne doit contenir que des caractères alphabétiques");
            return false;
        }

        if (emailTF.getText().isEmpty()) {
            AlertUtils.makeInformation("L'email ne doit pas être vide");
            return false;
        }
        if (!Pattern.compile("^(.+)@(.+)$").matcher(emailTF.getText()).matches()) {
            AlertUtils.makeInformation("Email invalide");
            return false;
        }

        if (phoneTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Le téléphone ne doit pas être vide");
            return false;
        }
        if (phoneTF.getText().length() != 8) {
            AlertUtils.makeInformation("Le téléphone doit avoir 8 chiffres");
            return false;
        }

        try {
            Integer.parseInt(phoneTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("Le téléphone doit contenir que des chiffres");
            return false;
        }

        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisissez une date pour l'événement");
            return false;
        }
        if (dateDP.getValue().isBefore(java.time.LocalDate.now())) {
            AlertUtils.makeInformation("La date doit être postérieure à la date d'aujourd'hui");
            return false;
        }

        if (categoryCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir une catégorie");
            return false;
        }

        return true;
    }
}
