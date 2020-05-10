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
public class GradeTable extends CanvasLayer {
    private GraphicsContext gc = this.getGraphicsContext2D();
    private static double MAP_DIMENSIONS = 500;

    GradeTable() {
        super(null, MAP_DIMENSIONS);
        draw();
    }

    /**
     * draw table to convas
     */
    private void draw() {
        double x = MAP_DIMENSIONS - 100;
        double y = 10;

        gc.setFill(new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), .3));
        gc.fillRoundRect(x - 5, 0, 115, 110, 5, 5);

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.strokeText("Grades", x, y+3);
        y += 18;

        gc.setStroke(PlotterUtilities.getColor(0));
        gc.setFill(PlotterUtilities.getColor(0));
        gc.strokeText("< -5 %", x, y);
        y += 15;

        gc.setStroke(PlotterUtilities.getColor(1));
        gc.setFill(PlotterUtilities.getColor(1));
        gc.strokeText(">= -5 % and < -1 %", x, y);
        y += 15;

        gc.setStroke(PlotterUtilities.getColor(2));
        gc.setFill(PlotterUtilities.getColor(2));
        gc.strokeText(">= -1 % and < 1 %", x, y);
        y += 15;

        gc.setStroke(PlotterUtilities.getColor(3));
        gc.setFill(PlotterUtilities.getColor(3));
        gc.strokeText(">= 1 % and < 3 %", x, y);
        y += 15;

        gc.setStroke(PlotterUtilities.getColor(4));
        gc.setFill(PlotterUtilities.getColor(4));
        gc.strokeText(">= 3 % and < 5 %", x, y);
        y += 15;

        gc.setStroke(PlotterUtilities.getColor(5));
        gc.setFill(PlotterUtilities.getColor(5));
        gc.strokeText(">= 5 %", x, y);
        y += 15;
    }
}
