/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package tests;

import gps.GPS;
import gps.Point;
import gps.Track;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tester class for the GPS application
 */
public class JUnitTest {

    private static final double DELTA = .01; //Signifies 1 percent margin of error

    /**
     * Tests metrics for GPSTest1.gpx file. Values are hardcoded. (Does not use the parser).
     */
    @Test
    public void testGPSTest1Metrics() {
        Point point = new Point(43.30000000000000, -87.90000000000000,
                500.0000000000000, "2016-02-10T13:00:00Z");
        ArrayList<Point> points = new ArrayList<>(Collections.singletonList(point));
        Track track = new Track(points, "One Point Track");
        GPS gps = new GPS();
        gps.addTrack(track);
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.3, gps.getTrack(0).getMaxLatitude());
        assertEquals(-87.9, gps.getTrack(0).getMinLongitude());
        assertEquals(-87.9, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(500, gps.getTrack(0).getMaxElevation());
        // TODO Determine Expected Values for following assertEquals
        assertEquals(0, gps.getTrack(0).getAveSpeedKM());
        assertEquals(0, gps.getTrack(0).getAveSpeedMiles());
        assertEquals(0, gps.getTrack(0).getDistanceKM());
        assertEquals(0, gps.getTrack(0).getDistanceMiles());
        assertEquals(0, gps.getTrack(0).getMaxSpeedKM());
        assertEquals(0, gps.getTrack(0).getMaxSpeedMiles());
    }

    /**
     * Tests metrics for GPSTest2.gpx file. Values are hardcoded. (Does not use the parser).
     */
    @Test
    public void testGPSTest2Metrics() {
        Point point1 = new Point(43.30000000000000, -87.90000000000000,
                500.0000000000000, "2016-02-10T13:00:00Z");
        Point point2 = new Point(43.30000000000000, -88.00000000000000,
                2500.0000000000000, "2016-02-10T13:10:00Z");
        ArrayList<Point> points = new ArrayList<>(Arrays.asList(point1, point2));
        Track track = new Track(points, "Two Point Track");
        GPS gps = new GPS();
        gps.addTrack(track);
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.3, gps.getTrack(0).getMaxLatitude());
        assertEquals(-88, gps.getTrack(0).getMinLongitude());
        assertEquals(-87.9, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(2500, gps.getTrack(0).getMaxElevation());
        assertEquals(49.56, gps.getTrack(0).getAveSpeedKM(), 49.56*DELTA);
        assertEquals(30.798, gps.getTrack(0).getAveSpeedMiles(), 30.798*DELTA);
        assertEquals(8.260, gps.getTrack(0).getDistanceKM(), 8.260*DELTA);
        assertEquals(5.133, gps.getTrack(0).getDistanceMiles(), 5.133*DELTA);
        assertEquals(49.56, gps.getTrack(0).getMaxSpeedKM(), 49.56*DELTA);
        assertEquals(30.798, gps.getTrack(0).getMaxSpeedMiles(), 30.798*DELTA);
    }

    /**
     * Tests metrics for GPSTest5Center.gpx file. Values are hardcoded. (Does not use the parser).
     */
    @Test
    public void testGPSTest5CenterMetrics() {
        Point point = new Point(43.30000000000000, -87.90000000000000,
                500.0000000000000, "2016-02-10T13:00:00Z");
        Point point2 = new Point(43.30000000000000, -88.00000000000000,
                500.0000000000000, "2016-02-10T13:10:00Z");
        Point point3 = new Point(43.40000000000000, -88.00000000000000,
                500.0000000000000, "2010-10-19T13:20:00Z");
        Point point4 = new Point(43.40000000000000, -87.90000000000000,
                500.0000000000000, "2010-10-19T13:30:00Z");
        Point point5 = new Point(43.30000000000000, -87.90000000000000,
                500.0000000000000, "2010-10-19T13:40:00Z");
        ArrayList<Point> points = new ArrayList<>(Arrays.asList(point, point2, point3, point4, point5));
        Track track = new Track(points, "Two Point Track");
        GPS gps = new GPS();
        gps.addTrack(track);
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.4, gps.getTrack(0).getMaxLatitude());
        assertEquals(-88, gps.getTrack(0).getMinLongitude());
        assertEquals(-87.9, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(500, gps.getTrack(0).getMaxElevation());
        assertEquals(51.285, gps.getTrack(0).getAveSpeedKM(), 51.285*DELTA);
        assertEquals(31.867, gps.getTrack(0).getAveSpeedMiles(), 31.867*DELTA);
        assertEquals(38.464, gps.getTrack(0).getDistanceKM(), 38.464*DELTA);
        assertEquals(23.9, gps.getTrack(0).getDistanceMiles(), 23.9*DELTA);
        // TODO Determine Expected Values for following assertEquals
        assertEquals(0, gps.getTrack(0).getMaxSpeedKM(), 0*DELTA);
        assertEquals(0, gps.getTrack(0).getMaxSpeedMiles(), 0*DELTA);
    }
}
