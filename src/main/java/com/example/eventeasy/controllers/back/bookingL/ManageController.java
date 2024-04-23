package com.example.eventeasy.controllers.back.bookingL;


import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.BookingL;
import com.example.eventeasy.services.BookingLService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField prixTF;
    @FXML
    public DatePicker dateDDP;
    @FXML
    public DatePicker dateFDP;
    @FXML
    public TextField statusTF;


    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    BookingL currentBookingL;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentBookingL = ShowAllController.currentBookingL;

        if (currentBookingL != null) {
            topText.setText("Modifier bookingL");
            btnAjout.setText("Modifier");

            try {
                prixTF.setText(String.valueOf(currentBookingL.getPrix()));
                dateDDP.setValue(currentBookingL.getDateD());
                dateFDP.setValue(currentBookingL.getDateF());
                statusTF.setText(currentBookingL.getStatus());


            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter bookingL");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            BookingL bookingL = new BookingL();
            bookingL.setPrix(Float.parseFloat(prixTF.getText()));
            bookingL.setDateD(dateDDP.getValue());
            bookingL.setDateF(dateFDP.getValue());
            bookingL.setStatus(statusTF.getText());


            if (currentBookingL == null) {
                if (BookingLService.getInstance().add(bookingL)) {
                    AlertUtils.makeSuccessNotification("BookingL ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_BOOKING_L);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                bookingL.setId(currentBookingL.getId());
                if (BookingLService.getInstance().edit(bookingL)) {
                    AlertUtils.makeSuccessNotification("BookingL modifié avec succés");
                    ShowAllController.currentBookingL = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_BOOKING_L);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (prixTF.getText().isEmpty()) {
            AlertUtils.makeInformation("prix ne doit pas etre vide");
            return false;
        }


        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("prix doit etre un réel");
            return false;
        }





        if (dateDDP.getValue() == null || dateDDP.getValue().isBefore(LocalDate.now())) {
            AlertUtils.makeInformation("La date de début doit être postérieure à la date d'aujourd'hui");
            return false;
        }

        if (dateFDP.getValue() == null || dateFDP.getValue().isBefore(LocalDate.now())) {
            AlertUtils.makeInformation("La date de fin doit être postérieure à la date d'aujourd'hui");
            return false;
        }








        if (statusTF.getText().isEmpty()) {
            AlertUtils.makeInformation("status ne doit pas etre vide");
            return false;
        }


        return true;
    }
}