package com.example.eventeasy.controllers.front.lieu;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Map  {
    private final MapPoint Tunisia= new MapPoint(34.0,9.0);
    @FXML
    private VBox address;



    public void initialize(){
        MapView mapView = createMapView();
        address.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
    }


    /*

    private MapView mapView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapView = new MapView();
        mapView.setCenter(new MapPoint(0, 0)); // Centrez la carte à la position souhaitée
        mapView.setZoom(10); // Réglez le niveau de zoom de la carte
        rootPane.getChildren().add(mapView); // Ajoutez la carte à votre scène
    }
    public void showMap() {
        mapView.setVisible(true);
    }*/
    private MapView createMapView(){
        MapView mapView =new MapView();
        mapView.setPrefSize(500,400);
        mapView.addLayer(new CustomMapLayer());
        mapView.setZoom(15);
        mapView.flyTo(0,Tunisia,0.1);

        return mapView;
    }
    private class CustomMapLayer extends MapLayer{
        private final Node marker;
        public CustomMapLayer(){
            marker = new Circle(5, Color.RED);
            getChildren().add(marker);
        }
        @Override
        protected void layoutLayer(){
            Point2D point = getMapPoint(Tunisia.getLatitude(), Tunisia.getLongitude());
            marker.setTranslateX(point.getX());
            marker.setTranslateY(point.getY());

        }
    }

}
