package com.example.eventeasy.controllers.back.allocation;

import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.Allocation;
import com.example.eventeasy.services.AllocationService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Allocation currentAllocation;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public TextField searchTF;

    List<Allocation> listAllocation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listAllocation = AllocationService.getInstance().afficher();

        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();

        Collections.reverse(listAllocation);

        if (!listAllocation.isEmpty()) {
            for (Allocation allocation : listAllocation) {
                if (allocation.getNom().toLowerCase().startsWith(searchText.toLowerCase())) {
                    mainVBox.getChildren().add(makeAllocationModel(allocation));
                }

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeAllocationModel(
            Allocation allocation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_ALLOCATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + allocation.getNom());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + allocation.getPrix());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + allocation.getDate());
            ((Text) innerContainer.lookup("#quantityText")).setText("Quantity : " + allocation.getQuantity());
            ((Text) innerContainer.lookup("#categoryIdText")).setText("Category : " + allocation.getCategoryAllocation());
            ((Text) innerContainer.lookup("#eventIdText")).setText("Event : " + allocation.getEvent());

            Path selectedImagePath = FileSystems.getDefault().getPath(allocation.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            Button rentButton = ((Button) innerContainer.lookup("#upToRentButton"));
            rentButton.setText(allocation.isUpToRent() ? "Annuler location" : "Louer");
            rentButton.setStyle(
                    "-fx-background-color: " + (allocation.isUpToRent() ? "#8c1b28" : "#037088") + ";"
            );
            rentButton.setTextFill(Color.WHITE);
            rentButton.setOnAction((ignored_) -> setUpToRent(allocation, rentButton));
            ((Button) innerContainer.lookup("#editButton")).setOnAction((ignored_) -> modifierAllocation(allocation));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored_) -> supprimerAllocation(allocation));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterAllocation(ActionEvent ignored) {
        currentAllocation = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_ALLOCATION);
    }

    private void modifierAllocation(Allocation allocation) {
        currentAllocation = allocation;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_ALLOCATION);
    }

    private void supprimerAllocation(Allocation allocation) {
        currentAllocation = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer allocation ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (AllocationService.getInstance().supprimer(allocation)) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_ALLOCATION);
                } else {
                    AlertUtils.makeError("Could not delete allocation");
                }
            }
        }
    }

    private void setUpToRent(Allocation allocation, Button rentButton) {
        if (AllocationService.getInstance().setUpToRent(allocation.getId(), !allocation.isUpToRent())) {
            allocation.setUpToRent(!allocation.isUpToRent());
            rentButton.setText(allocation.isUpToRent() ? "Annuler location" : "Louer");
            rentButton.setStyle(
                    "-fx-background-color: " + (allocation.isUpToRent() ? "#8c1b28" : "#037088") + ";"
            );
        } else {
            AlertUtils.makeError("Error");
        }
    }

    @FXML
    private void search(KeyEvent ignored) {
        displayData(searchTF.getText());
    }

}
