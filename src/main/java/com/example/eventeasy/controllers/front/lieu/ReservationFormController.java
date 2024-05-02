package com.example.eventeasy.controllers.front.lieu;

import com.example.eventeasy.entities.Lieu;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class ReservationFormController {

    @FXML
    private TextField topText; // Champ de texte pour afficher le nom du lieu
    @FXML
    private DatePicker dateDDP; // Sélecteur de date pour la date de début de la réservation
    @FXML
    private DatePicker dateFDP; // Sélecteur de date pour la date de fin de la réservation

    @FXML
    private TextField prixTF;
    @FXML
    private TextField idRes;




    // Méthode pour initialiser les données du lieu dans le formulaire de réservation
    public void initData(Lieu lieu) {
        // Afficher le nom du lieu dans le champ de texte
        //topText.setText(lieu.getNom());
        idRes.setText(String.valueOf(lieu.getId()));
        // Vous pouvez également afficher d'autres détails du lieu si nécessaire
        // Par exemple, le prix, la capacité, etc.

        // Vous pouvez également effectuer d'autres initialisations si nécessaire
    }

    // Méthode pour récupérer la date de début de la réservation
    public LocalDate getDateDebut() {
        return dateDDP.getValue();
    }

    // Méthode pour récupérer la date de fin de la réservation
    public LocalDate getDateFin() {
        return dateFDP.getValue();
    }

    // Autres méthodes de gestion du formulaire de réservation, par exemple valider les données, enregistrer la réservation, etc.
}
