package com.example.eventeasy.controllers.front.categoryAllocation;

import com.example.eventeasy.entities.CategoryAllocation;
import com.example.eventeasy.services.CategoryAllocationService;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {

    public static CategoryAllocation currentCategoryAllocation;

    @FXML
    public Text topText;

    public VBox mainVBox;

    List<CategoryAllocation> listCategoryAllocation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCategoryAllocation = CategoryAllocationService.getInstance().afficher();
        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listCategoryAllocation);

        if (!listCategoryAllocation.isEmpty()) {
            for (CategoryAllocation categoryAllocation : listCategoryAllocation) {

                mainVBox.getChildren().add(makeCategoryAllocationModel(categoryAllocation));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeCategoryAllocationModel(
            CategoryAllocation categoryAllocation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_CATEGORYALLOCATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + categoryAllocation.getNom());


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }
}
