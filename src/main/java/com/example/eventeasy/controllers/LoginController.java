package com.example.eventeasy.controllers;

import com.example.eventeasy.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    public void front(ActionEvent actionEvent) {
        MainApp.getInstance().loadFront();
    }

    public void back(ActionEvent actionEvent) {
        MainApp.getInstance().loadBack();
    }
}
