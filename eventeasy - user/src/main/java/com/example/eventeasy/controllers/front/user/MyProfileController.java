package com.example.eventeasy.controllers.front.user;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MyProfileController implements Initializable {

    public static User currentUser;
    public static String fieldToEdit;

    @FXML
    public Text emailText;
    @FXML
    public Text fnameText;
    @FXML
    public Text lnameText;
    @FXML
    public Text phonenumberText;

    @FXML
    public Text topText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = MainApp.getSession();

        emailText.setText("Email : " + user.getEmail());
        fnameText.setText("First name : " + user.getFname());
        lnameText.setText("Last name : " + user.getLname());
        phonenumberText.setText("Phone number : " + user.getPhonenumber());
    }

    @FXML
    public void editProfile(ActionEvent ignored) {
        fieldToEdit = "null";
        currentUser = MainApp.getSession();
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_EDIT_PROFILE);
    }

    @FXML
    public void modifierEmail(ActionEvent actionEvent) {
        fieldToEdit = "email";
        currentUser = MainApp.getSession();
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_EDIT_FIELD);
    }

    @FXML
    public void modifierFname(ActionEvent actionEvent) {
        fieldToEdit = "fname";
        currentUser = MainApp.getSession();
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_EDIT_FIELD);
    }

    @FXML
    public void modifierPhone(ActionEvent actionEvent) {
        fieldToEdit = "phonenumber";
        currentUser = MainApp.getSession();
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_EDIT_FIELD);
    }

    @FXML
    public void modifierLname(ActionEvent actionEvent) {
        fieldToEdit = "lname";
        currentUser = MainApp.getSession();
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_EDIT_FIELD);
    }

    @FXML
    public void modifierPassword(ActionEvent actionEvent) {
        currentUser = MainApp.getSession();
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_RESET_PASSWORD);
    }
}
