/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package tests;

import gps.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tester class for the GPS application
 */
public class JUnitTest {

    private static final double DELTA = .02; //Signifies 1 percent margin of error

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
     * Tests metrics for GPSTest5Center.txt file. Values are hardcoded. (Does not use the parser).
     */
    @Test
    public void testGPSTest5CenterMetrics() {
        Point point = new Point(43.30000000000000, -87.90000000000000,
                500.0000000000000, "2010-10-19T13:00:00Z");
        Point point2 = new Point(43.30000000000000, -88.00000000000000,
                500.0000000000000, "2010-10-19T13:10:00Z");
        Point point3 = new Point(43.40000000000000, -88.00000000000000,
                500.0000000000000, "2010-10-19T13:20:00Z");
        Point point4 = new Point(43.40000000000000, -87.90000000000000,
                500.0000000000000, "2010-10-19T13:30:00Z");
        Point point5 = new Point(43.30000000000000, -87.90000000000000,
                500.0000000000000, "2010-10-19T13:40:00Z");
        ArrayList<Point> points = new ArrayList<>(Arrays.asList(point, point2, point3, point4, point5));
        Track track = new Track(points, "Five Point Track");
        GPS gps = new GPS();
        gps.addTrack(track);
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.4, gps.getTrack(0).getMaxLatitude());
        assertEquals(-88, gps.getTrack(0).getMinLongitude());
        assertEquals(-87.9, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(500, gps.getTrack(0).getMaxElevation());
        assertEquals(57.696, gps.getTrack(0).getAveSpeedKM(), 57.696*DELTA);
        assertEquals(35.85, gps.getTrack(0).getAveSpeedMiles(), 35.85*DELTA);
        assertEquals(38.464, gps.getTrack(0).getDistanceKM(), 38.464*DELTA);
        assertEquals(23.9, gps.getTrack(0).getDistanceMiles(), 23.9*DELTA);
        assertEquals(66.7, gps.getTrack(0).getMaxSpeedKM(), 66.7*DELTA);
        assertEquals(41.5, gps.getTrack(0).getMaxSpeedMiles(), 41.5*DELTA);
    }

    /**
     * Tests Metrics for GPSTest10.gpx file. Values are hardcoded. (Does not use the parser).
     */
    @Test
    public void testGPSTest10Metrics() {
        Point point1 = new Point(43.3, -88.0, 1000.0, "2016-02-10T13:10:00Z");
        Point point2 = new Point(43.3, -88.1, 1500.0, "2016-02-10T13:20:00Z");
        Point point3 = new Point(43.3, -88.2, 2000.0, "2016-02-10T13:30:00Z");
        Point point4 = new Point(43.3, -88.3, 2500.0, "2016-02-10T13:40:00Z");
        Point point5 = new Point(43.3, -88.4, 3000.0, "2016-02-10T13:50:00Z");
        Point point6 = new Point(43.3, -88.5, 2500.0, "2016-02-10T14:00:00Z");
        Point point7 = new Point(43.3, -88.6, 2000.0, "2016-02-10T14:10:00Z");
        Point point8 = new Point(43.3, -88.7, 1500.0, "2016-02-10T14:20:00Z");
        Point point9 = new Point(43.3, -88.8, 1000.0, "2016-02-10T14:30:00Z");
        Point point10 = new Point(43.3, -88.9, 500.0, "2016-02-10T14:40:00Z");
        ArrayList<Point> points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4,
                point5, point6, point7, point8, point9, point10));
        Track track = new Track(points, "Ten Point Track");
        GPS gps = new GPS();
        gps.addTrack(track);
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.3, gps.getTrack(0).getMaxLatitude());
        assertEquals(-88.9, gps.getTrack(0).getMinLongitude());
        assertEquals(-88, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(3000, gps.getTrack(0).getMaxElevation());
        assertEquals(49.26, gps.getTrack(0).getAveSpeedKM(), 49.26*DELTA);
        assertEquals(30.61, gps.getTrack(0).getAveSpeedMiles(), 30.61*DELTA);
        assertEquals(73.9, gps.getTrack(0).getDistanceKM(), 73.9*DELTA);
        assertEquals(45.919, gps.getTrack(0).getDistanceMiles(), 45.919*DELTA);
        assertEquals(48.67, gps.getTrack(0).getMaxSpeedKM(), 48.67*DELTA);
        assertEquals(30.24, gps.getTrack(0).getMaxSpeedMiles(), 30.24*DELTA);
    }

    /**
     * Tests metrics for GPSTest1.gpx file. Uses the Parser.
     */
    @Test
    public void testGPSTest1MetricsParser() {
        GPS gps = new GPS();
        GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
        AbstractParserEventHandler handler = gpsTrackBuilder;
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\GPSTest1.gpx");
            gps.addTrack(gpsTrackBuilder.loadedTrack());
        } catch (Exception e) {
            fail();
        }
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.3, gps.getTrack(0).getMaxLatitude());
        assertEquals(-87.9, gps.getTrack(0).getMinLongitude());
        assertEquals(-87.9, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(500, gps.getTrack(0).getMaxElevation());
        assertEquals(0, gps.getTrack(0).getAveSpeedKM());
        assertEquals(0, gps.getTrack(0).getAveSpeedMiles());
        assertEquals(0, gps.getTrack(0).getDistanceKM());
        assertEquals(0, gps.getTrack(0).getDistanceMiles());
        assertEquals(0, gps.getTrack(0).getMaxSpeedKM());
        assertEquals(0, gps.getTrack(0).getMaxSpeedMiles());
    }

    /**
     * Tests metrics for GPSTest2.gpx file. Uses the Parser.
     */
    @Test
    public void testGPSTest2MetricsParser() {
        GPS gps = new GPS();
        GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
        AbstractParserEventHandler handler = gpsTrackBuilder;
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\GPSTest2.gpx");
            gps.addTrack(gpsTrackBuilder.loadedTrack());
        } catch (Exception e) {
            fail();
        }
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
     * Tests metrics for GPSTest5Center.txt file. Uses the Parser.
     */
    @Test
    public void testGPSTest5CenterMetricsParser() {
        GPS gps = new GPS();
        GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
        AbstractParserEventHandler handler = gpsTrackBuilder;
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\GPSTest5Center.txt");
            gps.addTrack(gpsTrackBuilder.loadedTrack());
        } catch (Exception e) {
            fail();
        }
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.4, gps.getTrack(0).getMaxLatitude());
        assertEquals(-88, gps.getTrack(0).getMinLongitude());
        assertEquals(-87.9, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(500, gps.getTrack(0).getMaxElevation());
        assertEquals(57.696, gps.getTrack(0).getAveSpeedKM(), 57.696*DELTA);
        assertEquals(35.85, gps.getTrack(0).getAveSpeedMiles(), 35.85*DELTA);
        assertEquals(38.464, gps.getTrack(0).getDistanceKM(), 38.464*DELTA);
        assertEquals(23.9, gps.getTrack(0).getDistanceMiles(), 23.9*DELTA);
        assertEquals(66.7, gps.getTrack(0).getMaxSpeedKM(), 66.7*DELTA);
        assertEquals(41.5, gps.getTrack(0).getMaxSpeedMiles(), 41.5*DELTA);
    }

    /**
     * Tests Metrics for GPSTest10.gpx file. Uses the Parser.
     */
    @Test
    public void testGPSTest10MetricsParser() {
        GPS gps = new GPS();
        GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
        AbstractParserEventHandler handler = gpsTrackBuilder;
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\GPSTest10.gpx");
            gps.addTrack(gpsTrackBuilder.loadedTrack());
        } catch (Exception e) {
            fail();
        }
        assertEquals(43.3, gps.getTrack(0).getMinLatitude());
        assertEquals(43.3, gps.getTrack(0).getMaxLatitude());
        assertEquals(-88.9, gps.getTrack(0).getMinLongitude());
        assertEquals(-88, gps.getTrack(0).getMaxLongitude());
        assertEquals(500, gps.getTrack(0).getMinElevation());
        assertEquals(3000, gps.getTrack(0).getMaxElevation());
        assertEquals(49.26, gps.getTrack(0).getAveSpeedKM(), 49.26*DELTA);
        assertEquals(30.61, gps.getTrack(0).getAveSpeedMiles(), 30.61*DELTA);
        assertEquals(73.9, gps.getTrack(0).getDistanceKM(), 73.9*DELTA);
        assertEquals(45.919, gps.getTrack(0).getDistanceMiles(), 45.919*DELTA);
        assertEquals(48.67, gps.getTrack(0).getMaxSpeedKM(), 48.67*DELTA);
        assertEquals(30.24, gps.getTrack(0).getMaxSpeedMiles(), 30.24*DELTA);
    }
}
