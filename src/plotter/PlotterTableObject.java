package plotter;

import javafx.scene.paint.Color;

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
