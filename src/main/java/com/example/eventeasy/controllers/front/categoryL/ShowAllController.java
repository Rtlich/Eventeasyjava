package com.example.eventeasy.controllers.front.categoryL;

import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.entities.CategoryL;
import com.example.eventeasy.services.CategoryLService;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static CategoryL currentCategoryL;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<CategoryL> listCategoryL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCategoryL = CategoryLService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listCategoryL);

        if (!listCategoryL.isEmpty()) {
            for (CategoryL categoryL : listCategoryL) {

                mainVBox.getChildren().add(makeCategoryLModel(categoryL));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeCategoryLModel(
            CategoryL categoryL
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_CATEGORY_L)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + categoryL.getNom());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((ignored) -> modifierCategoryL(categoryL));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerCategoryL(categoryL));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterCategoryL(ActionEvent ignored) {
        currentCategoryL = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_CATEGORY_L);
    }

    private void modifierCategoryL(CategoryL categoryL) {
        currentCategoryL = categoryL;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_CATEGORY_L);
    }

    private void supprimerCategoryL(CategoryL categoryL) {
        currentCategoryL = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer categoryL ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (CategoryLService.getInstance().delete(categoryL.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_CATEGORY_L);
                } else {
                    AlertUtils.makeError("Could not delete categoryL");
                }
            }
        }
    }


}
