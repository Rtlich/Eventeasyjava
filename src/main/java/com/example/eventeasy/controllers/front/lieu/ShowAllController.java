package com.example.eventeasy.controllers.front.lieu;

import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.controllers.front.bookingL.ManageController;
import com.example.eventeasy.entities.CategoryL;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import com.example.eventeasy.controllers.front.lieu.*;
import javafx.stage.Stage;

public class ShowAllController implements Initializable {

    public static Lieu currentLieu;

    @FXML
    public Text topText;

    @FXML
    public VBox mainVBox;
    @FXML
    public Button previousPageButton;
    @FXML
    public Button nextPageButton;
    @FXML
    public Label pageNumberLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<CategoryL> categoryComboBox;



    List<Lieu> listLieu;

    private List<Lieu> displayedLieux;
    private int currentPage = 1;
    private int itemsPerPage = 2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listLieu = LieuService.getInstance().getAll();
        displayData();
        // Charger toutes les catégories dans le ComboBox
        List<CategoryL> categories = LieuService.getInstance().getAllCategorys();
        categoryComboBox.getItems().addAll(categories);
    }

    void displayData() {
        mainVBox.getChildren().clear();

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, listLieu.size());
        displayedLieux = listLieu.subList(startIndex, endIndex);


        Collections.reverse(listLieu);

        if (!displayedLieux.isEmpty()) {
            for (Lieu lieu : displayedLieux) {
                mainVBox.getChildren().add(makeLieuModel(lieu));
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }

        updatePaginationButtons();

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
            ((Button) innerContainer.lookup("#ReserverButton")).setOnAction((ignored) -> ouvrirFormulaireReservation(lieu));


            //((Button) innerContainer.lookup("#editButton")).setOnAction((ignored) -> modifierLieu(lieu));
            //((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerLieu(lieu));
            //((Button) innerContainer.lookup("#ReserverButton")).setOnAction((ignored) -> ReserverLieu(lieu));



        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void ouvrirFormulaireReservation(Lieu lieu) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FRONT_MODEL_BOOKING_L));
        Parent root;

        try {
            root = loader.load();
            // Charger le contrôleur correct pour le formulaire de réservation
            ManageController controller = loader.getController();
            controller.initData(lieu); // Passer l'objet Lieu sélectionné
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToPreviousPage(ActionEvent event) {
        if (currentPage > 1) {
            currentPage--;
            displayData();
        }
    }
    @FXML
    private void goToNextPage(ActionEvent event) {
        int maxPage = (int) Math.ceil((double) listLieu.size() / itemsPerPage);
        if (currentPage < maxPage) {
            currentPage++;
            displayData();
        }
    }
    private void updatePaginationButtons() {
        int maxPage = (int) Math.ceil((double) listLieu.size() / itemsPerPage);
        pageNumberLabel.setText("Page " + currentPage + " sur " + maxPage);

        previousPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(currentPage == maxPage);
    }
    @FXML
    private void searchLieux(ActionEvent event) {
        String searchQuery = searchTextField.getText().trim().toLowerCase();
        if (!searchQuery.isEmpty()) {
            listLieu = LieuService.getInstance().searchByNom(searchQuery);
        } else {
            listLieu = LieuService.getInstance().getAll();
        }
        currentPage = 1;
        displayData();
    }
    @FXML
    private void filterLieux(ActionEvent event) {
        CategoryL selectedCategory = categoryComboBox.getValue();
        if (selectedCategory != null) {
            listLieu = LieuService.getInstance().filterByCategory(selectedCategory);
        } else {
            listLieu = LieuService.getInstance().getAll();
        }
        currentPage = 1;
        displayData();
    }


    @FXML
    private void ajouterLieu(ActionEvent ignored) {
        currentLieu = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_LIEU);
    }



    /* private void modifierLieu(Lieu lieu) {
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

     */


}
