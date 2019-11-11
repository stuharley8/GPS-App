/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;

/**
 * Controller Class for the GPS FXML
 */
public class Controller {

    private static ObservableList<String> choiceBoxList = FXCollections.observableArrayList();

    private GPS gps = new GPS();

    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

    static Alert ALERT = new Alert(Alert.AlertType.ERROR);

    @FXML
    private ChoiceBox<String> trackChoiceBox;

    @FXML
    private Label metricsLabel;

    @FXML
    private Label minLatLabel;

    @FXML
    private Label minLongLabel;

    @FXML
    private Label minEleLabel;

    @FXML
    private Label maxLatLabel;

    @FXML
    private Label maxLongLabel;

    @FXML
    private Label maxEleLabel;

    @FXML
    private Label maxSpeedMphLabel;

    @FXML
    private Label maxSpeedKphLabel;

    @FXML
    private Label aveSpeedMphLabel;

    @FXML
    private Label aveSpeedKphLabel;

    @FXML
    private Label totalDistanceMilesLabel;

    @FXML
    private Label totalDistanceKmLabel;

    /**
     * Gets the selected value from the choiceBox and attempts to update
     * the metrics labels
     */
    @FXML
    public void displayOnAction() {
        String trackName = trackChoiceBox.getValue();
        int index = -1;
        if(trackName != null) {
            for (int i = 0; i < gps.getNumTracks(); i++) {
                if (trackName.equals(gps.getTrack(i).getName())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                metricsLabel.setText(trackName);
                updateMetrics(index);
            }
        }
    }

    @FXML
    private void updateMetrics(int index) {
        Track tempTrack = gps.getTrack(index);
        minLatLabel.setText(tempTrack.getMinLatitude() + "");
        minLongLabel.setText(tempTrack.getMinLongitude() + "");
        minEleLabel.setText(tempTrack.getMinElevation() + "");
        maxLatLabel.setText(tempTrack.getMaxLatitude() + "");
        maxLongLabel.setText(tempTrack.getMaxLongitude() + "");
        maxEleLabel.setText(tempTrack.getMaxElevation() + "");
        maxSpeedMphLabel.setText(round(tempTrack.getMaxSpeedMiles(),2) + "");
        maxSpeedKphLabel.setText(round(tempTrack.getMaxSpeedKM(), 2) + "");
        aveSpeedMphLabel.setText(round(tempTrack.getAveSpeedMiles(), 2) + "");
        aveSpeedKphLabel.setText(round(tempTrack.getAveSpeedKM(), 2) + "");
        totalDistanceMilesLabel.setText(round(tempTrack.getDistanceMiles(), 2) + "");
        totalDistanceKmLabel.setText(round(tempTrack.getDistanceKM(), 2) + "");
    }

    /**
     * Allows the user to close the application
     */
    @FXML
    public void exitOnAction() {
        Platform.exit();
    }

    /**
     * Allows the user to select a GPS file. The file will be parsed and the track data
     * loaded into the gps object. The choiceBox will be updated to reflect the new track.
     */
    @FXML
    public void loadOnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "GPS Files", "*.txt", "*.gpx"));
        fileChooser.setInitialDirectory(new File(currentPath));
        try {
            File file = fileChooser.showOpenDialog(null);
            GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
            gpsTrackBuilder.enableLogging(true);
            Parser parser;
            try {
                parser = new Parser(gpsTrackBuilder);
                parser.parse(file.toString());
                gps.addTrack(gpsTrackBuilder.loadedTrack());
                updateChoiceBox();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Track Loaded Successfully");
                alert.setHeaderText(gpsTrackBuilder.loadedTrack().getName());
                alert.setContentText("The track has " + gpsTrackBuilder.loadedTrack().getNumPoints() + " point(s)");
                alert.showAndWait();
            } catch (SAXException e){
                ALERT.setTitle("Error Dialog");
                ALERT.setHeaderText("Parser SAX Exception");
                ALERT.setContentText(e.getMessage() + "\nThe error occurred near line "
                        + gpsTrackBuilder.getLine() + ", col " + gpsTrackBuilder.getColumn());
                ALERT.showAndWait();
            }
        } catch (Exception e) {
            ALERT.setTitle("Error Dialog");
            ALERT.setHeaderText("File Error");
            ALERT.setContentText(e.getMessage());
            ALERT.showAndWait();
        }
    }

    @FXML
    private void updateChoiceBox() {
        for(int i = gps.getNumTracks()-1; i < gps.getNumTracks(); i++) {
            choiceBoxList.add(gps.getTrack(i).getName());
            trackChoiceBox.getItems().add(choiceBoxList.get(i));
        }
    }

    private static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}