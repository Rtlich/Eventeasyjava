package com.example.eventeasy.controllers.back.contrat;


import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.Contrat;
import com.example.eventeasy.entities.Partenaire;
import com.example.eventeasy.services.ContratService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public DatePicker datedebutDP;
    @FXML
    public DatePicker datefinDP;

    @FXML
    public ComboBox<Partenaire> partenaireCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Contrat currentContrat;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Partenaire partenaire : ContratService.getInstance().getAllPartenaires()) {
            partenaireCB.getItems().add(partenaire);
        }

        currentContrat = ShowAllController.currentContrat;

        if (currentContrat != null) {
            topText.setText("Modifier contrat");
            btnAjout.setText("Modifier");

            try {
                datedebutDP.setValue(currentContrat.getDatedebut());
                datefinDP.setValue(currentContrat.getDatefin());

                partenaireCB.setValue(currentContrat.getPartenaire());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter contrat");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Contrat contrat = new Contrat();
            contrat.setDatedebut(datedebutDP.getValue());
            contrat.setDatefin(datefinDP.getValue());

            contrat.setPartenaire(partenaireCB.getValue());

            if (currentContrat == null) {
                if (ContratService.getInstance().add(contrat)) {
                    AlertUtils.makeSuccessNotification("Contrat ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CONTRAT);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                contrat.setId(currentContrat.getId());
                if (ContratService.getInstance().edit(contrat)) {
                    AlertUtils.makeSuccessNotification("Contrat modifié avec succés");
                    ShowAllController.currentContrat = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CONTRAT);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (datedebutDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour datedebut");
            return false;
        }

        if (datedebutDP.getValue().isBefore(java.time.LocalDate.now())) {
            AlertUtils.makeInformation("La date de début doit être supérieure ou égale à la date actuelle");
            return false;
        }


        if (datefinDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour datefin");
            return false;
        }

        if (datefinDP.getValue().isBefore(java.time.LocalDate.now())) {
            AlertUtils.makeInformation("La date de fin doit être supérieure ou égale à la date actuelle");
            return false;
        }

        if (datefinDP.getValue().isBefore(datedebutDP.getValue())) {
            AlertUtils.makeInformation("La date de fin doit être supérieure ou égale à la date de début");
            return false;
        }


        if (partenaireCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un partenaire");
            return false;
        }
        return true;
    }
}