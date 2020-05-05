/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Matt Haas
 * Created: 4/25/2020
 */

package plotter;

import gps.Point;
import gps.Track;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and draws tracks onto a cartesian map.
 */
public class PlotterController {

    public AnchorPane container;
    public Menu tracksMenu;
    public Pane mapArea;
    private List<Track> tracks;
    private List<CanvasLayer> canvasTracks;

    private double minXBound = Double.MAX_VALUE;
    private double maxXBound = -Double.MAX_VALUE;
    private double minYBound = Double.MAX_VALUE;
    private double maxYBound = -Double.MAX_VALUE;

    private static final double PADDING = 5;
    private static final double MAP_DIMENSIONS = 500;
    private static final double MAP_CENTER = (MAP_DIMENSIONS + (2 * PADDING)) / 2;

    private double originX;
    private double originY;

    private double xRatio;
    private double yRatio;

    private double maxXDistance;
    private double maxYDistance;

    private double yOffset;
    private double xOffset;

    private PlotterTable table;

    public void setTracks(List<Track> tracks) {
        originX = Double.MAX_VALUE;
        originY = Double.MAX_VALUE;

        //ignores track if less than 2 points
        this.tracks = new ArrayList<>();
        this.canvasTracks = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getPoints().size() > 1) {
                this.tracks.add(track);
            }
        }

        table = new PlotterTable(this.tracks);

        convertTracks();
        getBounds();
        setRatios();

        for (Track track : this.tracks) {
            CheckMenuItem item = new CheckMenuItem(track.getName());
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    addRemoveTrack(e);
                }
            });
            item.setSelected(true);
            tracksMenu.getItems().add(item);
        }

        drawTracks();
        drawMarkerLabels();

        mapArea.getChildren().add(table);
    }

    /**
     * Draws scaling marker units for visualizing distance
     */
    private void drawMarkerLabels() {
        CanvasLayer canvasLayer = new CanvasLayer(null, MAP_DIMENSIONS);
        GraphicsContext gc = canvasLayer.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);

        gc.setLineWidth(1);
        double labelY = 480;

        double distance = (maxXDistance > maxYDistance) ? maxXDistance : maxYDistance;
        double interval = distance / 10;

        int roundedInterval = ((int) (interval / 10) + 1) * 10;
        double pixelInterval = 50;
        double labelLineDistance = (roundedInterval * pixelInterval) / interval;

        DecimalFormat df = new DecimalFormat("#.00");

        double intervalToMiles = (double)roundedInterval * 0.621371192;
        while (labelLineDistance > 400) {
            roundedInterval /= 2;
            labelLineDistance /= 2;
        }

        double markerOffset = PADDING + 10;

        gc.moveTo(markerOffset, labelY);
        gc.lineTo(markerOffset + labelLineDistance, labelY);
        gc.strokeLine(markerOffset, labelY - 3, markerOffset, labelY + 3);
        gc.strokeLine(markerOffset + labelLineDistance, labelY - 3, markerOffset + labelLineDistance, labelY + 3);
        gc.strokeText(roundedInterval + " km", markerOffset + labelLineDistance + 5, labelY - 7);
        gc.strokeText(df.format(intervalToMiles) + " miles", markerOffset + labelLineDistance + 5, labelY + 10);
        gc.stroke();

        mapArea.getChildren().add(canvasLayer);
    }

    private double[] getDistance() {
        double minLat = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE;
        double minLong = Double.MAX_VALUE;
        double maxLong = -Double.MAX_VALUE;
        for (Track track : tracks) {
            if (minLat > track.getMinLatitude()) {
                minLat = track.getMinLatitude();
            }
            if (maxLat < track.getMaxLatitude()) {
                maxLat = track.getMaxLatitude();
            }
            if (minLong > track.getMinLongitude()) {
                minLong = track.getMinLongitude();
            }
            if (maxLong < track.getMaxLongitude()) {
                maxLong = track.getMaxLongitude();
            }
        }
        Point minXPoint = new Point(minLat, minLong);
        Point maxXPoint = new Point(minLat, maxLong);
        Point minYPoint = new Point(minLat, minLong);
        Point maxYPoint = new Point(maxLat, minLong);

        double x = Track.distanceCalc(maxXPoint, minXPoint);
        double y = Track.distanceCalc(maxYPoint, minYPoint);

        return new double[]{x, y};
    }

    private void setRatios() {
        yRatio = (Math.abs(maxYBound - minYBound) / Math.abs(maxXBound - minXBound));

        xRatio = (Math.abs(maxXBound - minXBound) / Math.abs(maxYBound - minYBound));
        if (xRatio > 1) {
            xRatio = 1;
        }
        if (yRatio > 1) {
            yRatio = 1;
        }
    }

    private void drawTracks() {
        for (CanvasLayer track : canvasTracks) {
            drawPoints(track);
        }
    }

    private void getBounds() {
        for (CanvasLayer track : canvasTracks) {
            if (track.getMinX() < minXBound) {
                minXBound = track.getMinX();
            }
            if (track.getMaxX() > maxXBound) {
                maxXBound = track.getMaxX();
            }
            if (track.getMinY() < minYBound) {
                minYBound = track.getMinY();
            }
            if (track.getMaxY() > maxYBound) {
                maxYBound = track.getMaxY();
            }
        }
    }

    /**
     * Conversion from gps to cartesian is stored in CanvasLayer object
     */
    private void convertTracks() {
        for (Track track : tracks) {
            canvasTracks.add(new CanvasLayer(track, MAP_DIMENSIONS));
        }
    }

    /**
     * Does conversions to map scale and draws track on map
     *
     * @param track The track to draw
     */
    private void drawPoints(CanvasLayer track) {
        if (originX == Double.MAX_VALUE) {
            originX = scaleX(track.getOriginX());
            originY = scaleY(track.getOriginY(), 0);
            drawOriginAxis();
        }
        double trackOriginX = scaleX(track.getOriginX());
        double trackOriginY = scaleY(track.getOriginY(), originY);

        GraphicsContext gc = track.getGraphicsContext2D();
        Color color = new Color(Math.random(), Math.random(), Math.random(), 1.0);
        table.setTrackColor(track.getName(), color);
        gc.setFill(color);
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.moveTo(xOffset + trackOriginX, yOffset + trackOriginY);

        for (double[] xy : track.getTrackXYFromOffset()) {
            double x = scaleX(xy[0]);
            double y = scaleY(xy[1], originY);
            gc.lineTo(xOffset + x, yOffset + y);
        }
        gc.stroke();
        mapArea.getChildren().add(track);
    }

    private void drawOriginAxis() {
        //offsets for centering
        double lowY = scaleY(minYBound, originY);
        double highY = scaleY(maxYBound, originY);
        double midY = (lowY - highY) / 2;

        double highX = scaleX(maxXBound);

        yOffset = MAP_CENTER - midY + originY;
        xOffset = (MAP_DIMENSIONS - highX) / 2 + PADDING;

        //draw axis
        CanvasLayer canvasLayer = new CanvasLayer(null, MAP_DIMENSIONS);
        GraphicsContext gc = canvasLayer.getGraphicsContext2D();
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        double offset = xOffset + originX;
        gc.strokeLine(offset, PADDING, offset, MAP_CENTER * 2);
        gc.strokeLine( PADDING, yOffset, MAP_DIMENSIONS + PADDING, yOffset);
        for (int i = 0; i < 11; ++i) {
            gc.strokeLine(offset + (i * 50), yOffset - 5, offset + (i * 50), yOffset + 5);
            gc.strokeLine(offset - (i * 50), yOffset - 5, offset - (i * 50), yOffset + 5);
            gc.strokeLine(offset - 5, yOffset + (i * 50), offset + 5, yOffset + (i * 50));
            gc.strokeLine(offset - 5, yOffset - (i * 50), offset + 5, yOffset - (i * 50));
        }

        double[] distances = getDistance();

        maxXDistance = distances[0];
        maxYDistance = distances[1];

        mapArea.getChildren().add(canvasLayer);
    }

    /**
     * Scales and transforms x value into map xy coordinates
     *
     * @param xValue value to convert
     * @return scaled x value
     */
    private double scaleX(double xValue) {
        double abs = Math.abs(minXBound);
        double scalarMax = maxXBound + abs; //might need this again
        double x = xValue + abs;
        if (maxXBound - minXBound > .000001) {
            x /= scalarMax;
        }
        x *= MAP_DIMENSIONS;
        x *= xRatio;
        return x;
    }

    /**
     * Scales and transforms Y value into map xy coordinates
     *
     * @param yValue value to convert
     * @return scaled Y value
     */
    private double scaleY(double yValue, double originOffset) {
        double y = yValue - minYBound;
        double scalarMax = maxYBound - minYBound; //might need this again
        if (scalarMax > .000001) {
            y /= scalarMax;
        }
        y = 1 - y;
        y *= MAP_DIMENSIONS;
        y *= yRatio;
        return y - originOffset;
    }

    private void addRemoveTrack(ActionEvent e) {
        CheckMenuItem i = (CheckMenuItem) e.getSource();
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

    private CanvasLayer getTrackInTracks(String name) {
        for (CanvasLayer track : canvasTracks) {
            if (track.getName().equals(name)) {
                return track;
            }
        }
        return null;
    }
}