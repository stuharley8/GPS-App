package plotter;

import javafx.scene.canvas.Canvas;

public class CanvasLayer extends Canvas {
    private String name;
    public CanvasLayer (String name, double MAP_DIMENSIONS) {
        super(MAP_DIMENSIONS, MAP_DIMENSIONS);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
