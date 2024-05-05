package com.example.eventeasy.controllers.back.lieu;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.CategoryL;
import com.example.eventeasy.entities.Lieu;
import com.example.eventeasy.services.LieuService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField nomTF;
    @FXML
    public TextField prixTF;
    @FXML
    public ImageView imageIV;
    @FXML
    public DatePicker dateDDP;
    @FXML
    public DatePicker dateFDP;
    @FXML
    public TextField capacityTF;
    @FXML
    public TextField regionTF;

    @FXML
    private VBox map;

    @FXML
    public ComboBox<CategoryL> categoryCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    @FXML
    private Label locationLabel;

    private MapView mapView;
    private Circle marker;

    Lieu currentLieu;
    Path selectedImagePath;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialiser la carte
        mapView = createMapView();
        map.getChildren().add(mapView);

        for (CategoryL category : LieuService.getInstance().getAllCategorys()) {
            categoryCB.getItems().add(category);
        }

        currentLieu = ShowAllController.currentLieu;

        if (currentLieu != null) {
            topText.setText("Modifier lieu");
            btnAjout.setText("Modifier");

            try {
                nomTF.setText(currentLieu.getNom());
                prixTF.setText(String.valueOf(currentLieu.getPrix()));
                selectedImagePath = FileSystems.getDefault().getPath(currentLieu.getImage());
                if (selectedImagePath.toFile().exists()) {
                    imageIV.setImage(new Image(selectedImagePath.toUri().toString()));
                }
                dateDDP.setValue(currentLieu.getDateD());
                dateFDP.setValue(currentLieu.getDateF());
                capacityTF.setText(String.valueOf(currentLieu.getCapacity()));
                regionTF.setText(currentLieu.getRegion());

                categoryCB.setValue(currentLieu.getCategory());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter lieu");
            btnAjout.setText("Ajouter");
        }
    }

    // Créer et configurer la carte
    private MapView createMapView() {
        mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.setZoom(15);

        marker = new Circle(5, Color.RED);
        mapView.addLayer(new CustomMapLayer(marker));

        // Initialiser l'événement de clic sur la carte
        mapView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Point2D mouseCoords = new Point2D(event.getX(), event.getY());
            MapPoint mapPoint = mapView.getMapPosition(event.getX(), event.getY());
            updateMarkerPosition(mapPoint);
        });

        return mapView;
    }

    // Mettre à jour la position du marqueur sur la carte et afficher les coordonnées
    private void updateMarkerPosition(MapPoint mapPoint) {
        locationLabel.setText(String.format("Latitude: %f, Longitude: %f", mapPoint.getLatitude(), mapPoint.getLongitude()));
        marker.setCenterX(mapPoint.getLongitude());
        marker.setCenterY(mapPoint.getLatitude());
    }

    @FXML
    private void manage(ActionEvent ignored) {
        if (controleDeSaisie()) {
            createImageFile();
            String imagePath = selectedImagePath.toString();

            Lieu lieu = new Lieu();
            lieu.setNom(nomTF.getText());
            lieu.setPrix(Float.parseFloat(prixTF.getText()));
            lieu.setImage(imagePath);
            lieu.setDateD(dateDDP.getValue());
            lieu.setDateF(dateFDP.getValue());
            lieu.setCapacity(Integer.parseInt(capacityTF.getText()));
            lieu.setRegion(regionTF.getText());

            lieu.setCategory(categoryCB.getValue());

            // Mettre à jour les valeurs de latitude et de longitude
            lieu.setLatitude(marker.getCenterY());
            lieu.setLongitude(marker.getCenterX());

            if (currentLieu == null) {
                if (LieuService.getInstance().add(lieu)) {
                    AlertUtils.makeSuccessNotification("Lieu ajouté avec succès");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_LIEU);
                } else {
                    AlertUtils.makeError("Erreur lors de l'ajout du lieu");
                }
            } else {
                lieu.setId(currentLieu.getId());
                if (LieuService.getInstance().edit(lieu)) {
                    AlertUtils.makeSuccessNotification("Lieu modifié avec succès");
                    ShowAllController.currentLieu = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_LIEU);
                } else {
                    AlertUtils.makeError("Erreur lors de la modification du lieu");
                }
            }
        }
    }

    @FXML
    public void chooseImage(ActionEvent ignored) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(MainApp.mainStage);
        if (file != null) {
            selectedImagePath = Paths.get(file.getPath());
            imageIV.setImage(new Image(file.toURI().toString()));
        }
    }

    public void createImageFile() {
        try {
            Path newPath = FileSystems.getDefault().getPath("src/main/resources/com/example/eventeasy/images/uploads/" + selectedImagePath.getFileName());
            Files.copy(selectedImagePath, newPath, StandardCopyOption.REPLACE_EXISTING);
            selectedImagePath = newPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean controleDeSaisie() {
        if (nomTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Le nom ne doit pas être vide");
            return false;
        }

        if (nomTF.getText().matches(".*\\d.*")) {
            AlertUtils.makeInformation("Le nom ne doit pas contenir de chiffres");
            return false;
        }

        if (prixTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Le prix ne doit pas être vide");
            return false;
        }

        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("Le prix doit être un nombre réel");
            return false;
        }

        if (selectedImagePath == null) {
            AlertUtils.makeInformation("Veuillez choisir une image");
            return false;
        }

        if (dateDDP.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir une date pour la date de début");
            return false;
        }

        if (dateFDP.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir une date pour la date de fin");
            return false;
        }

        LocalDate today = LocalDate.now();
        if (dateDDP.getValue().isBefore(today)) {
            AlertUtils.makeInformation("La date de début doit être aujourd'hui ou ultérieure");
            return false;
        }

        if (dateFDP.getValue().isBefore(dateDDP.getValue())) {
            AlertUtils.makeInformation("La date de fin doit être postérieure à la date de début");
            return false;
        }

        if (capacityTF.getText().isEmpty()) {
            AlertUtils.makeInformation("La capacité ne doit pas être vide");
            return false;
        }

        try {
            Integer.parseInt(capacityTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("La capacité doit être un nombre entier");
            return false;
        }

        if (regionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("La région ne doit pas être vide");
            return false;
        }

        if (categoryCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir une catégorie");
            return false;
        }

        return true;
    }

    private class CustomMapLayer extends MapLayer {
        private Circle marker;

        public CustomMapLayer(Circle marker) {
            this.marker = marker;
            getChildren().add(marker);
        }

        @Override
        protected void layoutLayer() {
            marker.setTranslateX(0);
            marker.setTranslateY(0);
        }
    }
}
