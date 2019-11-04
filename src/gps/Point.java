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
 * @author harleys
 * @version 1.0
 * @created 04-Nov-2019 1:39:07 AM
 */
public class Point {

    private double elevation;
    private double latitude;
    private double longitude;
    private Instant time;

    public Point() {

    }

    /**
     * @param latitude
     * @param longitude
     * @param elevation
     * @param time
     */
    public Point(double latitude, double longitude, double elevation, String time) {

    }

    public double getElevation() {
        return 0;
    }

    public double getLatitude() {
        return 0;
    }

    public double getLongitude() {
        return 0;
    }

    public String getTime() {
        return "";
    }

}