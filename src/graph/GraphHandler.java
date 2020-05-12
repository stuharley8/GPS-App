package graph;

import gps.Point;
import gps.Track;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.List;

/**
 * Class that handles drawing the different types of graphs
 */
public class GraphHandler {

    private static final int EARTH_RADIUS_METERS = 6371000;
    private static final double KM_TO_MILES = 0.621371;

    private LineChart<Double, Double> chart;
    private boolean isMiles;
    List<Track> selectedTracks;


    /**
     * Constructs the graph handler
     * @param chart LineChart being drawn
     * @param selectedTracks list of tracks that are selected
     * @param isMiles if user switches metrics
     */
    public GraphHandler(LineChart<Double, Double> chart, List<Track> selectedTracks, boolean isMiles){
        this.selectedTracks = selectedTracks;
        this.chart = chart;
        this.isMiles = isMiles;
    }

    /**
     * Draws distance vs time graph
     */
    public void drawAllDistanceGraphs(){
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawDistanceGraph(track.getName());
        }
    }

    /**
     * Draws elevation vs time graph
     */
    public void drawAllElevationGraphs(){
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawElevationGraph(track.getName());
        }
    }

    /**
     * Draws elevation gain vs time graph
     */
    public void drawAllElevationGainGraphs(){
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawElevationGainGraph(track.getName());
        }
    }

    /**
     * Draws speed vs time graph
     */
    public void drawAllSpeedGraphs() {
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawSpeedGraph(track.getName());
        }
    }

    /**
     * Sets the tracks for the handler to choose from
     * @param selectedTracks list of tracks that are selected on the gui
     */
    public void setSelectedTracks(List<Track> selectedTracks){
        this.selectedTracks = selectedTracks;
    }

    private void drawSpeedGraph(String name){
        Track track = null;
        for (Track t:selectedTracks
        ) {
            if (t.getName().equals(name)){
                track = t;
            }
        }
        XYChart.Series points = new XYChart.Series();
        List<Point> pointList = track.getPoints();
        double speed = 0;
        double time = 0;
        double distance = 0;
        for(int i = 0; i < pointList.size()-1; i++){
            time += calculateTime(pointList.get(i), pointList.get(i+1));
            speed = calculateSpeed(pointList.get(i), pointList.get(i+1));
            distance += calculateDistance(pointList.get(i), pointList.get(i+1));
            XYChart.Data point = new XYChart.Data(time, speed);
            Circle circle = new Circle(1.0);
            circle.setVisible(false);
            point.setNode(circle);
            points.getData().add(point);
        }
        String rounded = String.format("%.3f", distance);
        points.setName(track.getName() + " Total Distance: " + rounded + " km");
        chart.getData().add(points);
    }

    private void drawElevationGraph(String name){
        Track track = null;
        for (Track t:selectedTracks
        ) {
            if (t.getName().equals(name)){
                track = t;
            }
        }
        XYChart.Series points = new XYChart.Series();
        List<Point> pointList = track.getPoints();
        double gain = 0;
        double time = 0;
        double initialTime = pointList.get(0).getDate().getTime();
        double lastElevation = pointList.get(0).getElevation();
        XYChart.Data initialPoint  = new XYChart.Data(time, lastElevation);
        Circle circle = new Circle(1.0);
        circle.setVisible(false);
        initialPoint.setNode(circle);
        points.getData().add(initialPoint);
        for(int i = 1; i < pointList.size(); i++){
            time += ((pointList.get(i).getDate().getTime()- initialTime)/1000) / 60;
            initialTime = pointList.get(i).getDate().getTime();
            XYChart.Data point  = new XYChart.Data(time, pointList.get(i).getElevation());
            Circle circle2 = new Circle(1.0);
            circle.setVisible(false);
            point.setNode(circle2);
            points.getData().add(point);
            if(pointList.get(i).getElevation() >= lastElevation){
                gain =+ pointList.get(i).getElevation() - lastElevation ;
            }
            lastElevation = pointList.get(i).getElevation();
        }
        String rounded = String.format("%.3f", gain);
        points.setName(track.getName() + " Elevation Gain: " + rounded + " m");
        chart.getData().add(points);
                //.add(points);
    }

    private void drawElevationGainGraph(String name){
        Track track = null;
        for (Track t:selectedTracks
        ) {
            if (t.getName().equals(name)){
                track = t;
            }
        }
        XYChart.Series points = new XYChart.Series();
        List<Point> pointList = track.getPoints();
        double gain = 0;
        double time = 0;
        for(int i = 0; i < pointList.size()-1; i++){
            time += calculateTime(pointList.get(i), pointList.get(i+1));
            gain += calculateElevationGain(pointList.get(i), pointList.get(i+1));
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

    private void drawDistanceGraph(String name){
        Track track = null;
        for (Track t:selectedTracks
        ) {
            if (t.getName().equals(name)){
                track = t;
            }
        }
        double distance = 0;
        double time = 0;
        String unit = "km";
        XYChart.Series points = new XYChart.Series();
        if(isMiles){
            unit = "mi";
        }

        List<Point> pointList = track.getPoints();
        distance = drawDistancePoints(pointList, points, distance, time, isMiles);
        String rounded = String.format("%.3f", distance);
        points.setName(track.getName() + " Total Distance: " + rounded + " " + unit);
        chart.getData().add(points);
    }

    private double drawDistancePoints(List<Point> pointList,  XYChart.Series points, double distance, double time, boolean isMiles){
        for (int i = 0; i < pointList.size()-1; i++){
            if(isMiles){
                distance += KM_TO_MILES*calculateDistance(pointList.get(i), pointList.get(i+1));
            }else{
                distance += calculateDistance(pointList.get(i), pointList.get(i+1));
            }

            time += calculateTime(pointList.get(i), pointList.get(i+1));
            XYChart.Data point = new XYChart.Data(time, distance);
            Circle circle = new Circle(1.0);
            circle.setVisible(false);
            point.setNode(circle);
            points.getData().add(point);
        }
        return distance;
    }


    private double calculateDistance(Point pointA, Point pointB){
        double deltaX = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(Math.abs(pointB.getLongitude())) - Math.toRadians(Math.abs(pointA.getLongitude())))
                * Math.cos((Math.toRadians(Math.abs(pointB.getLatitude())) + Math.toRadians(Math.abs(pointA.getLatitude()))) / 2);
        double deltaY = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(pointB.getLatitude()) - Math.toRadians(pointA.getLatitude()));
        double deltaZ = pointB.getElevation() - pointA.getElevation();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) / 1000;
    }

    private double calculateTime(Point pointA, Point pointB){
        long totalTime = Math.abs(pointB.getDate().getTime() - pointA.getDate().getTime());
        double seconds = totalTime / 1000.0;
        double minutes = seconds / 60;
        return minutes;
    }

    private double calculateElevationGain(Point pointA, Point pointB){
        double gain = 0;
        if(pointB.getElevation()-pointA.getElevation() > 0){
            gain += pointB.getElevation()-pointA.getElevation();
        }
        return gain;
    }

    private double calculateSpeed(Point pointA, Point pointB){
        double tempKM = calculateDistance(pointA, pointB);
        double speedKM = (tempKM /calculateTime(pointA, pointB))/60;
        return speedKM;
    }



}
