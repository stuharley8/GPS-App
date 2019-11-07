/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Represents a Point in a GPS track
 */
public class Point {

    private double elevation;
    private double latitude;
    private double longitude;
    private Date date;

    /**
     * Constructor
     * @param latitude the latitude
     * @param longitude the longitude
     * @param elevation the elevation
     * @param date the date represented as a String in UTC format
     */
    public Point(double latitude, double longitude, double elevation, String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:s'Z'");
            this.date = sdf.parse(date);
        } catch (Exception e) {
            // Should not fail
        }
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

    public Date getDate() {
        return date;
    }

}