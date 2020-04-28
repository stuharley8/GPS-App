/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import graph.GraphController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import plotter.PlotterController;
import table.TableController;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Controller Class for the GPS FXML
 */
public class Controller {

    private static ObservableList<String> choiceBoxList = FXCollections.observableArrayList();

    private static GPS gps = new GPS();

    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

    static Alert ALERT = new Alert(Alert.AlertType.ERROR);

    private static final double M_2_FT = 3.28084;

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
        if (trackName != null) {
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
        minLatLabel.setText(round(tempTrack.getMinLatitude(), 2) + "");
        minLongLabel.setText(round(tempTrack.getMinLongitude(), 2) + "");
        minEleLabel.setText(round(tempTrack.getMinElevation(), 2) + "m / "
                + round(tempTrack.getMinElevation() * M_2_FT, 2) + "ft");
        maxLatLabel.setText(round(tempTrack.getMaxLatitude(), 2) + "");
        maxLongLabel.setText(round(tempTrack.getMaxLongitude(), 2) + "");
        maxEleLabel.setText(round(tempTrack.getMaxElevation(), 2) + "m / "
                + round(tempTrack.getMaxElevation() * M_2_FT, 2) + "ft");
        maxSpeedMphLabel.setText(round(tempTrack.getMaxSpeedMiles(), 2) + "");
        maxSpeedKphLabel.setText(round(tempTrack.getMaxSpeedKM(), 2) + "");
        aveSpeedMphLabel.setText(round(tempTrack.getAveSpeedMiles(), 2) + "");
        aveSpeedKphLabel.setText(round(tempTrack.getAveSpeedKM(), 2) + "");
        totalDistanceMilesLabel.setText(round(tempTrack.getDistanceMiles(), 2) + "");
        totalDistanceKmLabel.setText(round(tempTrack.getDistanceKM(), 2) + "");
        if (tempTrack.getNumPoints() == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Track Contains 1 Point");
            alert.setHeaderText(tempTrack.getName());
            alert.setContentText("Speed and Distance metrics cannot be calculated for a track with only 1 point");
            alert.showAndWait();
        }
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Track Loaded Successfully");
                alert.setHeaderText(gpsTrackBuilder.loadedTrack().getName());
                alert.setContentText("The track has " + gpsTrackBuilder.loadedTrack().getNumPoints() + " point(s)");
                alert.showAndWait();
            } catch (SAXException e) {
                ALERT.setTitle("Error Dialog");
                ALERT.setHeaderText("Parser SAX Exception");
                ALERT.setContentText(e.getMessage() + "\nThe error occurred near line "
                        + gpsTrackBuilder.getLine() + ", col " + gpsTrackBuilder.getColumn());
                ALERT.showAndWait();
            } catch (NullPointerException e) {
                // If they exit out of the filechooser window without choosing a file
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
        for (int i = gps.getNumTracks() - 1; i < gps.getNumTracks(); i++) {
            choiceBoxList.add(gps.getTrack(i).getName());
            trackChoiceBox.getItems().add(choiceBoxList.get(i));
        }
    }

    private double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Opens up the graph window
     * @throws IOException an IOException
     */
    @FXML
    public void openGraph() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graph/Graph.fxml"));
        Parent part = loader.load();
        GraphController graphController = (GraphController) loader.getController();
        Stage graphStage = new Stage();
        Scene graphScene = new Scene(part);
        graphStage.setScene(graphScene);
        graphStage.setTitle("Graph View");
        graphController.setTracks(gps.getTracks());
        graphStage.show();
    }

    /**
     * Opens the map window
     * @throws IOException an IOException
     */
    @FXML
    public void openPlotter() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../plotter/plotter.fxml"));
        Region contentRootRegion = loader.load();

        PlotterController controller = (PlotterController) loader.getController();
        Stage plotter = new Stage();

        plotter.heightProperty().addListener((o, oldValue, newValue) -> {
            plotter.setHeight(plotter.getWidth() + 50);
        });
        plotter.widthProperty().addListener((o, oldValue, newValue) -> {
            if (plotter.getWidth() < 500) {
                //plotter.setHeight(plotter.getWidth()+50);
                plotter.setWidth(500);
            }
            if (plotter.getWidth() > 1000) {
                plotter.setWidth(1000);
            }
            plotter.setHeight(plotter.getWidth()  + 50);
        });


        //credit to Jason Winnebeck
        //http://gillius.org/blog/2013/02/javafx-window-scaling-on-resize.html
        double origW = 500;
        double origH = 500;

        if (contentRootRegion.getPrefWidth() == Region.USE_COMPUTED_SIZE)
            contentRootRegion.setPrefWidth(origW);
        else
            origW = contentRootRegion.getPrefWidth();

        if (contentRootRegion.getPrefHeight() == Region.USE_COMPUTED_SIZE)
            contentRootRegion.setPrefHeight(origH);
        else
            origH = contentRootRegion.getPrefHeight();

        Group group = new Group(contentRootRegion);
        StackPane rootPane = new StackPane();

        rootPane.getChildren().add(group);

        plotter.setTitle("Track Plotter");
        Scene scene = new Scene(rootPane, origW, origH);
        group.scaleXProperty().bind(scene.widthProperty().divide(origW));
        group.scaleYProperty().bind(group.scaleXProperty());
        plotter.setScene(scene);

        ArrayList<Track> tracks = new ArrayList<>();
        for (int i = 0; i < gps.getNumTracks(); i++) {
            Track t = gps.getTrack(i);
            boolean trackAlreadyLoaded = false;
            for (Track track : tracks) {
                if (track.getName().equals(t.getName())) {
                    trackAlreadyLoaded = true;
                    break;
                }
            }

            if (!trackAlreadyLoaded) {
                tracks.add(gps.getTrack(i));
            }
        }

        controller.setTracks(tracks);

        plotter.show();
    }

     /**
     * Opens up the table window
     * @throws IOException an IOException
     */
    @FXML
    public void openTable() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../table/Table.fxml"));
        Parent part = loader.load();
        TableController tableController = loader.getController();
        Stage tableStage = new Stage();
        Scene tableScene = new Scene(part);
        tableStage.setScene(tableScene);
        tableStage.setTitle("Table View");
        tableController.loadTrackChoices(choiceBoxList);
        tableStage.show();
    }

    public static GPS getGPS() {
        return gps;
    }
}