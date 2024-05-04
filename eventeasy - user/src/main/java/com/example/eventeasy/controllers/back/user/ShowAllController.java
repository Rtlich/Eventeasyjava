package com.example.eventeasy.controllers.back.user;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.controllers.back.MainWindowController;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.entities.UserRole;
import com.example.eventeasy.services.UserService;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static User currentUser;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;
    public TextField searchTF;

    List<User> listUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listUser = UserService.getInstance().getAll();


        displayData("");
    }

    void displayData(String searchText) {
        listUser.removeIf(user -> user.getRole().equals(UserRole.Admin) || user.getId() == MainApp.getSession().getId());

        mainVBox.getChildren().clear();

        Collections.reverse(listUser);

        if (!listUser.isEmpty()) {
            for (User user : listUser) {
                if (searchText.isEmpty()
                        || user.getEmail().toLowerCase().contains(searchText.toLowerCase())
                        || user.getFname().toLowerCase().contains(searchText.toLowerCase())
                        || user.getLname().toLowerCase().contains(searchText.toLowerCase())
                        || String.valueOf(user.getPhonenumber()).contains(searchText.toLowerCase())
                        || user.getRole().toString().toLowerCase().contains(searchText.toLowerCase())
                ) {
                    mainVBox.getChildren().add(makeUserModel(user));
                }
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeUserModel(User user) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_USER)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#emailText")).setText("Email : " + user.getEmail());
            ((Text) innerContainer.lookup("#fnameText")).setText("Fname : " + user.getFname());
            ((Text) innerContainer.lookup("#lnameText")).setText("Lname : " + user.getLname());
            ((Text) innerContainer.lookup("#phonenumberText")).setText("Phonenumber : " + user.getPhonenumber());
            ((Text) innerContainer.lookup("#rolesText")).setText("Role : " + (user.getRole().equals(UserRole.Admin) ? "Admin" : "User"));

            Text enabledText = ((Text) innerContainer.lookup("#enabledText"));
            enabledText.setText("Enabled : " + user.getEnabled());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierUser(user));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerUser(user));

            Button button = ((Button) innerContainer.lookup("#blockUnblockButton"));

            if (user.getEnabled()) {
                button.setStyle("-fx-background-color: red;");
                button.setText("Bloquer");
            } else {
                button.setStyle("-fx-background-color: green;");
                button.setText("Débloquer");
            }

            button.setOnAction((event) -> toggleBlock(user, button, enabledText));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void modifierUser(User user) {
        currentUser = user;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_USER);
    }

    private void supprimerUser(User user) {
        currentUser = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer user ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (UserService.getInstance().delete(user.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_USER);
                } else {
                    AlertUtils.makeError("Could not delete user");
                }
            }
        }
    }

    private void toggleBlock(User user, Button button, Text enabledText) {
        user.setEnabled(!user.getEnabled());

        if (UserService.getInstance().setEnabled(user.getId(), user.getEnabled())) {
            if (user.getEnabled()) {
                button.setStyle("-fx-background-color: red;");
                button.setText("Bloquer");
            } else {
                button.setStyle("-fx-background-color: green;");
                button.setText("Débloquer");
            }

            enabledText.setText("Enabled : " + user.getEnabled());
        } else {
            AlertUtils.makeError("Error");
        }
    }

    public void showStatistics(ActionEvent actionEvent) {
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_USER_STATISTICS);
    }

    public void search(KeyEvent keyEvent) {
        displayData(searchTF.getText());
    }
}
