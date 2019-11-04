/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import java.util.List;

/**
 * Represents a Collection of Points that form a GPS track
 */
public class Track {

    private double aveSpeedKM;
    private double aveSpeedMiles;
    private double distanceKM;
    private double distanceMiles;
    private double maxElevation;
    private double maxLatitude;
    private double maxLongitude;
    private double maxSpeedKM;
    private double maxSpeedMiles;
    private double minElevation;
    private double minLatitude;
    private double minLongitude;

    private String name;
    private List<Point> points;

    /**
     * Constructor
     * @param points a not null list of points that represents the path of the track.
     * @param name the name of the track
     */
    public Track(List<Point> points, String name) {
        this.points = points;
        this.name = name;
    }

    private void calcMinAndMaxes() {
        double minLat = points.get(0).getLatitude();
        double minLong = points.get(0).getLongitude();
        double minEle = points.get(0).getElevation();
        double maxLat = points.get(0).getLatitude();
        double maxLong = points.get(0).getLongitude();
        double maxEle = points.get(0).getElevation();
        for(int i = 1; i < points.size(); i++) {
            if(points.get(i).getElevation() > maxEle) {
                maxEle = points.get(i).getElevation();
            } else if(points.get(i).getElevation() < minEle) {
                minEle = points.get(i).getElevation();
            }
            if(points.get(i).getLatitude() > maxLat) {
                maxLat = points.get(i).getLatitude();
            } else if(points.get(i).getLatitude() < minLat) {
                minLat = points.get(i).getLatitude();
            }
            if(points.get(i).getLongitude() > maxLong) {
                maxLong = points.get(i).getLongitude();
            } else if(points.get(i).getLongitude() < minLong) {
                minLong = points.get(i).getLongitude();
            }
        }
        minElevation = minEle;
        minLatitude = minLat;
        minLongitude = minLong;
        maxElevation = maxEle;
        maxLatitude = maxLat;
        maxLongitude = maxLong;
    }

    private void calcAveSpeedKM() {

    }

    private void calcAveSpeedMiles() {

    }

    private void calcDistanceKM() {

    }

    private void calcDistanceMiles() {

    }

    private void calcMaxSpeedKM() {

    }

    private void calcMaxSpeedMiles() {

    }

    public double getAveSpeedKM() {
        return aveSpeedKM;
    }

    public double getAveSpeedMiles() {
        return aveSpeedMiles;
    }

    public double getDistanceKM() {
        return distanceKM;
    }

    public double getDistanceMiles() {
        return distanceMiles;
    }

    public double getMaxElevation() {
        return maxElevation;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public double getMaxSpeedKM() {
        return maxSpeedKM;
    }

    public double getMaxSpeedMiles() {
        return maxSpeedMiles;
    }

    public double getMinElevation() {
        return minElevation;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public String getName() {
        return name;
    }

    public int getNumPoints() {
        return points.size();
    }
}