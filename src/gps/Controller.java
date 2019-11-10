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
import java.nio.file.Paths;

/**
 * Controller Class for the GPS FXML
 */
public class Controller {

    private static ObservableList<String> choiceBoxList = FXCollections.observableArrayList();

    private GPS gps = new GPS();

    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

    @FXML
    private ChoiceBox<String> trackChoiceBox;

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
        maxSpeedMphLabel.setText(tempTrack.getMaxSpeedMiles() + "");
        maxSpeedKphLabel.setText(tempTrack.getMaxSpeedKM() + "");
        aveSpeedMphLabel.setText(tempTrack.getAveSpeedMiles() + "");
        aveSpeedKphLabel.setText(tempTrack.getAveSpeedKM() + "");
        totalDistanceMilesLabel.setText(tempTrack.getDistanceMiles() + "");
        totalDistanceKmLabel.setText(tempTrack.getDistanceKM() + "");
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
                "GPS Files", "*.txt", ".gpx"));
        fileChooser.setInitialDirectory(new File(currentPath));
        try {
            File file = fileChooser.showOpenDialog(null);
            GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
            AbstractParserEventHandler handler = gpsTrackBuilder;
            handler.enableLogging(true);
            Parser parser;
            try {
                parser = new Parser(handler);
                parser.parse(file.toString());
                gps.addTrack(gpsTrackBuilder.loadedTrack());
            } catch (SAXException e){
                System.out.println("Parser threw SAXException: " + e.getMessage());
                System.out.println("The error occurred near line " + handler.getLine() + ", col " + handler.getColumn());
            } catch (Exception e) {
                System.out.println("Parser threw Exception: " + e.getMessage());
            }
            //TODO: Parser Stuff
            loadChoiceBox();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("File Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void loadChoiceBox() {
        trackChoiceBox.getItems().removeAll();
        choiceBoxList.removeAll();
        for(int i = 0; i < gps.getNumTracks(); i++) {
            choiceBoxList.add(gps.getTrack(i).getName());
        }
        trackChoiceBox.getItems().addAll(choiceBoxList);
    }

}