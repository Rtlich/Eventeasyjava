package com.example.eventeasy.controllers;

import com.example.eventeasy.MainApp;
import com.example.eventeasy.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void front(ActionEvent actionEvent) {
        MainApp.getInstance().loadFront();
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        MainApp.getInstance().loadBack();
    }
}
