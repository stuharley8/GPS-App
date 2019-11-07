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

    private final double METERS_TO_FEET = 3.28084;
    private final int EARTH_RADIUS_METERS = 6371000;

    private String name;
    private List<Point> points;

    /**
     * Constructor. Calculates metrics for the track when initialized.
     * @param points a not null list of points that represents the path of the track.
     * @param name the name of the track
     * @throws NullPointerException if points is null
     * @throws IllegalArgumentException if points contains 0 points
     */
    public Track(List<Point> points, String name) {
        if(points == null) {
            throw new NullPointerException("List of point is null");
        } else if(points.size() == 0) {
            throw new IllegalArgumentException("Track must contain at least one point");
        }
        this.points = points;
        this.name = name;
        calcMinsAndMaxes();
        calcDistanceKM();
        calcDistanceMiles();
    }

    private void calcMinsAndMaxes() {
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
        double deltaX;
        double deltaY;
        double deltaZ;
        double distance;
        double totalDistance = 0.0;
        double elevationA;
        double elevationB;
        double longA;
        double longB;
        double latA;
        double latB;

        for (int i = 0; i < points.size()-1; i++) {
            Point pointA = points.get(i);
            Point pointB = points.get(i+1);
            elevationA = pointA.getElevation();
            elevationB = pointB.getElevation();
            longA = pointA.getLongitude();
            longB = pointB.getLongitude();
            latA = pointA.getLatitude();
            latB = pointB.getLatitude();
            deltaX = (EARTH_RADIUS_METERS + (elevationB + elevationA) / 2)*(Math.toRadians(Math.abs(longB))-Math.toRadians(Math.abs(longA)))*Math.cos((Math.toRadians(latB)+Math.toRadians(latA))/2);
            deltaY = (EARTH_RADIUS_METERS + (elevationB + elevationA)/2)*(Math.toRadians(latB)-Math.toRadians(latA));
            deltaZ = elevationB-elevationA;
            double radians = 0.0174533;
//            deltaX = (EARTH_RADIUS_METERS + (elevationB + elevationA) / 2)*(Math.abs(longB*radians)-Math.abs(longA*radians))*Math.cos(((latB*radians)+(latA*radians))/2);
//            deltaY = (EARTH_RADIUS_METERS + (elevationB + elevationA)/2)*((latB*radians)-(latA*radians));
//            deltaZ = elevationB-elevationA;
            distance = deltaX + deltaY + deltaZ;
            totalDistance += distance;
        }

        distanceKM = totalDistance/1000.0;
    }

    private void calcDistanceMiles() {
        distanceMiles = distanceKM*0.621371;
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

    public double getMaxElevationFt(){
        return maxElevation*METERS_TO_FEET;
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

    public double getMinElevationFt(){
        return minElevation*METERS_TO_FEET;
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