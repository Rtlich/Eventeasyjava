package com.example.eventeasy.controllers.back.categoryL;


import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.CategoryL;
import com.example.eventeasy.services.CategoryLService;
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

    CategoryL currentCategoryL;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentCategoryL = ShowAllController.currentCategoryL;

        if (currentCategoryL != null) {
            topText.setText("Modifier categoryL");
            btnAjout.setText("Modifier");

            try {
                nomTF.setText(currentCategoryL.getNom());


            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter categoryL");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            CategoryL categoryL = new CategoryL();
            categoryL.setNom(nomTF.getText());


            if (currentCategoryL == null) {
                if (CategoryLService.getInstance().add(categoryL)) {
                    AlertUtils.makeSuccessNotification("CategoryL ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORY_L);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                categoryL.setId(currentCategoryL.getId());
                if (CategoryLService.getInstance().edit(categoryL)) {
                    AlertUtils.makeSuccessNotification("CategoryL modifié avec succés");
                    ShowAllController.currentCategoryL = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORY_L);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {
        String nom = nomTF.getText().trim();

        // Vérifier si le nom est vide
        if (nom.isEmpty()) {
            AlertUtils.makeInformation("Le nom ne doit pas être vide.");
            return false;
        }

        // Vérifier si le nom contient des chiffres
        if (containsNumbers(nom)) {
            AlertUtils.makeInformation("Le nom ne doit pas contenir de chiffres.");
            return false;
        }

        return true;
    }

    // Méthode pour vérifier si le nom contient des chiffres
    private boolean containsNumbers(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

}