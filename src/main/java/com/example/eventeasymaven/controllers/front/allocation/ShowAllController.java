package com.example.eventeasymaven.controllers.front.allocation;

import com.example.eventeasymaven.entities.Allocation;
import com.example.eventeasymaven.services.AllocationService;
import com.example.eventeasymaven.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {

    public static Allocation currentAllocation;

    @FXML
    public Text topText;

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
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_ALLOCATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + allocation.getNom());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + allocation.getPrix());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + allocation.getDate());
            ((Text) innerContainer.lookup("#categoryIdText")).setText("Category : " + allocation.getCategoryAllocation().toString());
            ((Text) innerContainer.lookup("#eventIdText")).setText("Event : " + allocation.getEvent().toString());

            Path selectedImagePath = FileSystems.getDefault().getPath(allocation.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }


    @FXML
    private void search(KeyEvent ignored) {
        displayData(searchTF.getText());
    }

}
