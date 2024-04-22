package com.example.eventeasymaven.controllers;

import com.example.eventeasymaven.MainApp;
import com.example.eventeasymaven.utils.AlertUtils;
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
