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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphController {

    @FXML
    LineChart<Double, Double> chart;

    public AnchorPane container;
    public Menu tracksMenu;
    List<Track> tracks;
    List<Track> selectedTracks;

    @FXML
    NumberAxis xAxis = new NumberAxis();
    @FXML
    NumberAxis yAxis = new NumberAxis();
    @FXML
    RadioMenuItem miles;
    @FXML
    RadioMenuItem kilometers;

    private static final int EARTH_RADIUS_METERS = 6371000;
    private static final double KM_TO_MILES = 0.621371;


    @FXML
    public void drawAllSelectedTracks(){
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawGraph(track.getName());
        }

    }

    public void setTracks(List<Track> tracks) {
        //ignores track if less than 2 points
        this.tracks = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getNumPoints() > 1) {
                this.tracks.add(track);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, track.getName()+" has less than 2 points");
                alert.setHeaderText("Invalid amount of track points");
                alert.showAndWait();

            }
        }

        //draw all loaded tracks initially
        drawAllGraphs();

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
        selectedTracks = new ArrayList<>(this.tracks);


    }

    public void addRemoveTrack(ActionEvent e) {
        CheckMenuItem i = (CheckMenuItem) e.getSource();
//        System.out.println(i.getText() + " was clicked. It is now " + i.isSelected());
        Track track = null;
        for (Track t:tracks
        ) {
            if (t.getName().equals(i.getText())){
                track = t;
            }
        }
        if (i.isSelected()) {
            selectedTracks.add(track);

        } else {

            selectedTracks.remove(track);

        }
        chart.getData().clear();
        drawAllSelectedTracks();
    }


    private void drawAllGraphs(){
        chart.getData().clear();
        for (Track track : this.tracks) {
            drawGraph(track.getName());
        }
    }

    private void drawGraph(String name){
        Track track = null;
        for (Track t:tracks
             ) {
            if (t.getName().equals(name)){
                track = t;
            }
        }
        double distance = 0;
        double time = 0;
        boolean isMiles = false;
        String unit = "km";
        XYChart.Series points = new XYChart.Series();
        if(miles.isSelected()){
            isMiles = true;
            unit = "mi";
        }

        List<Point> pointList = track.getPoints();
        distance = drawPoints(pointList, points, distance, time, isMiles);
        String rounded = String.format("%.3f", distance);
        points.setName(track.getName() + " Total Distance: " + rounded + " " + unit);
        chart.getData().add(points);
    }

    private double drawPoints(List<Point> pointList,  XYChart.Series points, double distance, double time, boolean isMiles){
        for (int i = 0; i < pointList.size()-1; i++){
            if(isMiles){
                distance += KM_TO_MILES*calculateDistance(pointList.get(i), pointList.get(i+1));
            }else{
                distance += calculateDistance(pointList.get(i), pointList.get(i+1));
            }

            time += calculateTime(pointList.get(i), pointList.get(i+1));
            points.getData().add(new XYChart.Data(time, distance));
        }
        return distance;
    }


    private static double calculateDistance(Point pointA, Point pointB){
        double deltaX = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(Math.abs(pointB.getLongitude())) - Math.toRadians(Math.abs(pointA.getLongitude())))
                * Math.cos((Math.toRadians(Math.abs(pointB.getLatitude())) + Math.toRadians(Math.abs(pointA.getLatitude()))) / 2);
        double deltaY = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(pointB.getLatitude()) - Math.toRadians(pointA.getLatitude()));
        double deltaZ = pointB.getElevation() - pointA.getElevation();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) / 1000;
    }

    private static double calculateTime(Point pointA, Point pointB){
        long totalTime = Math.abs(pointB.getDate().getTime() - pointA.getDate().getTime());
        double seconds = totalTime / 1000.0;
        double minutes = seconds / 60;
        return minutes;
    }

}
