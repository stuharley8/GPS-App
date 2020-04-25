/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Matt Haas
 * Created: 4/25/2020
 */

package plotter;

import javafx.scene.paint.Color;

/**
 * Object information that is drawn to the map table
 */
public class PlotterTableObject {
    private String name;
    private Color color;
    private double distanceInMiles;
    private double distanceInKM;
    private boolean isVisible;

    public PlotterTableObject(String name, double distanceMiles, double distanceKM) {
        this.name = name;
        this.distanceInMiles = distanceMiles;
        this.distanceInKM = distanceKM;
        this.isVisible = true;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public double getDistanceInKM() {
        return distanceInKM;
    }

    public double getDistanceInMiles() {
        return distanceInMiles;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
