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
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * A layer for the canvas containing a track's information
 */
public class CanvasLayer extends Canvas {

    private List<double[]> xyCoordinatesFromOffset = null;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private String name;
    private double originX;
    private double originY;

    public CanvasLayer(Track track, double MAP_DIMENSIONS) {
        super(MAP_DIMENSIONS * 2, MAP_DIMENSIONS * 2);
        if (track == null) {
            this.name = "map element";
        } else {
            this.name = track.getName();
            convertPointsToXY(track);
        }
    }

    private void convertPointsToXY(Track track) {
        List<Point> points = track.getPoints();
        xyCoordinatesFromOffset = new ArrayList<>();
        double[] origin = singleCoordinateConversion(points.get(0));

        originX = origin[0];
        originY = origin[1];

        minX = originX;
        maxX = originX;
        minY = originY;
        maxY = originY;

        double prevX = originX;
        double prevY = originY;

        for (int i = 1; i < points.size(); ++i) {
            double array[] = deltaCoordinateConversion(points.get(i - 1), points.get(i), prevX, prevY);
            xyCoordinatesFromOffset.add(array);
            prevX = array[0];
            prevY = array[1];

            if (prevX < minX) {
                minX = prevX;
            }
            if (prevX > maxX) {
                maxX = prevX;
            }
            if (prevY < minY) {
                minY = prevY;
            }
            if (prevY > maxY) {
                maxY = prevY;
            }
        }
    }

    /**
     * Converts from polar coordinates to cartesian
     *
     * @param point current point
     * @return
     */
    private double[] singleCoordinateConversion(Point point) {
//        double r = 6378137 + (Math.toRadians(point.getElevation()));
        double r = 6378137;
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

//        double r = (6378137 + (current.getElevation() + previous.getElevation()) / 2);
        double r = (6378137);
        double deltaX = r * (currentLong - prevLong) * Math.cos((currentLat + prevLat) / 2);
        double deltaY = r * (currentLat - prevLat);
        return new double[]{prevX + deltaX, prevY + deltaY};
    }

    public String getName() {
        return name;
    }

    public List<double[]> getTrackXYFromOffset() {
        return xyCoordinatesFromOffset;
    }

    public double getOriginX() {
        return originX;
    }

    public double getOriginY() {
        return originY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }
}
