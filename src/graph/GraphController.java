/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Noah Ernst, Stuart Harley
 * Created: 4/15/2020
 */

package graph;

import gps.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles everything for the LineChart that is displayed when instantiated
 */
public class GraphController {

    private GraphHandler graphHandler;
    List<Track> tracks;
    List<Track> selectedTracks;

    @FXML
    LineChart<Double, Double> chart;

    @FXML
    private Menu tracksMenu;

    @FXML
    RadioMenuItem dVT;
    @FXML
    RadioMenuItem eVT;
    @FXML
    RadioMenuItem eGVT;
    @FXML
    RadioMenuItem cVT;
    @FXML
    RadioMenuItem sVT;
    @FXML
    RadioMenuItem sVD;

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
    public void drawAllSelectedTracks() {
        graphHandler = new GraphHandler(chart, selectedTracks, miles.isSelected());
        if (dVT.isSelected()) {
            graphHandler.drawAllDistanceGraphs();
            xAxis.setLabel("Time (min)");
            if(miles.isSelected()) {
                yAxis.setLabel("Distance (mi)");
            } else {
                yAxis.setLabel("Distance (km)");
            }
            unitsMenu.setDisable(false);
        } else if (eVT.isSelected()) {
            graphHandler.drawAllElevationGraphs();
            xAxis.setLabel("Time (min)");
            yAxis.setLabel("Elevation (m)");
            unitsMenu.setDisable(true);
        } else if (eGVT.isSelected()) {
            graphHandler.drawAllElevationGainGraphs();
            xAxis.setLabel("Time (min)");
            yAxis.setLabel("Elevation Gain (m)");
            unitsMenu.setDisable(true);
        } else if (cVT.isSelected()) {
            graphHandler.drawAllCaloriesGraphs();
            xAxis.setLabel("Time (min)");
            yAxis.setLabel("Calories Expended (cal)");
            unitsMenu.setDisable(true);
        } else if(sVT.isSelected()) {
            graphHandler.drawAllSpeedVsTimeGraphs();
            xAxis.setLabel("Time (min)");
            yAxis.setLabel("Speed (km/hr)");
            unitsMenu.setDisable(true);
        } else if(sVD.isSelected()) {
            graphHandler.drawAllSpeedVsDistanceGraphs();
            if(miles.isSelected()) {
                xAxis.setLabel("Distance (mi)");
            } else {
                xAxis.setLabel("Distance (km)");
            }
            yAxis.setLabel("Speed (kph)");
            unitsMenu.setDisable(false);
        }
    }

    /**
     * Sets the tracks that are loaded in to selectable menu items. Draws the first distance graph.
     * @param tracks the tracks
     */
    public void setTracks(List<Track> tracks) {
        //ignores track if less than 2 points
        this.tracks = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getNumPoints() > 1) {
                this.tracks.add(track);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        track.getName() + " has less than 2 points");
                alert.setHeaderText("Invalid amount of track points");
                alert.showAndWait();

            }
        }
        for (Track track : this.tracks) {
            CheckMenuItem item = new CheckMenuItem(track.getName());
            item.setOnAction(this::addRemoveTrack);
            item.setSelected(true);
            tracksMenu.getItems().add(item);

        }
        selectedTracks = new ArrayList<>(this.tracks);
        graphHandler = new GraphHandler(chart, selectedTracks, miles.isSelected());
        graphHandler.drawAllDistanceGraphs();
    }


    /**
     * Handles adding or removing a track to the graph on selection from the user
     * @param e the action event of selecting the track menu item
     */
    public void addRemoveTrack(ActionEvent e) {
        CheckMenuItem i = (CheckMenuItem) e.getSource();
        Track track = null;
        for(Track t : tracks) {
            if (t.getName().equals(i.getText())) {
                track = t;
            }
        }
        if(i.isSelected()) {
            selectedTracks.add(track);
        } else {
            selectedTracks.remove(track);
        }
        graphHandler.setSelectedTracks(selectedTracks);
        drawAllSelectedTracks();
    }
}
