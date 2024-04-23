package com.example.eventeasy.controllers.front.lieu;

import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.entities.Lieu;
import com.example.eventeasy.services.LieuService;
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

    public static Lieu currentLieu;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Lieu> listLieu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listLieu = LieuService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listLieu);

        if (!listLieu.isEmpty()) {
            for (Lieu lieu : listLieu) {

                mainVBox.getChildren().add(makeLieuModel(lieu));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeLieuModel(
            Lieu lieu
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_LIEU)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + lieu.getNom());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + lieu.getPrix());

            ((Text) innerContainer.lookup("#dateDText")).setText("DateD : " + lieu.getDateD());
            ((Text) innerContainer.lookup("#dateFText")).setText("DateF : " + lieu.getDateF());
            ((Text) innerContainer.lookup("#capacityText")).setText("Capacity : " + lieu.getCapacity());
            ((Text) innerContainer.lookup("#regionText")).setText("Region : " + lieu.getRegion());

            ((Text) innerContainer.lookup("#categoryText")).setText("CategoryL : " + lieu.getCategory().toString());
            Path selectedImagePath = FileSystems.getDefault().getPath(lieu.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            ((Button) innerContainer.lookup("#editButton")).setOnAction((ignored) -> modifierLieu(lieu));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerLieu(lieu));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterLieu(ActionEvent ignored) {
        currentLieu = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_LIEU);
    }

    private void modifierLieu(Lieu lieu) {
        currentLieu = lieu;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_LIEU);
    }

    private void supprimerLieu(Lieu lieu) {
        currentLieu = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer lieu ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (LieuService.getInstance().delete(lieu.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_LIEU);
                } else {
                    AlertUtils.makeError("Could not delete lieu");
                }
            }
        }
    }


}
