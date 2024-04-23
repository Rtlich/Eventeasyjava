package com.example.eventeasy.controllers.front.lieu;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.entities.CategoryL;
import com.example.eventeasy.entities.Lieu;
import com.example.eventeasy.services.LieuService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
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
    public ComboBox<CategoryL> categoryCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Lieu currentLieu;
    Path selectedImagePath;
    boolean imageEdited;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

            if (currentLieu == null) {
                if (LieuService.getInstance().add(lieu)) {
                    AlertUtils.makeSuccessNotification("Lieu ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_LIEU);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                lieu.setId(currentLieu.getId());
                if (LieuService.getInstance().edit(lieu)) {
                    AlertUtils.makeSuccessNotification("Lieu modifié avec succés");
                    ShowAllController.currentLieu = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_LIEU);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

            if (selectedImagePath != null) {
                createImageFile();
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
            AlertUtils.makeInformation("nom ne doit pas etre vide");
            return false;
        }


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
        if (selectedImagePath == null) {
            AlertUtils.makeInformation("Veuillez choisir une image");
            return false;
        }


        if (dateDDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour dateD");
            return false;
        }


        if (dateFDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour dateF");
            return false;
        }


        if (capacityTF.getText().isEmpty()) {
            AlertUtils.makeInformation("capacity ne doit pas etre vide");
            return false;
        }


        try {
            Integer.parseInt(capacityTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("capacity doit etre un nombre");
            return false;
        }

        if (regionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("region ne doit pas etre vide");
            return false;
        }


        if (categoryCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un category");
            return false;
        }
        return true;
    }
}