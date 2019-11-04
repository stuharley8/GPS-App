/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import java.time.Instant;

/**
 * Represents a Point in a GPS track
 */
public class Point {

    private double elevation;
    private double latitude;
    private double longitude;
    private Instant time;

    /**
     * Constructor
     * @param latitude the latitude
     * @param longitude the longitude
     * @param elevation the elevation
     * @param time the time
     */
    public Point(double latitude, double longitude, double elevation, Instant time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.time = time;
    }

    public double getElevation() {
        return elevation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Instant getTime() {
        return time;
    }

}