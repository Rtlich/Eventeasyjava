package com.example.eventeasymaven.controllers.back.allocation;

import com.example.eventeasymaven.MainApp;
import com.example.eventeasymaven.entities.Allocation;
import com.example.eventeasymaven.entities.CategoryAllocation;
import com.example.eventeasymaven.entities.Event;
import com.example.eventeasymaven.controllers.back.MainWindowController;
import com.example.eventeasymaven.services.AllocationService;
import com.example.eventeasymaven.services.CategoryAllocationService;
import com.example.eventeasymaven.utils.AlertUtils;
import com.example.eventeasymaven.utils.Constants;
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
    public DatePicker dateDP;
    @FXML
    public TextField quantityTF;
    @FXML
    public ComboBox<CategoryAllocation> categoryAllocationCB;
    @FXML
    public ComboBox<Event> eventCB;
    @FXML
    public ImageView imageIV;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Allocation currentAllocation;
    Path selectedImagePath;
    boolean imageEdited;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (CategoryAllocation categoryAllocation : CategoryAllocationService.getInstance().afficher()) {
            categoryAllocationCB.getItems().add(categoryAllocation);
        }

        for (Event event : AllocationService.getInstance().getAllEvent()) {
            eventCB.getItems().add(event);
        }

        currentAllocation = ShowAllController.currentAllocation;

        if (currentAllocation != null) {
            topText.setText("Modifier allocation");
            btnAjout.setText("Modifier");

            try {
                nomTF.setText(currentAllocation.getNom());
                prixTF.setText(String.valueOf(currentAllocation.getPrix()));
                dateDP.setValue(currentAllocation.getDate());
                quantityTF.setText(String.valueOf(currentAllocation.getQuantity()));
                categoryAllocationCB.setValue(currentAllocation.getCategoryAllocation());
                eventCB.setValue(currentAllocation.getEvent());
                selectedImagePath = FileSystems.getDefault().getPath(currentAllocation.getImage());
                if (selectedImagePath.toFile().exists()) {
                    imageIV.setImage(new Image(selectedImagePath.toUri().toString()));
                }

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter allocation");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            String imagePath;
            if (imageEdited) {
                imagePath = currentAllocation.getImage();
            } else {
                createImageFile();
                imagePath = selectedImagePath.toString();
            }

            Allocation allocation = new Allocation(
                    nomTF.getText(),
                    Float.parseFloat(prixTF.getText()),
                    dateDP.getValue(),
                    Integer.parseInt(quantityTF.getText()),
                    categoryAllocationCB.getValue(),
                    eventCB.getValue(),
                    imagePath
            );

            if (currentAllocation == null) {
                if (AllocationService.getInstance().ajouter(allocation)) {
                    AlertUtils.makeSuccessNotification("Allocation ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_ALLOCATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                allocation.setId(currentAllocation.getId());
                if (AllocationService.getInstance().modifier(allocation)) {
                    AlertUtils.makeSuccessNotification("Allocation modifié avec succés");
                    ShowAllController.currentAllocation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_ALLOCATION);
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
            Path newPath = FileSystems.getDefault().getPath("src/com/eventeasy/images/uploads/" + selectedImagePath.getFileName());
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
        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour date");
            return false;
        }


        if (quantityTF.getText().isEmpty()) {
            AlertUtils.makeInformation("quantity ne doit pas etre vide");
            return false;
        }


        try {
            Integer.parseInt(quantityTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("quantity doit etre un nombre");
            return false;
        }

        if (categoryAllocationCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir categoryAllocation");
            return false;
        }


        if (categoryAllocationCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir categoryAllocation");
            return false;
        }


        if (selectedImagePath == null) {
            AlertUtils.makeInformation("Veuillez choisir une image");
            return false;
        }


        return true;
    }
}