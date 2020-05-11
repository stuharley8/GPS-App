/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Noah Ernst, Stuart Harley
 * Created: 4/15/2020
 */

package graph;

import gps.Point;
import gps.Track;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * Class that handles drawing the different types of graphs
 */
public class GraphHandler {

    private static final double KM_TO_MILES = 0.621371;

    private LineChart<Double, Double> chart;
    private boolean isMiles;
    List<Track> selectedTracks;


    /**
     * Constructor
     * @param chart the chart to draw on
     * @param selectedTracks the tracks to draw
     * @param isMiles whether the distance is in miles or kilometers
     */
    public GraphHandler(LineChart<Double, Double> chart, List<Track> selectedTracks, boolean isMiles) {
        this.selectedTracks = selectedTracks;
        this.chart = chart;
        this.isMiles = isMiles;
    }

    /**
     * Draws all the distance graph lines for the selected tracks
     */
    public void drawAllDistanceGraphs() {
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawDistanceGraph(track);
        }
    }

    /**
     * Draws all the elevation graph lines for the selected tracks
     */
    public void drawAllElevationGraphs() {
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawElevationGraph(track);
        }
    }

    /**
     * Draws all the elevation grain graph lines for the selected tracks
     */
    public void drawAllElevationGainGraphs() {
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawElevationGainGraph(track);
        }
    }

    /**
     * Draws all the calories expended graph lines for the selected tracks
     */
    public void drawAllCaloriesGraphs() {
        chart.getData().clear();
        for(Track track : this.selectedTracks) {
            drawCaloriesGraph(track);
        }
    }

    public void setSelectedTracks(List<Track> selectedTracks) {
        this.selectedTracks = selectedTracks;
    }

    private void drawCaloriesGraph(Track track) {
        XYChart.Series points = new XYChart.Series();
        int calories = 0;
        double time = 0;
        for (int i = 0; i < track.getNumPoints() - 1; i++) {
            time += calculateTime(track.getPoint(i), track.getPoint(i + 1));
            calories += Track.calorieCount(track.getPoint(i), track.getPoint(i + 1));
            XYChart.Data point = new XYChart.Data(time, calories);
            Circle circle = new Circle(1.0);
            circle.setVisible(false);
            point.setNode(circle);
            points.getData().add(point);
        }
        points.setName(track.getName() + " Calories: " + calories + "cal");
        chart.getData().add(points);
    }

    private void drawElevationGraph(Track track) {
        XYChart.Series points = new XYChart.Series();
        double gain = 0;
        double time = 0;
        double initialTime = track.getPoint(0).getDate().getTime();
        double lastElevation = 0;
        for (int i = 0; i < track.getNumPoints(); i++) {
            time += ((track.getPoint(i).getDate().getTime() - initialTime) / 1000) / 60;
            XYChart.Data point = new XYChart.Data(time, track.getPoint(i).getElevation());
            Circle circle = new Circle(1.0);
            circle.setVisible(false);
            point.setNode(circle);
            points.getData().add(point);
            if (track.getPoint(i).getElevation() >= lastElevation) {
                gain = +track.getPoint(i).getElevation() - lastElevation;
            }
            lastElevation = track.getPoint(i).getElevation();
        }
        String rounded = String.format("%.3f", gain);
        points.setName(track.getName() + " Elevation Gain: " + rounded + " m");
        chart.getData().add(points);
    }

    private void drawElevationGainGraph(Track track) {
        XYChart.Series points = new XYChart.Series();
        double gain = 0;
        double time = 0;
        for (int i = 0; i < track.getNumPoints() - 1; i++) {
            time += calculateTime(track.getPoint(i), track.getPoint(i + 1));
            gain += calculateElevationGain(track.getPoint(i), track.getPoint(i + 1));
            XYChart.Data point = new XYChart.Data(time, gain);
            Circle circle = new Circle(1.0);
            circle.setVisible(false);
            point.setNode(circle);
            points.getData().add(point);
        }
        String rounded = String.format("%.3f", gain);
        points.setName(track.getName() + " Elevation Gain: " + rounded + " m");
        chart.getData().add(points);

    }

    private void drawDistanceGraph(Track track) {
        double distance = 0;
        double time = 0;
        String unit = "km";
        XYChart.Series points = new XYChart.Series();
        if (isMiles) {
            unit = "mi";
        }
        distance = drawDistancePoints(track, points, distance, time, isMiles);
        String rounded = String.format("%.3f", distance);
        points.setName(track.getName() + " Total Distance: " + rounded + " " + unit);
        chart.getData().add(points);
    }

    private double drawDistancePoints(Track track, XYChart.Series points, double distance, double time, boolean isMiles) {
        for (int i = 0; i < track.getNumPoints() - 1; i++) {
            if (isMiles) {
                distance += KM_TO_MILES * Track.distanceCalc(track.getPoint(i), track.getPoint(i + 1));
            } else {
                distance += Track.distanceCalc(track.getPoint(i), track.getPoint(i + 1));
            }

            time += calculateTime(track.getPoint(i), track.getPoint(i + 1));
            XYChart.Data point = new XYChart.Data(time, distance);
            Circle circle = new Circle(1.0);
            circle.setVisible(false);
            point.setNode(circle);
            points.getData().add(point);
        }
        return distance;
    }

    private double calculateTime(Point pointA, Point pointB) {
        long totalTime = Math.abs(pointB.getDate().getTime() - pointA.getDate().getTime());
        double seconds = totalTime / 1000.0;
        double minutes = seconds / 60;
        return minutes;
    }

    private double calculateElevationGain(Point pointA, Point pointB) {
        double gain = 0;
        if (pointB.getElevation() - pointA.getElevation() > 0) {
            gain += pointB.getElevation() - pointA.getElevation();
        }
        return gain;
    }


}
