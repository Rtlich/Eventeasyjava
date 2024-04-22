package com.example.eventeasy.controllers.back.categoryE;

import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.CategoryE;
import com.example.eventeasy.services.CategoryEService;
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

    public static CategoryE currentCategoryE;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<CategoryE> listCategoryE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCategoryE = CategoryEService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listCategoryE);

        if (!listCategoryE.isEmpty()) {
            for (CategoryE categoryE : listCategoryE) {

                mainVBox.getChildren().add(makeCategoryEModel(categoryE));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeCategoryEModel(
            CategoryE categoryE
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_CATEGORY_E)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + categoryE.getType());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierCategoryE(categoryE));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerCategoryE(categoryE));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterCategoryE(ActionEvent ignored) {
        currentCategoryE = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CATEGORY_E);
    }

    private void modifierCategoryE(CategoryE categoryE) {
        currentCategoryE = categoryE;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CATEGORY_E);
    }

    private void supprimerCategoryE(CategoryE categoryE) {
        currentCategoryE = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer categoryE ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (CategoryEService.getInstance().delete(categoryE.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORY_E);
                } else {
                    AlertUtils.makeError("Could not delete categoryE");
                }
            }
        }
    }


}
