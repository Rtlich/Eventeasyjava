package com.example.eventeasymaven.controllers.back.categoryAllocation;

import com.example.eventeasymaven.entities.CategoryAllocation;
import com.example.eventeasymaven.controllers.back.MainWindowController;
import com.example.eventeasymaven.services.CategoryAllocationService;
import com.example.eventeasymaven.utils.AlertUtils;
import com.example.eventeasymaven.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static CategoryAllocation currentCategoryAllocation;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
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
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_CATEGORYALLOCATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + categoryAllocation.getNom());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierCategoryAllocation(categoryAllocation));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerCategoryAllocation(categoryAllocation));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterCategoryAllocation(ActionEvent ignored) {
        currentCategoryAllocation = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CATEGORYALLOCATION);
    }

    private void modifierCategoryAllocation(CategoryAllocation categoryAllocation) {
        currentCategoryAllocation = categoryAllocation;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CATEGORYALLOCATION);
    }

    private void supprimerCategoryAllocation(CategoryAllocation categoryAllocation) {
        currentCategoryAllocation = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer categoryAllocation ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (CategoryAllocationService.getInstance().supprimer(categoryAllocation)) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORYALLOCATION);
                } else {
                    AlertUtils.makeError("Could not delete categoryAllocation");
                }
            }
        }
    }
}
