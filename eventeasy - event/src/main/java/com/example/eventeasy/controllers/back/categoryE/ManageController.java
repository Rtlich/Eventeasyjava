package com.example.eventeasy.controllers.back.categoryE;

import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.CategoryE;
import com.example.eventeasy.services.CategoryEService;
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
    public TextField typeTF;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    CategoryE currentCategoryE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentCategoryE = ShowAllController.currentCategoryE;

        if (currentCategoryE != null) {
            topText.setText("Modifier categoryE");
            btnAjout.setText("Modifier");

            try {
                typeTF.setText(currentCategoryE.getType());
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter categoryE");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {
        if (controleDeSaisie()) {
            CategoryE categoryE = new CategoryE();
            categoryE.setType(typeTF.getText());

            if (currentCategoryE == null) {
                if (CategoryEService.getInstance().add(categoryE)) {
                    AlertUtils.makeSuccessNotification("Category event ajouté avec succès");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORY_E);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                categoryE.setId(currentCategoryE.getId());
                if (CategoryEService.getInstance().edit(categoryE)) {
                    AlertUtils.makeSuccessNotification("Category event modifié avec succès");
                    ShowAllController.currentCategoryE = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CATEGORY_E);
                } else {
                    AlertUtils.makeError("Error");
                }
            }
        }
    }

    private boolean controleDeSaisie() {
        // Vérification que le champ de la catégorie n'est pas vide
        if (typeTF.getText().isEmpty()) {
            AlertUtils.makeInformation("Le champ de la catégorie ne doit pas être vide");
            return false;
        }

        // Vérification que le champ de la catégorie ne contient ni chiffres ni symboles
        if (typeTF.getText().matches(".*\\d.*") || !typeTF.getText().matches("[a-zA-Z]+")) {
            AlertUtils.makeInformation("Le champ de la catégorie ne doit contenir que des lettres ");
            return false;
        }

        return true;
    }
}
