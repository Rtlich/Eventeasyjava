package com.example.eventeasy.controllers.front;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.utils.Animations;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    static AnchorPane staticContent;
    private static MainWindowController instance;

    private final Color COLOR_GRAY = new Color(0.9, 0.9, 0.9, 1);
    private final Color COLOR_PRIMARY = Color.web("#BA877F");
    private final Color COLOR_DARK = new Color(1, 1, 1, 0.65);

    private Button[] liens;

    @FXML
    private AnchorPane content;


    @FXML
    private Button btnBookingLs;

    @FXML
    private Button btnCategoryLs;

    @FXML
    private Button btnLieus;


    public static MainWindowController getInstance() {
        if (instance == null) {
            instance = new MainWindowController();
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        staticContent = content;

        liens = new Button[]{
                btnBookingLs,
                btnCategoryLs,
                btnLieus,
        };

        for (Button lien : liens) {
            lien.setTextFill(COLOR_DARK);
            lien.setBackground(new Background(new BackgroundFill(COLOR_PRIMARY, CornerRadii.EMPTY, Insets.EMPTY)));
            Animations.animateButton(lien, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
        }

        btnBookingLs.setTextFill(COLOR_DARK);

        btnCategoryLs.setTextFill(COLOR_DARK);

        btnLieus.setTextFill(COLOR_DARK);


        loadInterface(Constants.FXML_FRONT_HOME);
    }


    @FXML
    private void afficherBookingLs(ActionEvent ignored) {
        goToLink(Constants.FXML_FRONT_DISPLAY_ALL_BOOKING_L);

        btnBookingLs.setTextFill(COLOR_PRIMARY);
        Animations.animateButton(btnBookingLs, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
    }

    @FXML
    private void afficherCategoryLs(ActionEvent ignored) {
        goToLink(Constants.FXML_FRONT_DISPLAY_ALL_CATEGORY_L);

        btnCategoryLs.setTextFill(COLOR_PRIMARY);
        Animations.animateButton(btnCategoryLs, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
    }

    @FXML
    private void afficherLieus(ActionEvent ignored) {
        goToLink(Constants.FXML_FRONT_DISPLAY_ALL_LIEU);

        btnLieus.setTextFill(COLOR_PRIMARY);
        Animations.animateButton(btnLieus, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
    }


    @FXML
    public void logout(ActionEvent ignored) {
        MainApp.getInstance().logout();
    }

    private void goToLink(String fxmlLink) {
        for (Button lien : liens) {
            lien.setTextFill(COLOR_DARK);
            Animations.animateButton(lien, COLOR_GRAY, COLOR_DARK, COLOR_PRIMARY, 0, false);
        }
        MainWindowController.getInstance().loadInterface(fxmlLink);
    }

    public void loadInterface(String location) {
        staticContent.getChildren().clear();
        if (getClass().getResource(location) == null) {
            System.out.println("Could not load FXML check the path");
        } else {
            try {
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(location)));
                AnchorPane.setTopAnchor(parent, 0.0);
                AnchorPane.setBottomAnchor(parent, 0.0);
                AnchorPane.setRightAnchor(parent, 0.0);
                AnchorPane.setLeftAnchor(parent, 0.0);
                staticContent.getChildren().add(parent);
            } catch (IOException e) {
                System.out.println("Could not load FXML : " + e.getMessage() + " check your controller");
                e.printStackTrace();
            }
        }
    }
}
