package gps;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
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

    private static final double MAP_DIMENSIONS = 500;
    private static final double MAP_CENTER = MAP_DIMENSIONS / 2.0;
    private static final double MAP_SCALE = MAP_DIMENSIONS / 2.0;
    private static double MAX_DISTANCE;
    private static double MAX_X;
    private static double MAX_Y;
    private PlotterTable table;

    public void setTracks(List<Track> tracks) {
        //ignores track if less than 2 points
        this.tracks = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getPoints().size() > 1) {
                this.tracks.add(track);
            }
        }

        table = new PlotterTable(this.tracks);

        for (Track track : this.tracks) {
            Point minPoint = new Point(track.getMinLatitude(), track.getMinLongitude());
            Point maxPoint = new Point(track.getMaxLatitude(), track.getMaxLongitude());
            double [] array = coordConverstion(minPoint, maxPoint, 0,0);
            if (array[0] > MAX_X) {
                MAX_X = array[0];
            }
            if (array[1] > MAX_Y) {
                MAX_Y = array[1];
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



    }

    public void addRemoveTrack(ActionEvent e) {
        CheckMenuItem i = (CheckMenuItem) e.getSource();
//        System.out.println(i.getText() + " was clicked. It is now " + i.isSelected());
        if (i.isSelected()) {
            drawPoints(getTrackInTracks(i.getText()));

        } else {
            removeTrackFromScene(i.getText());

        }
        redrawTable(i.getText(), i.isSelected());
    }

    private void redrawTable(String name, boolean isSelected) {
        table.redraw(name, isSelected);
    }


    private void removeTrackFromScene(String name) {
        ObservableList<Node> nodes = mapArea.getChildren();

        for (Node node : nodes) {
            CanvasLayer layer = (CanvasLayer) node;
            if (layer.getName().equals(name)) {
                nodes.remove(node);
                break;
            }
        }
    }

}
