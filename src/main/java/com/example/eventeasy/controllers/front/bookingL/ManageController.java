package com.example.eventeasy.controllers.front.bookingL;


import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.entities.BookingL;
import com.example.eventeasy.entities.Lieu;
import com.example.eventeasy.services.BookingLService;
import com.example.eventeasy.services.LieuService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.twilio.Twilio;



import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
    private TextField idRess;



    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    BookingL currentBookingL;
    Lieu lieu; // Déclaration de la variable lieu


    // Votre SID de compte Twilio
    public static final String ACCOUNT_SID = "AC14fce1898408bc9e0b8442b01dadf23d";

    // Votre token d'authentification Twilio
    public static final String AUTH_TOKEN = "527c46732bebd796e735382736082e2e";

    // Le numéro Twilio
    public static final String TWILIO_NUMBER = "+14436162814";
    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField messageField;

    @FXML
    private Button SMSButton;



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

        SMSButton = new Button("Send SMS");
        SMSButton.setLayoutX(200);
        SMSButton.setLayoutY(200);
        SMSButton.getStyleClass().add("sms-button");
        SMSButton.setOnAction(event -> sendSMS());

    }
    public void sendSMS() {
        String userPhoneNumber = phoneNumberField.getText();
        String confirmationMessage = generateConfirmationMessage();
        sendSMS(userPhoneNumber, confirmationMessage);
    }
    private void sendSMS(String toPhoneNumber, String message) {
        if (message == null || message.isEmpty()) {
            AlertUtils.makeError("Le message ne peut pas être vide");
            return;
        }

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                        new PhoneNumber(toPhoneNumber),
                        new PhoneNumber(TWILIO_NUMBER),
                        message)
                .create();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation de réservation");
        alert.setHeaderText(null);
        alert.setContentText("Un SMS de confirmation a été envoyé !");
        alert.showAndWait();

        phoneNumberField.clear();
        messageField.clear();
    }
    private String generateConfirmationMessage() {
        String message = "Votre réservation a été confirmée avec succès !\n\n";
        message += "Date de début : " + dateDDP.getValue() + "\n";
        message += "Date de fin : " + dateFDP.getValue() + "\n";
        message += "Prix : " + prixTF.getText() + " €\n\n";
        message += "Merci pour votre réservation !";
        return message;
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
                            // Ajouter la réservation
                            AlertUtils.makeSuccessNotification("BookingL ajouté avec succès");
                            MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_BOOKING_L);
                            closeWindow();

                            // Envoyer le SMS de confirmation
                            String userPhoneNumber = phoneNumberField.getText();
                            String confirmationMessage = generateConfirmationMessage();
                            sendSMS(userPhoneNumber, confirmationMessage);
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

        if (dateFDP.getValue().isBefore(dateDDP.getValue())) {
            AlertUtils.makeInformation("La date de fin doit être après la date de début");
            return false;
        }

        Lieu lieu = LieuService.getInstance().getLieuById(Integer.parseInt(idRess.getText()));
        if (lieu != null) {
            LocalDate startDate = dateDDP.getValue();
            LocalDate endDate = dateFDP.getValue();

            // Vérifier s'il existe une réservation pour le même lieu dans la période spécifiée
            List<BookingL> existingBookings = BookingLService.getInstance().getBookingsForLieu(lieu.getId(), startDate, endDate);
            if (!existingBookings.isEmpty()) {
                AlertUtils.makeInformation("Le lieu est déjà réservé pour cette période");
                return false;
            }

            // Vérifier la disponibilité du lieu pour la période spécifiée
            if (startDate.isBefore(lieu.getDateD()) || endDate.isAfter(lieu.getDateF())) {
                AlertUtils.makeInformation("La réservation doit être dans l'intervalle de disponibilité du lieu");
                return false;
            }
        } else {
            AlertUtils.makeInformation("Le lieu spécifié n'existe pas");
            return false;
        }

        return true;
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