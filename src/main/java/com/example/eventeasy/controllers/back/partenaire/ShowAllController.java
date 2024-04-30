package com.example.eventeasy.controllers.back.partenaire;

import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.Partenaire;
import com.example.eventeasy.services.PartenaireService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Partenaire currentPartenaire;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Partenaire> listPartenaire;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listPartenaire = PartenaireService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listPartenaire);

        if (!listPartenaire.isEmpty()) {
            for (Partenaire partenaire : listPartenaire) {

                mainVBox.getChildren().add(makePartenaireModel(partenaire));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makePartenaireModel(
            Partenaire partenaire
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_PARTENAIRE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + partenaire.getNom());
            ((Text) innerContainer.lookup("#telText")).setText("Tel : " + partenaire.getTel());
            ((Text) innerContainer.lookup("#donText")).setText("Don : " + partenaire.getDon());


            Path selectedImagePath = FileSystems.getDefault().getPath(partenaire.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierPartenaire(partenaire));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerPartenaire(partenaire));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterPartenaire(ActionEvent ignored) {
        currentPartenaire = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_PARTENAIRE);
    }

    private void modifierPartenaire(Partenaire partenaire) {
        currentPartenaire = partenaire;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_PARTENAIRE);
    }

    private void supprimerPartenaire(Partenaire partenaire) {
        currentPartenaire = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer partenaire ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (PartenaireService.getInstance().delete(partenaire.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_PARTENAIRE);
                } else {
                    AlertUtils.makeError("Could not delete partenaire");
                }
            }
        }
    }


}
