/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Noah Ernst
 * Created: 4/15/2020
 */

package graph;

import gps.Track;
import gps.Point;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles everything for the LineChart that is displayed when instantiated
 */
public class GraphController {

    @FXML
    LineChart<Double, Double> chart;

    public AnchorPane container;
    public Menu tracksMenu;

    @FXML
    RadioMenuItem dVT;
    @FXML
    RadioMenuItem eVT;
    @FXML
    RadioMenuItem eGVT;
    @FXML
    RadioMenuItem sVT;



    private GraphHandler graphHandler;
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
    @FXML
    Menu unitsMenu;

    /**
     * Draws all tracks to the LineChart
     */
    @FXML
    public void drawAllSelectedTracks(){
        graphHandler = new GraphHandler(chart, selectedTracks,miles.isSelected());
        if(dVT.isSelected()){
            graphHandler.drawAllDistanceGraphs();
            yAxis.setLabel("Distance (km/mi)");
            unitsMenu.setDisable(false);
        }else if(eVT.isSelected()){
            graphHandler.drawAllElevationGraphs();
            yAxis.setLabel("Elevation (m)");
            unitsMenu.setDisable(true);

        }else if(eGVT.isSelected()){
            graphHandler.drawAllElevationGainGraphs();
            yAxis.setLabel("Elevation Gain (m)");
            unitsMenu.setDisable(true);
        }else if(sVT.isSelected()){
            graphHandler.drawAllSpeedGraphs();
            yAxis.setLabel("Speed (km/hr)");
            unitsMenu.setDisable(true);
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
        graphHandler = new GraphHandler(chart, selectedTracks, miles.isSelected());
        graphHandler.drawAllDistanceGraphs();


    }


    public void addRemoveTrack(ActionEvent e) {
        CheckMenuItem i = (CheckMenuItem) e.getSource();
        Track track = null;
        for (Track t:tracks
        ) {
            if (t.getName().equals(i.getText())){
                track = t;
            }
        }
        if (i.isSelected()) {
            selectedTracks.add(track);
            graphHandler.setSelectedTracks(selectedTracks);

        } else {

            selectedTracks.remove(track);
            graphHandler.setSelectedTracks(selectedTracks);

        }
        drawAllSelectedTracks();
    }
}
