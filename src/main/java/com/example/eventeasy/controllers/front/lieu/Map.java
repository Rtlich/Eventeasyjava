package com.example.eventeasy.controllers.front.lieu;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Map implements Initializable {

    @FXML
    private AnchorPane rootPane;

    private MapView mapView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapView = new MapView();
        mapView.setCenter(new MapPoint(0, 0));
        mapView.setZoom(10);
        rootPane.getChildren().add(mapView);

        // Afficher la carte lors de l'initialisation
        showMap();
    }

    public void showMap() {
        mapView.setVisible(true);
    }
}
