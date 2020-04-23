package plotter;

import gps.Track;
import gps.Point;
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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PlotterController {

    public AnchorPane container;
    public Menu tracksMenu;
    public Pane mapArea;
    List<Track> tracks;

    private static final double MAP_DIMENSIONS = 500;
    private static final double MAP_CENTER = MAP_DIMENSIONS / 2.0;
    private static final double MAP_OFFSET = MAP_DIMENSIONS / 2.0;
    private static double MAX_X;
    private static double MAX_Y;
    private static double originX = Double.MAX_VALUE;
    private static double originY = Double.MAX_VALUE;

    private double minLongCoordinate;
    private double maxLongCoordinate;
    private double minLatCoordinate;
    private double maxLatCoordinate;

    private double shrinkFactor = 1.0;

    private double epsilon = 0.000001;

    private PlotterTable table;
    private Stage stage;

    public void setTracks(List<Track> tracks) {
        originX = Double.MAX_VALUE;
        originY = Double.MAX_VALUE;
        //ignores track if less than 2 points
        this.tracks = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getPoints().size() > 1) {
                this.tracks.add(track);
            }
        }

        table = new PlotterTable(this.tracks);

        double minLongitude = tracks.get(0).getMinLongitude();
        double maxLongitude = tracks.get(0).getMaxLongitude();
        double minLatitude = tracks.get(0).getMinLatitude();
        double maxLatitude = tracks.get(0).getMaxLatitude();

        for (Track track : this.tracks) {
            Point minPoint = new Point(track.getMinLatitude(), track.getMinLongitude());
            Point maxPoint = new Point(track.getMaxLatitude(), track.getMaxLongitude());
            double[] array = deltaCoordinateConversion(minPoint, maxPoint, 0, 0);
            if (array[0] > MAX_X) {
                MAX_X = array[0];
            }
            if (array[1] > MAX_Y) {
                MAX_Y = array[1];
            }

            if (minPoint.getLongitude() < minLongitude) {
                minLongitude = minPoint.getLongitude();
            }
            if (maxPoint.getLongitude() > maxLongitude) {
                maxLongitude = maxPoint.getLongitude();
            }
            if (minPoint.getLatitude() < minLatitude) {
                minLatitude = minPoint.getLatitude();
            }
            if (maxPoint.getLatitude() > maxLatitude) {
                maxLatitude = maxPoint.getLatitude();
            }
        }

        Point minPoint = new Point(minLatitude, minLongitude);
        Point maxPoint = new Point(maxLatitude, maxLongitude);

        double[] min = singleCoordinateConversion(minPoint);
        double[] max = singleCoordinateConversion(maxPoint);

        minLongCoordinate = min[0];
        maxLongCoordinate = max[0];
        minLatCoordinate = min[1];
        maxLatCoordinate = max[1];

        double shrinkX = MAX_X / Math.abs(maxLongCoordinate - minLongCoordinate);
        double shrinkY = MAX_Y / Math.abs(maxLatCoordinate - minLatCoordinate);

        shrinkFactor = (shrinkX < shrinkY) ? shrinkX : shrinkY;
//        shrinkFactor = shrinkX;

        //draw x,y axis
        CanvasLayer canvasLayer = new CanvasLayer("axis", MAP_DIMENSIONS);
        GraphicsContext gc = canvasLayer.getGraphicsContext2D();
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.strokeLine(MAP_CENTER + MAP_OFFSET, 0, MAP_CENTER + MAP_OFFSET, MAP_DIMENSIONS);
        gc.strokeLine(0, MAP_CENTER, MAP_DIMENSIONS * 2, MAP_CENTER);

        mapArea.getChildren().add(canvasLayer);

        for (Track track : this.tracks) {
            CheckMenuItem item = new CheckMenuItem(track.getName());
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    addRemoveTrack(e);
                }
            });
            item.setSelected(true);
            tracksMenu.getItems().add(item);

            drawPoints(track);
        }
        mapArea.getChildren().add(table);
    }

    private double normalizeX(double coordinate) {
        double abs = Math.abs(minLongCoordinate);
        double scalarMax = maxLongCoordinate + abs; //might need this again
        double x = coordinate + abs;
        if (scalarMax > epsilon) {
            x /= scalarMax;
            x *= MAP_DIMENSIONS;
            return x;
        }
        return 0;
    }

    private double normalizeY(double coordinate) {
        double scalarMax = maxLatCoordinate - minLatCoordinate; //might need this again
        if (scalarMax > epsilon) {
            double y = coordinate - minLatCoordinate;
            y /= scalarMax;
            y = 1 - y;
            y *= MAP_DIMENSIONS * (Math.abs(maxLatCoordinate-minLatCoordinate) / Math.abs(maxLongCoordinate-minLongCoordinate));
            return y;
        }
        return 0;
    }

    public void addRemoveTrack(ActionEvent e) {
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

    private Track getTrackInTracks(String name) {
        for (Track track : tracks) {
            if (track.getName().equals(name)) {
                return track;
            }
        }
        return null;
    }

    private double getXScalar(double longitude) {
//        return (longitude - minLongCoordinate) / (maxLongCoordinate - minLongCoordinate);
        return (longitude - minLongCoordinate) / (maxLongCoordinate - minLongCoordinate);
    }

    private double getYScalar(double latitude) {
        return (latitude - minLatCoordinate) / (maxLatCoordinate - minLatCoordinate);
    }

    private void drawPoints(Track track) {
        CanvasLayer canvas = new CanvasLayer(track.getName(), MAP_DIMENSIONS);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Color color = new Color(Math.random(), Math.random(), Math.random(), 1.0);
        table.setTrackColor(track.getName(), color);
        gc.setFill(color);
        gc.setStroke(color);

        List<Point> points = track.getPoints();

        List<double[]> converted = new ArrayList<>();
        double prevX = 0.0;
        double prevY = 0.0;

        for (int i = 1; i < points.size(); ++i) {
            double array[] = deltaCoordinateConversion(points.get(i - 1), points.get(i), prevX, prevY);
            converted.add(array);
            prevX = array[0];
            prevY = array[1];
        }

        double[] maxMin = getLatLongMaxMin(converted);
        double longitudeMin = maxMin[0];
        double longitudeMax = maxMin[1];
        double latitudeMin = maxMin[2];
        double latitudeMax = maxMin[3];

        double[] origin = singleCoordinateConversion(track.getPoints().get(0));
        double startX = normalizeX(origin[0]);
        double startY = normalizeY(origin[1]);

        double epsilon = .000001;
        double latRatio = (Math.abs(latitudeMax - latitudeMin) / Math.abs(longitudeMax - longitudeMin));
        if (Double.isNaN(latRatio)) {
            latRatio = 0;
        }
        double longRatio = (Math.abs(longitudeMax - longitudeMin) / Math.abs(latitudeMax - latitudeMin));
        if (Double.isNaN(longRatio)) {
            longRatio = 0;
        }

        double[] zeroXValues = applyXTransformation(converted.get(0)[0], longitudeMin, longitudeMax, longRatio);
        double zeroY = applyYTransformation(converted.get(0)[1], latitudeMin, latitudeMax, latRatio, zeroXValues[1]);

        if (originX == Double.MAX_VALUE) {
            double[] values = singleCoordinateConversion(tracks.get(0).getPoints().get(0));
            originX = normalizeX(values[0]);
            originY = normalizeY(values[1]) + zeroY;
        }

        gc.setLineWidth(2);
        gc.moveTo(MAP_OFFSET + (startX - originX) + MAP_CENTER, (startY - originY) + MAP_CENTER + zeroY);

        for (double[] point : converted) {

            double[] xValues = applyXTransformation(point[0], longitudeMin, longitudeMax, longRatio);
            double longX = xValues[0];
            double latY = applyYTransformation(point[1], latitudeMin, latitudeMax, latRatio, xValues[1]);

            if (Math.abs(latitudeMax - latitudeMin) < epsilon) {
                gc.lineTo(MAP_OFFSET + (startX - originX)  + longX,  MAP_CENTER ); //THIS IS WRONG

            } else {
//            gc.fillOval(MAP_OFFSET + (startX - originX) + MAP_CENTER + longX, (startY - originY) + MAP_CENTER + latY, 2, 2);
//            gc.fillOval(MAP_CENTER + longX, MAP_CENTER + latY - zeroY, 1, 1);
                gc.lineTo(MAP_OFFSET + (startX - originX) + MAP_CENTER + longX, (startY - originY) + MAP_CENTER + latY);
            }
        }

        gc.stroke();

        mapArea.getChildren().add(canvas);
    }

    /**
     * Converts from polar coordinates to cartesian
     *
     * @param point current point
     * @return
     */
    private double[] singleCoordinateConversion(Point point) {
        double r = 6378137 + (Math.toRadians(point.getElevation()));
        double X = r * (Math.toRadians(point.getLongitude())) * Math.cos(Math.toRadians(point.getLatitude()));
        double Y = r * (Math.toRadians(point.getLatitude()));

        return new double[]{X, Y};
    }

    /**
     * Converts from polar coordinates to cartesian
     *
     * @param previous previous point
     * @param current  current point
     * @param prevX    previous point's converted x value
     * @param prevY    previous point's converted y value
     * @return
     */
    private double[] deltaCoordinateConversion(Point previous, Point current, double prevX, double prevY) {
        double prevLong = Math.toRadians(previous.getLongitude());
        double prevLat = Math.toRadians(previous.getLatitude());
        double currentLong = Math.toRadians(current.getLongitude());
        double currentLat = Math.toRadians(current.getLatitude());

        double r = (6378137 + (current.getElevation() + previous.getElevation()) / 2);
        double deltaX = r * (currentLong - prevLong) * Math.cos((currentLat + prevLat) / 2);
        double deltaY = r * (currentLat - prevLat);
        return new double[]{prevX + deltaX, prevY + deltaY};
    }


    /**
     * Transforms stop latitude into transform y position map coordinates
     *
     * @param latitude    the latitude of the stop
     * @param latitudeMin the minimum latitude of the track
     * @param latitudeMax the maximum latitude of the track
     * @return the transformed y coordinate
     * @author Matt Haas
     */
    private double applyYTransformation(double latitude, double latitudeMin, double latitudeMax, double ratio, double scalarX) {
        double scalarMax = latitudeMax - latitudeMin; //might need this again
        if (scalarMax > epsilon) {
            double y = latitude - latitudeMin;
            y /= scalarMax;
            y *= scalarX;
            y = 1 - y;
//        y *= MAP_SCALE;
            y *= MAP_DIMENSIONS * shrinkFactor;
            if (ratio < 1) {
                y *= ratio;
            }
            return y;
        }
        return 0;
    }

    /**
     * Transforms stop latitude into transform x position map coordinates
     *
     * @param longitude    the longitude of the stop
     * @param longitudeMin the minimum longitude of the track
     * @param longitudeMax the maximum longitude of the track
     * @return the transformed x coordinate
     * @author Matt Haas
     */
    private double[] applyXTransformation(double longitude, double longitudeMin, double longitudeMax, double ratio) {
        double abs = Math.abs(longitudeMin);
        double scalarMax = longitudeMax + abs; //might need this again
        double x = longitude + abs;
        double scalarX = (scalarMax / MAX_X);
        if (scalarMax > epsilon){
            x /= scalarMax;
            x *= scalarX;
//        x *= MAP_SCALE;
            x *= MAP_DIMENSIONS * shrinkFactor;
            if (ratio < 1) {
                x *= ratio;
            }
            return new double[]{x, scalarX};
        }
        if (maxLatCoordinate - minLatCoordinate > epsilon) {
            return new double[] {(maxLatCoordinate - minLatCoordinate) / (maxLongCoordinate - minLongCoordinate), scalarX};
        }
        return new double[]{getXScalar(longitude), scalarX};
    }




    /**
     * Normalizes range of latitude and longitude to values between 0 and 1
     *
     * @param maxMin the max and min data of the track
     * @return array containing normalized max and min date for lat and long
     */
    private double[] normalizeMaxMin(double[] maxMin) {
        double offset = 10;
        double shrinkFactor = .9;
        maxMin[1] -= Math.abs(maxMin[0]); //latitude - absVal
        maxMin[0] -= Math.abs(maxMin[0]);
        maxMin[3] += Math.abs(maxMin[2]); //longitude + absVal
        maxMin[2] += Math.abs(maxMin[2]);

        double max = Double.max(maxMin[1], maxMin[3]);
        for (int i = 0; i < maxMin.length; i++) {
            maxMin[i] /= max;
            if (i < 2) {
                maxMin[i] = 1 - maxMin[i];
            }
        }
        return maxMin;
    }

    /**
     * Gets maximum and minimum value of longitude and latitude over every point in a track
     *
     * @param coordinateList list of stop lat and long for every point
     * @return double array of minimum and maximum latitude and minimum and maximum longitude
     * @author Matt Haas
     */
    private double[] getLatLongMaxMin(List<double[]> coordinateList) {
        double[] maxMin = new double[4];
        double latMax = -Double.MAX_VALUE;
        double latMin = Double.MAX_VALUE;
        double longMax = -Double.MAX_VALUE;
        double longMin = Double.MAX_VALUE;

        for (double[] array : coordinateList) {
            if (array[0] < latMin) {
                latMin = array[0];
            }
            if (array[0] > latMax) {
                latMax = array[0];
            }
            //longitude is negative
            if (array[1] < longMin) {
                longMin = array[1];
            }
            if (array[1] > longMax) {
                longMax = array[1];
            }
        }
        maxMin[0] = latMin;
        maxMin[1] = latMax;
        maxMin[2] = longMin;
        maxMin[3] = longMax;
        return maxMin;
    }

    public void setStage(Stage plotter) {
        this.stage = plotter;
    }
}