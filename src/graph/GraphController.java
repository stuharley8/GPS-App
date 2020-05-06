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

    private static final int EARTH_RADIUS_METERS = 6371000;
    private static final double KM_TO_MILES = 0.621371;


    @FXML
    public void drawAllSelectedTracks(){
        graphHandler.drawAllGraphs();
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
        graphHandler.drawAllGraphs();


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
        graphHandler.drawAllGraphs();
    }
}
