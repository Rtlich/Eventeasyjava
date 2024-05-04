package com.example.eventeasy.controllers.back.categoryAllocation;


import com.example.eventeasy.entities.CategoryAllocation;
import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.services.CategoryAllocationService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField nomTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    CategoryAllocation currentCategoryAllocation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentCategoryAllocation = ShowAllController.currentCategoryAllocation;

        if (currentCategoryAllocation != null) {
            topText.setText("Modifier categoryAllocation");
            btnAjout.setText("Modifier");

            try {
                nomTF.setText(currentCategoryAllocation.getNom());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter categoryAllocation");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            CategoryAllocation categoryAllocation = new CategoryAllocation(
                    nomTF.getText()
            );

            if (currentCategoryAllocation == null) {
                if (CategoryAllocationService.getInstance().ajouter(categoryAllocation)) {
                    AlertUtils.makeSuccessNotification("CategoryAllocation ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORYALLOCATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                categoryAllocation.setId(currentCategoryAllocation.getId());
                if (CategoryAllocationService.getInstance().modifier(categoryAllocation)) {
                    AlertUtils.makeSuccessNotification("CategoryAllocation modifié avec succés");
                    ShowAllController.currentCategoryAllocation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORYALLOCATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (nomTF.getText().isEmpty()) {
            AlertUtils.makeInformation("nom ne doit pas etre vide");
            return false;
        }


        return true;
    }
}