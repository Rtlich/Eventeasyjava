package com.example.eventeasy.controllers.front.lieu;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class Map implements Initializable {

    @FXML
    private VBox address;

    private MapView mapView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapView = createMapView();
        address.getChildren().add(mapView);

        addMarkers();
    }

    // Créer et configurer la carte
    private MapView createMapView() {
        mapView = new MapView();
        mapView.setPrefSize(600, 400);

        // Coordonnées GPS du centre de la carte
        double centerLatitude = 21.4664;
        double centerLongitude = 0.3595;

        // Niveau de zoom approprié
        int zoomLevel = 1;

        mapView.setCenter(new MapPoint(centerLatitude, centerLongitude));
        mapView.setZoom(zoomLevel);

        // Événement de clic sur la carte
        mapView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            MapPoint mapPoint = mapView.getMapPosition(event.getX(), event.getY());
        });

        return mapView;
    }

    // Ajouter les marqueurs pour les lieux
    private void addMarkers() {
        addMarker(34.0, 9.0);
        addMarker(46.0, 2.0);
        addMarker(42.83333333, 12.83333333);
    }

    // Ajouter un marqueur pour un lieu spécifique
    private void addMarker(double latitude, double longitude) {
        Circle marker = new Circle(5, Color.BLUE);
        marker.setCenterX(longitude);
        marker.setCenterY(latitude);
        mapView.addLayer(new CustomMapLayer(marker));
    }

    private class CustomMapLayer extends MapLayer {
        private Circle marker;

        public CustomMapLayer(Circle marker) {
            this.marker = marker;
            getChildren().add(marker);
        }

        @Override
        protected void layoutLayer() {
            // Ne rien faire ici, les marqueurs sont positionnés directement sur la carte
        }
    }
}
