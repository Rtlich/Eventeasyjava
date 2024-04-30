package com.example.eventeasy.controllers.back.contrat;

import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.Contrat;
import com.example.eventeasy.services.ContratService;
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

    public static Contrat currentContrat;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Contrat> listContrat;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listContrat = ContratService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listContrat);

        if (!listContrat.isEmpty()) {
            for (Contrat contrat : listContrat) {

                mainVBox.getChildren().add(makeContratModel(contrat));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeContratModel(
            Contrat contrat
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_CONTRAT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#datedebutText")).setText("Date debut : " + contrat.getDatedebut());
            ((Text) innerContainer.lookup("#datefinText")).setText("Date fin : " + contrat.getDatefin());

            ((Text) innerContainer.lookup("#partenaireText")).setText("Partenaire : " + contrat.getPartenaire().toString());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierContrat(contrat));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerContrat(contrat));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterContrat(ActionEvent ignored) {
        currentContrat = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CONTRAT);
    }

    private void modifierContrat(Contrat contrat) {
        currentContrat = contrat;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CONTRAT);
    }

    private void supprimerContrat(Contrat contrat) {
        currentContrat = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer contrat ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ContratService.getInstance().delete(contrat.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CONTRAT);
                } else {
                    AlertUtils.makeError("Could not delete contrat");
                }
            }
        }
    }


}
