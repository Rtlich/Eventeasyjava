package com.example.eventeasy.controllers.front.reclamation;


import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.controllers.front.reclamation.ShowAllController;
import com.example.eventeasy.entities.Reclamation;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.services.ReclamationService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField descriptionTF;
    @FXML
    public ComboBox<User> userCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reclamation currentReclamation;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (User user : ReclamationService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        currentReclamation = com.example.eventeasy.controllers.front.reclamation.ShowAllController.currentReclamation;

        if (currentReclamation != null) {
            topText.setText("Modifier reclamation");
            btnAjout.setText("Modifier");

            try {
                descriptionTF.setText(currentReclamation.getDescription());
                userCB.setValue(currentReclamation.getUser());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reclamation");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Reclamation reclamation = new Reclamation();
            reclamation.setDescription(descriptionTF.getText());
            reclamation.setDate(java.time.LocalDate.now());
            reclamation.setUser(userCB.getValue());

            if (currentReclamation == null) {
                if (ReclamationService.getInstance().add(reclamation)) {
                    AlertUtils.makeSuccessNotification("Reclamation ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                reclamation.setId(currentReclamation.getId());
                if (ReclamationService.getInstance().edit(reclamation)) {
                    AlertUtils.makeSuccessNotification("Reclamation modifié avec succés");
                    ShowAllController.currentReclamation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {
        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }

        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }
        return true;
    }
}