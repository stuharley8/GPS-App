package graph;

import gps.Track;
import gps.Point;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GraphController {

    @FXML
    LineChart<Double, Double> chart;

    public AnchorPane container;
    public Menu tracksMenu;
    public Pane mapArea;
    List<Track> tracks;

    @FXML
    NumberAxis xAxis = new NumberAxis();
    @FXML
    NumberAxis yAxis = new NumberAxis();
    private static final double MAP_DIMENSIONS = 500;
    private static final double MAP_CENTER = MAP_DIMENSIONS / 2.0;
    private static final double MAP_SCALE = MAP_DIMENSIONS / 2.0;
    private static final int EARTH_RADIUS_METERS = 6371000;
    private static double MAX_DISTANCE;
    private static double MAX_X;
    private static double MAX_Y;


    public void setTracks(List<Track> tracks) {
        //ignores track if less than 2 points
        this.tracks = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getNumPoints() > 1) {
                this.tracks.add(track);
            }
        }

        for (Track track : this.tracks) {
            double distance = 0;
            double time = 0;
            XYChart.Series points = new XYChart.Series();
            points.setName(track.getName());
            List<Point> pointList = track.getPoints();
            for (int i = 0; i < pointList.size()-1; i++){
                distance += calculateDistance(pointList.get(i), pointList.get(i+1));
                time += calculateTime(pointList.get(i), pointList.get(i+1));
                points.getData().add(new XYChart.Data(time, distance));
            }
            chart.getData().add(points);
        }

        for(Track track : this.tracks) {
            CheckMenuItem item = new CheckMenuItem(track.getName());
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    addRemoveTrack(e);
                }
            });
            item.setSelected(true);
            tracksMenu.getItems().add(item);

        }



    }

    public void addRemoveTrack(ActionEvent e) {
        CheckMenuItem i = (CheckMenuItem) e.getSource();
//        System.out.println(i.getText() + " was clicked. It is now " + i.isSelected());
        if (i.isSelected()) {


        } else {
          //  removeTrackFromScene(i.getText());

        }
        //redrawTable(i.getText(), i.isSelected());
    }

    public void drawGraph(){

    }

//    private void redrawTable(String name, boolean isSelected) {
//        table.redraw(name, isSelected);
//    }
//
//
//    private void removeTrackFromScene(String name) {
//        ObservableList<Node> nodes = mapArea.getChildren();
//
//        for (Node node : nodes) {
//            CanvasLayer layer = (CanvasLayer) node;
//            if (layer.getName().equals(name)) {
//                nodes.remove(node);
//                break;
//            }
//        }
//    }

    public static double calculateDistance(Point pointA, Point pointB){
        double deltaX = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(Math.abs(pointB.getLongitude())) - Math.toRadians(Math.abs(pointA.getLongitude())))
                * Math.cos((Math.toRadians(Math.abs(pointB.getLatitude())) + Math.toRadians(Math.abs(pointA.getLatitude()))) / 2);
        double deltaY = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(pointB.getLatitude()) - Math.toRadians(pointA.getLatitude()));
        double deltaZ = pointB.getElevation() - pointA.getElevation();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) / 1000;
    }

    public static double calculateTime(Point pointA, Point pointB){
        long totalTime = Math.abs(pointB.getDate().getTime() - pointA.getDate().getTime());
        double seconds = totalTime / 1000.0;
        double minutes = seconds / 60;
        return minutes;
    }

}
