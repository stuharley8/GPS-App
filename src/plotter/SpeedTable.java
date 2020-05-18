/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Matt Haas
 * Created: 5/10/2020
 */

package plotter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Handles drawing the table on the plotter
 */
public class SpeedTable extends CanvasLayer {
    private GraphicsContext gc = this.getGraphicsContext2D();
    private static double MAP_DIMENSIONS = 500;
    private double speed = 0;

    SpeedTable() {
        super(null, MAP_DIMENSIONS);
        draw();
    }

    /**
     * draw table to convas
     */
    private void draw() {
        gc.clearRect(0, 0, MAP_DIMENSIONS, MAP_DIMENSIONS);
        double x = MAP_DIMENSIONS - 150;
        double y = 10;

        gc.setFill(new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), .3));
        gc.fillRoundRect(x - 10, 0, 155, 45, 5, 5);

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.strokeText("Track sections exceeding \n" + String.format("%.1f", speed) + " KPH marked in black", x, y + 8);
        y += 18;

    }

    public void setSpeed(double speed) {
        this.speed = speed;
        draw();
    }


}
