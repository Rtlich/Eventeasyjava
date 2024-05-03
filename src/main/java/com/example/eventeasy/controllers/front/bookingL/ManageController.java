package com.example.eventeasy.controllers.front.bookingL;


import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.entities.BookingL;
import com.example.eventeasy.entities.Lieu;
import com.example.eventeasy.services.BookingLService;
import com.example.eventeasy.services.LieuService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;



public class ManageController implements Initializable {

    @FXML
    public TextField prixTF;
    @FXML
    public DatePicker dateDDP;
    @FXML
    public DatePicker dateFDP;
    @FXML
    public Label labelLieuName;



    @FXML
    private TextField idRess;



    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    BookingL currentBookingL;
    Lieu lieu; // Déclaration de la variable lieu



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentBookingL = ShowAllController.currentBookingL;

        if (currentBookingL != null) {
            topText.setText("Modifier Reservation");
            btnAjout.setText("Modifier");

            try {
                prixTF.setText(String.valueOf(currentBookingL.getPrix()));
                dateDDP.setValue(currentBookingL.getDateD());
                dateFDP.setValue(currentBookingL.getDateF());



            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter Reservation");
            btnAjout.setText("Ajouter");
        }
        // Ajout des ChangeListeners pour mettre à jour automatiquement le prix
        dateDDP.valueProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                updatePrix();
            }
        });

        dateFDP.valueProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                updatePrix();
            }
        });

    }

    @FXML
    private void manage(ActionEvent event) {
        if (controleDeSaisie()) {
            BookingL bookingL = new BookingL();
            bookingL.setPrix(Float.parseFloat(prixTF.getText()));
            bookingL.setDateD(dateDDP.getValue());
            bookingL.setDateF(dateFDP.getValue());


            String idRessText = idRess.getText();
            if (!idRessText.isEmpty()) {
                try {
                    int lieub_id = Integer.parseInt(idRessText);
                    bookingL.setLieub_id(lieub_id);


                    if (currentBookingL == null) {
                        if (BookingLService.getInstance().add(bookingL)) {
                            AlertUtils.makeSuccessNotification("BookingL ajouté avec succès");
                            MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_BOOKING_L);
                            closeWindow();
                        } else {
                            AlertUtils.makeError("Erreur lors de l'ajout de BookingL");
                        }
                    } else {
                        bookingL.setId(currentBookingL.getId()); // Set the ID of the current booking
                        if (BookingLService.getInstance().edit(bookingL)) {
                            AlertUtils.makeSuccessNotification("BookingL modifié avec succès");
                            ShowAllController.currentBookingL = null;
                            MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_BOOKING_L);
                        } else {
                            AlertUtils.makeError("Erreur lors de la modification de BookingL");
                        }
                    }
                } catch (NumberFormatException e) {
                    AlertUtils.makeError("L'identifiant de ressource doit être un nombre entier");
                }
            } else {
                AlertUtils.makeInformation("Veuillez saisir un identifiant de ressource");
            }
        }
    }

    public void initData(Lieu lieu) {
        // Afficher le nom du lieu dans le champ de texte
        //topText.setText(lieu.getNom());
        idRess.setText(String.valueOf(lieu.getId()));
        this.lieu = lieu; // Ajout de cette ligne pour initialiser l'objet lieu
    }

    public void initData(int lieub_id) {
        idRess.setText(String.valueOf(lieub_id));
    }

    private boolean controleDeSaisie() {
        if (prixTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Le prix ne doit pas être vide");
            return false;
        }

        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("Le prix doit être un réel");
            return false;
        }

        if (dateDDP.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir une date de début");
            return false;
        }

        if (dateFDP.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir une date de fin");
            return false;
        }

        if (dateDDP.getValue().isBefore(LocalDate.now())) {
            AlertUtils.makeInformation("La date de début doit être aujourd'hui ou ultérieure");
            return false;
        }

        Lieu lieu = LieuService.getInstance().getLieuById(Integer.parseInt(idRess.getText()));
        if (lieu != null &&
                !dateDDP.getValue().isBefore(lieu.getDateD()) &&
                !dateFDP.getValue().isBefore(dateDDP.getValue()) &&
                !dateFDP.getValue().isAfter(lieu.getDateF())) {

            return true;
        } else {
            AlertUtils.makeInformation("La réservation doit être dans l'intervalle de disponibilité du lieu et après la date d'aujourd'hui");
            return false;
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAjout.getScene().getWindow();
        stage.close();
    }
    public void updatePrix() {
        if (lieu != null && dateDDP.getValue() != null && dateFDP.getValue() != null) {
            long days = Math.abs(dateFDP.getValue().toEpochDay() - dateDDP.getValue().toEpochDay());
            float prixTotal = days * lieu.getPrix();
            prixTF.setText(String.valueOf(prixTotal));
        }





}}