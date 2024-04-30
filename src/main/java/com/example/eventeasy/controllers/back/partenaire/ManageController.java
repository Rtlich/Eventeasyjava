package com.example.eventeasy.controllers.back.partenaire;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.Partenaire;
import com.example.eventeasy.services.PartenaireService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    public TextField telTF;
    @FXML
    public TextField donTF;
    @FXML
    public ImageView imageIV;


    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Partenaire currentPartenaire;
    Path selectedImagePath;
    boolean imageEdited;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentPartenaire = ShowAllController.currentPartenaire;

        if (currentPartenaire != null) {
            topText.setText("Modifier partenaire");
            btnAjout.setText("Modifier");

            try {
                nomTF.setText(currentPartenaire.getNom());
                telTF.setText(String.valueOf(currentPartenaire.getTel()));
                donTF.setText(String.valueOf(currentPartenaire.getDon()));
                selectedImagePath = FileSystems.getDefault().getPath(currentPartenaire.getImage());
                if (selectedImagePath.toFile().exists()) {
                    imageIV.setImage(new Image(selectedImagePath.toUri().toString()));
                }


            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter partenaire");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {
            createImageFile();
            String imagePath = selectedImagePath.toString();

            Partenaire partenaire = new Partenaire();
            partenaire.setNom(nomTF.getText());
            partenaire.setTel(Integer.parseInt(telTF.getText()));
            partenaire.setDon(Float.parseFloat(donTF.getText()));
            partenaire.setImage(imagePath);


            if (currentPartenaire == null) {
                if (PartenaireService.getInstance().add(partenaire)) {
                    AlertUtils.makeSuccessNotification("Partenaire ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_PARTENAIRE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                partenaire.setId(currentPartenaire.getId());
                if (PartenaireService.getInstance().edit(partenaire)) {
                    AlertUtils.makeSuccessNotification("Partenaire modifié avec succés");
                    ShowAllController.currentPartenaire = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_PARTENAIRE);
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


        if (telTF.getText().isEmpty()) {
            AlertUtils.makeInformation("tel ne doit pas etre vide");
            return false;
        }

        if (telTF.getText().length() != 8) {
            AlertUtils.makeInformation("tel doit etre de 8 chiffres");
            return false;
        }

        try {
            Integer.parseInt(telTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("tel doit etre un nombre");
            return false;
        }

        if (donTF.getText().isEmpty()) {
            AlertUtils.makeInformation("don ne doit pas etre vide");
            return false;
        }


        try {
            Float.parseFloat(donTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("don doit etre un réel");
            return false;
        }
        if (selectedImagePath == null) {
            AlertUtils.makeInformation("Veuillez choisir une image");
            return false;
        }


        return true;
    }
}