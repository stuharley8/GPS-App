/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a Point in a GPS track
 */
public class Point {

    private double elevation;
    private double latitude;
    private double longitude;
    private Date date;

    private static final double MAX_LATITUDE = 90;
    private static final double MAX_LONGITUDE = 180;

    /**
     * Constructor. Sets base values of elevation and date which will be added later.
     * @param latitude the latitude
     * @param longitude the longitude
     * @throws IllegalArgumentException if the latitude or longitude is invalid
     */
    public Point(double latitude, double longitude) {
        if(Math.abs(latitude) > MAX_LATITUDE) {
            throw new IllegalArgumentException("Invalid latitude of " + latitude);
        }
        if(Math.abs(longitude) > MAX_LONGITUDE) {
            throw new IllegalArgumentException("Invalid longitude of " + longitude);
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = -1;
        this.date = null;

    }

    /**
     * Constructor used for testing
     * @param latitude the latitude
     * @param longitude the longitude
     * @param elevation the elevation
     * @param date the date as a String in UTC format
     */
    public Point(double latitude, double longitude, double elevation, String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        setDate(date);
    }

    /**
     * Sets the date of the point object
     * @param time the date represented as a String in UTC format
     * @throws IllegalArgumentException if there is a problem parsing the provided time
     */
    public void setDate(String time) throws IllegalArgumentException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:s'Z'");
            this.date = sdf.parse(time);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
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