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
import org.xml.sax.SAXException;
import table.TableSpeedsHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tester class for the GPS application
 */
public class JUnitTest {

    private static final double DELTA = .02; //Signifies a 2 percent margin of error

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
        assertEquals(49.56, gps.getTrack(0).getAveSpeedKM(), 49.56 * DELTA);
        assertEquals(30.798, gps.getTrack(0).getAveSpeedMiles(), 30.798 * DELTA);
        assertEquals(8.260, gps.getTrack(0).getDistanceKM(), 8.260 * DELTA);
        assertEquals(5.133, gps.getTrack(0).getDistanceMiles(), 5.133 * DELTA);
        assertEquals(49.56, gps.getTrack(0).getMaxSpeedKM(), 49.56 * DELTA);
        assertEquals(30.798, gps.getTrack(0).getMaxSpeedMiles(), 30.798 * DELTA);
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
        assertEquals(57.696, gps.getTrack(0).getAveSpeedKM(), 57.696 * DELTA);
        assertEquals(35.85, gps.getTrack(0).getAveSpeedMiles(), 35.85 * DELTA);
        assertEquals(38.464, gps.getTrack(0).getDistanceKM(), 38.464 * DELTA);
        assertEquals(23.9, gps.getTrack(0).getDistanceMiles(), 23.9 * DELTA);
        assertEquals(66.7, gps.getTrack(0).getMaxSpeedKM(), 66.7 * DELTA);
        assertEquals(41.5, gps.getTrack(0).getMaxSpeedMiles(), 41.5 * DELTA);
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
        assertEquals(49.26, gps.getTrack(0).getAveSpeedKM(), 49.26 * DELTA);
        assertEquals(30.61, gps.getTrack(0).getAveSpeedMiles(), 30.61 * DELTA);
        assertEquals(73.9, gps.getTrack(0).getDistanceKM(), 73.9 * DELTA);
        assertEquals(45.919, gps.getTrack(0).getDistanceMiles(), 45.919 * DELTA);
        assertEquals(48.67, gps.getTrack(0).getMaxSpeedKM(), 48.67 * DELTA);
        assertEquals(30.24, gps.getTrack(0).getMaxSpeedMiles(), 30.24 * DELTA);
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
        assertEquals(49.56, gps.getTrack(0).getAveSpeedKM(), 49.56 * DELTA);
        assertEquals(30.798, gps.getTrack(0).getAveSpeedMiles(), 30.798 * DELTA);
        assertEquals(8.260, gps.getTrack(0).getDistanceKM(), 8.260 * DELTA);
        assertEquals(5.133, gps.getTrack(0).getDistanceMiles(), 5.133 * DELTA);
        assertEquals(49.56, gps.getTrack(0).getMaxSpeedKM(), 49.56 * DELTA);
        assertEquals(30.798, gps.getTrack(0).getMaxSpeedMiles(), 30.798 * DELTA);
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
        assertEquals(57.696, gps.getTrack(0).getAveSpeedKM(), 57.696 * DELTA);
        assertEquals(35.85, gps.getTrack(0).getAveSpeedMiles(), 35.85 * DELTA);
        assertEquals(38.464, gps.getTrack(0).getDistanceKM(), 38.464 * DELTA);
        assertEquals(23.9, gps.getTrack(0).getDistanceMiles(), 23.9 * DELTA);
        assertEquals(66.7, gps.getTrack(0).getMaxSpeedKM(), 66.7 * DELTA);
        assertEquals(41.5, gps.getTrack(0).getMaxSpeedMiles(), 41.5 * DELTA);
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
        assertEquals(49.26, gps.getTrack(0).getAveSpeedKM(), 49.26 * DELTA);
        assertEquals(30.61, gps.getTrack(0).getAveSpeedMiles(), 30.61 * DELTA);
        assertEquals(73.9, gps.getTrack(0).getDistanceKM(), 73.9 * DELTA);
        assertEquals(45.919, gps.getTrack(0).getDistanceMiles(), 45.919 * DELTA);
        assertEquals(48.67, gps.getTrack(0).getMaxSpeedKM(), 48.67 * DELTA);
        assertEquals(30.24, gps.getTrack(0).getMaxSpeedMiles(), 30.24 * DELTA);
    }

    /**
     * Tests the parser if bad latitudes are entered
     */
    @Test
    public void testBadLatitudes() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\gpstest-bad latitudes.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("Invalid latitude of -90.1", e.getMessage());
            assertEquals("line 11", "line " + handler.getLine());
            assertEquals("col 63", "col " + handler.getColumn());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the parser if bad latitudes are entered
     */
    @Test
    public void testBadLongitudes() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\gpstest-bad longitudes.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("Invalid longitude of -180.1", e.getMessage());
            assertEquals("line 11", "line " + handler.getLine());
            assertEquals("col 61", "col " + handler.getColumn());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the parser if bad times are entered
     */
    @Test
    public void testBadTimes() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\gpstest-bad times.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("Unparseable date: \"2014-10-19X13:17:30Z\"", e.getMessage());
            assertEquals("line 24", "line " + handler.getLine());
            assertEquals("col 37", "col " + handler.getColumn());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the parser if an elevation is missing
     */
    @Test
    public void testMissingElevation() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\gpstest-missing elevation.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("</trkpt> element is missing a <ele> subelement or attribute! line 17, col 15", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the parser if a latitude is missing
     */
    @Test
    public void testMissingLatitude() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\gpstest-missing latitude.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("<trkpt> element has an illegal number of attributes: 1", e.getMessage());
            assertEquals("line 19", "line " + handler.getLine());
            assertEquals("col 40", "col " + handler.getColumn());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the parser if a longitude is missing
     */
    @Test
    public void testMissingLongitude() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\gpstest-missing longitude.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("<trkpt> element has an illegal number of attributes: 1", e.getMessage());
            assertEquals("line 15", "line " + handler.getLine());
            assertEquals("col 38", "col " + handler.getColumn());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the parser if a time is missing
     */
    @Test
    public void testMissingTime() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\gpstest-missing time.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("</trkpt> element is missing a <time> subelement or attribute! line 13, col 14", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the parser if the gpx xml tag is invalid
     */
    @Test
    public void testInvalidGPXTAG() {
        GPS gps = new GPS();
        AbstractParserEventHandler handler = new GPSTrackBuilder();
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\InvalidGPX.txt");
            fail();
        } catch (SAXException e) {
            assertEquals("Expected <gpx> element at this location! line 2, col 6", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests the table to see if it is displaying the correct info
     */
    @Test
    public void testTimesAtVariousSpeedsTable() {
        TableSpeedsHandler tsh = null;
        GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
        AbstractParserEventHandler handler = gpsTrackBuilder;
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\MHA5-15-All60.txt");
            tsh = new TableSpeedsHandler(gpsTrackBuilder.loadedTrack());
        } catch (Exception e) {
            fail();
        }
        assertEquals(tsh.getThreeLessMin(), 60);
        assertEquals(tsh.getSevenLessMin(), 60);
        assertEquals(tsh.getTenLessMin(), 60);
        assertEquals(tsh.getFifteenLessMin(), 60);
        assertEquals(tsh.getTwentyLessMin(), 60);
        assertEquals(tsh.getTwentyGreaterMin(), 60);
        assertEquals(tsh.getPercentThreeLess(), 16.67, DELTA);
        assertEquals(tsh.getPercentSevenLess(), 16.67, DELTA);
        assertEquals(tsh.getPercentTenLess(), 16.67, DELTA);
        assertEquals(tsh.getPercentFifteenLess(), 16.67, DELTA);
        assertEquals(tsh.getPercentTwentyLess(), 16.67 ,DELTA);
        assertEquals(tsh.getPercentTwentyGreater(), 16.67, DELTA);
    }

    /**
     * Tests the table to see if it is displaying the correct info. The times double every row
     */
    @Test
    public void testTimesAtVariousSpeedsTableDoubles() {
        TableSpeedsHandler tsh = null;
        GPSTrackBuilder gpsTrackBuilder = new GPSTrackBuilder();
        AbstractParserEventHandler handler = gpsTrackBuilder;
        handler.enableLogging(true);
        Parser parser;
        try {
            parser = new Parser(handler);
            parser.parse("testfiles\\MHA5-15-Doubles.gpx");
            tsh = new TableSpeedsHandler(gpsTrackBuilder.loadedTrack());
        } catch (Exception e) {
            fail();
        }
        assertEquals(tsh.getThreeLessMin(), 0);
        assertEquals(tsh.getSevenLessMin(), 10);
        assertEquals(tsh.getTenLessMin(), 20);
        assertEquals(tsh.getFifteenLessMin(), 40);
        assertEquals(tsh.getTwentyLessMin(), 80);
        assertEquals(tsh.getTwentyGreaterMin(), 160);
        assertEquals(tsh.getPercentThreeLess(), 0, DELTA);
        assertEquals(tsh.getPercentSevenLess(), 3.23, DELTA);
        assertEquals(tsh.getPercentTenLess(), 6.45, DELTA);
        assertEquals(tsh.getPercentFifteenLess(), 12.9, DELTA);
        assertEquals(tsh.getPercentTwentyLess(), 25.81 ,DELTA);
        assertEquals(tsh.getPercentTwentyGreater(), 51.61, DELTA);
    }
}
