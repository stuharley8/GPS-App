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
     */
    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = -1;
        this.date = null;

    }

    public void setDate(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:s'Z'");
            this.date = sdf.parse(time);
        } catch (Exception e) {
            // Should not fail
        }
    }

    public void setElevation(double elevation){
        this.elevation = elevation;
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