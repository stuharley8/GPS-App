package gps;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;


/*
 *
 *  You must complete the methods below so that you can capture the xml
 *  data of interest to your application. Follow the instructions in the comments,
 *  and look at the RosterBuilder.java code for examples.
 *
 *  You must pass the reference to this class to the Parser constructor.
 */
public class GPSTrackBuilder extends AbstractParserEventHandler {
    // possible state values
    private enum PossibleStates { INITIAL, GPX, TRK, NAME, TRKSEG, TRKPT, TIME, ELE, FINAL }

    private PossibleStates currentState = PossibleStates.INITIAL; // starting state of parsing
    private List<Point> track; // You need to declare some kind of coordinate collection class here.
    private String name;
    private Point currentPoint;

    @Override
    protected void log(String prefix, String data) {
        return; // So it doesn't print to the console
    }

    @Override
    // This method is called whenever the Parser encounters a new element tag
    // which may or may not have Attributes. For example,
    // <trkpt lat="43.30627219" lon="-87.98892299">
    // The <trkpt> element contains two attributes: lat and lon.
    // Conversely, <ele>249.5</ele> contains no attributes.
    // The code below shows how to access the values of the attributes when they exist.
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        int line = locator.getLineNumber(); // current line being parsed
        int column = locator.getColumnNumber(); // current column being parsed

        log("startElement found", "<" + localName + "> at line " + line + ", col " + column); // debug logger

        // localName contains the name of the element - e.g. gpx, name, trkpt, etc

        // You need to ensure that elements are found in the correct order by maintaining state.
        // For instance:
        if (currentState == PossibleStates.INITIAL) {
            if (!localName.equalsIgnoreCase("gpx")) { // <gpx> should be the first element found
                throw new SAXException("Expected <gpx> element at this location! line " + line + ", col " + column);
                // This ensures that no other element besides <gpx> is at the start of the xml file
            }
            currentState = PossibleStates.GPX; // once <gpx> is found, we're in the GPX state!
            // You need to initialize your coordinate collection class here.
        }

        //Add other logic to check for <trk>, <name>, <trkseg> and <trkpt> and advance to the those states, at
        // each step making sure that the previous state was correct.
        //For instance, a <name> element can only be found within a <trk> element (in the TRK state),

        // this is the trk
        if (currentState == PossibleStates.GPX) {
            if (localName.equalsIgnoreCase("trk")){
                throw new SAXException("Expected <trk> element at this location! line " + line + ", col " + column);
            }
            currentState = PossibleStates.TRK;

            track = new ArrayList<>();
        }

        // this is the name getting the name attribute
        if (localName.equalsIgnoreCase("name")){
            if (currentState != PossibleStates.TRK) {
                throw new SAXException("<name> element found in illegal location");
            }
            currentState = PossibleStates.NAME;
        }

        // this is the trkseg getting nothing
        if (localName.equalsIgnoreCase("trkseg")){
            if (currentState != PossibleStates.TRK) {
                throw new SAXException("Expected <trkseg> element at this location! line " + line + ", col " + column);
            }
            currentState = PossibleStates.TRKSEG;
        }

        // this is the trkpt getting lat and long
        if (localName.equalsIgnoreCase("trkpt")) {
            if (currentState != PossibleStates.TRKSEG) {
                throw new SAXException("Expected <trkpt> element at this location! line " + line + ", col " + column);
            }
            currentState = PossibleStates.TRKPT;

            if (atts.getLength() != 2){
                throw new SAXException("<trkpt> element has an illegal number of attributes: " + atts.getLength());
            }

            String firstAtt = atts.getLocalName(0); // should be lat or lon
            String firstAttValue = atts.getValue(0);
            String secondAtt = atts.getLocalName(1); // should be lat or lon
            String secondAttValue = atts.getValue(1);

            double latitude;
            double longitude;
            if (firstAtt.equalsIgnoreCase("lat") && secondAtt.equalsIgnoreCase("lon")) {
                try {
                    latitude = Double.parseDouble(firstAttValue);
                    longitude = Double.parseDouble(secondAttValue);
                } catch (NumberFormatException npe){
                    throw new SAXException("<trkpt> attributes lat or lon are not doubles!");
                }

            } else if (firstAtt.equalsIgnoreCase("lon") && secondAtt.equalsIgnoreCase("lat")) {
                try { // if attributes are flipped
                    latitude = Double.parseDouble(secondAttValue);
                    longitude = Double.parseDouble(firstAttValue);
                } catch (NumberFormatException npe){
                    throw new SAXException("<trkpt> attributes lat or lon are not doubles!");
                }

            } else {
                throw new SAXException("<trkpt> element has one or more illegal attributes: " + firstAtt + ":" + secondAtt);
            }
            try {
                currentPoint = new Point(latitude, longitude);
            } catch (IllegalArgumentException e) {
                throw new SAXException(e.getMessage());
            }
        }

        if (localName.equalsIgnoreCase("ele")) {
            if (currentState != PossibleStates.TRKPT) {
                throw new SAXException("Expected <ele> element at this location! line " + line + ", col " + column);
            }
            currentState = PossibleStates.ELE;
        }

        if (localName.equalsIgnoreCase("time")){
            if (currentState != PossibleStates.TRKPT){
                throw new SAXException("Expected <ele> element at this location! line " + line + ", col " + column);
            }
            currentState = PossibleStates.TIME;
        }

        // Once you have advanced to the TRKPT state,
        // you must create/initialize a new coordinate object whenever a <trkpt> is found.

        // You must check to make sure that the <trkpt> element has both lat and lon
        // attributes, so that you can set those values in the coordinate object.
        // If either of those attributes don't exist, throw a SAXException.

        // After entering the TRKPT state, you must continue to check for other elements and make sure the state is correct as each is found;
        // for instance, an <ele> element can only be found within a <trkpt> element (in the TRKPT state),
        // a <time> element can only be found within a <trkpt> element (in the TRKPT state)
        // If not, throw a SAXException!
    }

    @Override
    // This method is called whenever the Parser encounters an end tag (e.g. </trkpt>, </gpx>)
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        int line = locator.getLineNumber(); // current line being parsed
        int column = locator.getColumnNumber(); // current column being parsed

        log("endElement found", "<" + localName + "> at line " + line + ", col " + column); // debug logger

        // localName contains the name of the element - e.g. gpx, name, trkpt, etc
        if (localName.equalsIgnoreCase("time")){
            if (currentState != PossibleStates.TIME){
                throw new SAXException("</time> element found in illegal location! " + line + ", col " + column);
            }
            currentState = PossibleStates.TRKPT;
        }

        if (localName.equalsIgnoreCase("ele")){
            if (currentState != PossibleStates.ELE){
                throw new SAXException("</ele> element found in illegal location! " + line + ", col " + column);
            }
            currentState = PossibleStates.TRKPT;
        }

        //this adds the point to the track list as long as it is set correctly and valid
        if (localName.equalsIgnoreCase("trkpt")){
            if (currentState != PossibleStates.TRKPT){
                throw new SAXException("</time> element found in illegal location! " + line + ", col " + column);
            }
            if (currentPoint.getDate() == null){
                throw new SAXException("</trkpt> element is missing a <time> subelement or attribute! line "
                        + line +", col " + column);
            }
            if (currentPoint.getElevation() == -1){
                throw new SAXException("</trkpt> element is missing a <ele> subelement or attribute! line "
                 + line + ", col " + column);
            }
            track.add(currentPoint);
            currentState = PossibleStates.TRKSEG;
        }
        // You must finalize the coordinate object whenever a </trkpt> is found, making
        // sure that you have fully initialized its lat, lon, time, and elevation attributes.
        // If it's not fully initialized, something was missing, so throw a SAXException in that case.

        if (localName.equalsIgnoreCase("trkseg")){
            if (currentState != PossibleStates.TRKSEG){
                throw new SAXException("</trkseg> element found in illegal location!");
            }
            currentState = PossibleStates.TRK;
        }

        if (localName.equalsIgnoreCase("name")){
            if (currentState != PossibleStates.NAME){
                throw new SAXException("</name> element found in illegal location");
            }
            currentState = PossibleStates.TRK;
        }

        if (localName.equalsIgnoreCase("trk")){
            if (currentState != PossibleStates.TRK){
                throw new SAXException("</trk> element found in illegal location");
            }
            currentState = PossibleStates.GPX;
        }

        if (localName.equalsIgnoreCase("gpx")) {
            if (currentState != PossibleStates.GPX) { // should be back in GPX state when </gpx> is encountered
                throw new SAXException("</gpx> element found in illegal location! line " + line + ", col " + column);
            }
            currentState = PossibleStates.FINAL; // </gpx> signals that we've reached the end state!
            // When the FINAL state is reached, your collection of coordinates should be complete.
            // You will have to provide a public getter method to allow that collection to be retrieved.
        }
        // You must check for other end elements and make sure the state is correct as each is found;
        // for instance, an </trkpt> element can only be found if the state is TRKPT,
        // a </ele> element can only be found if the state is ELE, etc.
        // If not, throw a SAXException!
    }

    @Override
    // This method is called whenever the Parser encounters text between tags.
    // For example, for the tag <ele>249.5</ele>, the characters "249.5" are reported.
    // For the tag <time>2010-10-19T13:00:00Z</time>, the characters "2010-10-19T13:00:00Z" are reported.
    // For tags like <gpx> and <trkpt>, a newline character is reported.
    public void characters(char[] ch, int start, int length) throws SAXException {
        int line = locator.getLineNumber(); // current line being parsed
        int column = locator.getColumnNumber(); // current column being parsed

        // convert the char[] array to a String for convenience
        String s = new String(ch);
        s = s.substring(start, start + length);
        s = s.trim(); // remove leading and trailing whitespace

        if (!s.isEmpty()) { // ignore whitespace
            log("characters found", "'" + s + "' at line " + line + ", col " + column);
        }

        if (currentState == PossibleStates.NAME){
            name = s;
        }

        if (currentState == PossibleStates.TIME){
            if (s.isEmpty()) {
                throw new SAXException("</time> attribute is empty!");
            }
            try {
                currentPoint.setDate(s);
            } catch (IllegalArgumentException e) {
                throw new SAXException(e.getMessage());
            }
        }

        // this make sure it is parsing a double from the file
        if (currentState == PossibleStates.ELE){
            try {
                double elevation = Double.parseDouble(s);
                currentPoint.setElevation(elevation);
            } catch (NumberFormatException npe){
                throw new SAXException("</ele> attribute is not a double!");
            }
        }
        // Check the state to see what XML element these characters belong to.

        //Note that elements like <ele> and <time> contain characters that must be converted into
        //  elevation and date/time values. If conversion fails, throw a SAXException!
    }

    @Override
    //This method is called when the Parser reaches the end of the
    // XML document. If the document was a well-formed GPS file, then the currentState of the
    // system should be possibleStates.FINAL (since the end </gpx> element was
    // found). If the currentState is not FINAL here, then something was wrong
    // with the document structure, so throw a SAXException!
    public void endDocument() throws SAXException {

        log("end of document found", "parsing complete"); // debug logger

        if (currentState != PossibleStates.FINAL) {
            throw new SAXException("Document structure error. Not in FINAL state at the end of the document!");
        }
    }

    /**
     * Constructs the track loaded with the points created by the Track Builder parsing the xml file
     * @return the loaded track
     */
    public Track loadedTrack() {
        return new Track(track, name);
    }
}