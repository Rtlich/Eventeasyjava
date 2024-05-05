package com.example.eventeasy.controllers.back.bookingL;

import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.BookingL;
import com.example.eventeasy.entities.Lieu;
import com.example.eventeasy.services.BookingLService;
import com.example.eventeasy.services.LieuService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;
import java.util.*;

public class ShowAllController implements Initializable {

    public static BookingL currentBookingL;

    @FXML
    public Text topText;

    @FXML
    public VBox mainVBox;

    @FXML
    private VBox mostReservedDetailsVBox;
    @FXML
    private PieChart mostReservedPieChart;

    @FXML
    private BarChart<String, Number> reservationChart;

    List<BookingL> listBookingL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        listBookingL = BookingLService.getInstance().getAll();
        displayData();
        displayMostReservedLieu();


    }

    private void displayMostReservedLieu() {
        Lieu mostReservedLieu = LieuService.getInstance().getMostReservedLieu();

        if (mostReservedLieu != null) {
            Text lieuName = new Text(mostReservedLieu.getNom());
            Text reservationCount = new Text("Nombre de réservations : " + mostReservedLieu.getReservationCount());

            mostReservedDetailsVBox.getChildren().addAll(lieuName, reservationCount);

            // Remplir le PieChart avec les détails du lieu le plus réservé
            updatePieChartData(mostReservedLieu.getReservationCount(), mostReservedLieu.getCapacity() - mostReservedLieu.getReservationCount());

            // Animer le PieChart
            animatePieChart(mostReservedLieu.getReservationCount(), mostReservedLieu.getCapacity() - mostReservedLieu.getReservationCount());
        } else {
            Text noLieuText = new Text("Aucun lieu trouvé");
            mostReservedDetailsVBox.getChildren().add(noLieuText);
        }
    }

    private void updatePieChartData(double reservationCount, double availableCount) {
        mostReservedPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Réservations", reservationCount),
                new PieChart.Data("Disponible", availableCount)
        ));
    }

    private void animatePieChart(double reservationCount, double availableCount) {
        Timeline timeline = new Timeline();
        for (PieChart.Data data : mostReservedPieChart.getData()) {
            KeyValue keyValue = new KeyValue(data.pieValueProperty(),
                    data.getName().equals("Réservations") ? reservationCount : availableCount,
                    Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }
    void displayData() {
        mainVBox.getChildren().clear();
        Collections.reverse(listBookingL);
        if (!listBookingL.isEmpty()) {
            for (BookingL bookingL : listBookingL) {
                mainVBox.getChildren().add(makeBookingLModel(bookingL));
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeBookingLModel(
            BookingL bookingL
    ) {
        Parent parent = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_BACK_MODEL_BOOKING_L));
            parent = loader.load();

            HBox innerContainer = (HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0);
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + bookingL.getPrix());
            ((Text) innerContainer.lookup("#dateDText")).setText("DateD : " + bookingL.getDateD());
            ((Text) innerContainer.lookup("#dateFText")).setText("DateF : " + bookingL.getDateF());

            LieuService lieuService = new LieuService();
            String nomLieu = lieuService.getLieuById(bookingL.getLieub_id()).getNom();
            ((Text) innerContainer.lookup("#statusText")).setText("Lieu : " + nomLieu);

            //((Button) innerContainer.lookup("#editButton")).setOnAction((ignored) -> modifierBookingL(bookingL));
            //((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerBookingL(bookingL));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterBookingL(ActionEvent ignored) {
        currentBookingL = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_BOOKING_L);
    }

    private void modifierBookingL(BookingL bookingL) {
        currentBookingL = bookingL;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_BOOKING_L);
    }

    private void supprimerBookingL(BookingL bookingL) {
        currentBookingL = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer bookingL ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (BookingLService.getInstance().delete(bookingL.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_BOOKING_L);
                } else {
                    AlertUtils.makeError("Could not delete bookingL");
                }
            }
        }
    }
}
