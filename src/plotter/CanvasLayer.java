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
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
    private Color color;

    private String name;
    private double originX;
    private double originY;
    private boolean isSelected;

    private ArrayList<Integer> gradeArray;// = new ArrayList<>();
    private ArrayList<Double> speedArray;// = new ArrayList<>();
    
    //MLH: Public methods should have complete javadoc

    public CanvasLayer(Track track, double MAP_DIMENSIONS) {
        super(MAP_DIMENSIONS + 10, MAP_DIMENSIONS + 10);

        isSelected = true;
        if (track == null) {
            this.name = "map element";
        } else {
            gradeArray = new ArrayList<>();
            speedArray = new ArrayList<>();
            this.name = track.getName();
            minLatitude = track.getMinLatitude();
            maxLatitude = track.getMaxLatitude();
            minLongitude = track.getMinLongitude();
            maxLongitude = track.getMaxLongitude();
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
            insertGradeIntoArray(points.get(i - 1), points.get(i));

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


    private void insertGradeIntoArray(Point a, Point b) {
        double grade = Track.gradeCalc(a, b);
        double time = calculateTime(a, b);
        double distance = Track.distanceCalc(a, b);
        double speed = distance / (time / 60);
        speedArray.add(speed);

        if (grade < -5) {
            gradeArray.add(0);
        } else if (grade >= -5 && grade < -1) {
            gradeArray.add(1);
        } else if (grade >= -1 && grade < 1) {
            gradeArray.add(2);
        } else if (grade >= 1 && grade < 3) {
            gradeArray.add(3);
        } else if (grade >= 3 && grade < 5) {
            gradeArray.add(4);
        } else {
            gradeArray.add(5);
        }
    }

    /**
     * Converts from polar coordinates to cartesian
     *
     * @param point current point
     * @return
     */
    private static double[] singleCoordinateConversion(Point point) {
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
    private static double[] deltaCoordinateConversion(Point previous, Point current, double prevX, double prevY) {
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public Color getColor(String functionName, int index) {
        if (functionName.equals(PlotterUtilities.gradeFunction)) {
            return PlotterUtilities.getColor(gradeArray.get(index));
        }
        return color;
    }

    public double getSpeedAtIndex(int index) {
        return speedArray.get(index);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private double calculateTime(Point pointA, Point pointB) {
        long totalTime = Math.abs(pointB.getDate().getTime() - pointA.getDate().getTime());
        double seconds = totalTime / 1000.0;
        return seconds / 60;
    }


}
