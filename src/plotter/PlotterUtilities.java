package plotter;

import javafx.scene.paint.Color;

import java.util.HashMap;

public class PlotterUtilities {

    public static final String gradeFunction = "grade";
    public static final String speedFunction = "speed";
    public static final String defaultFunction = "default";

    private static final HashMap<Integer, Color> colors;

    static {
        HashMap<Integer, Color> map = new HashMap<>();
        map.put(0, Color.DARKBLUE);
        map.put(1, Color.CADETBLUE);
        map.put(2, Color.GREEN);
        map.put(3, Color.YELLOW);
        map.put(4, Color.ORANGE);
        map.put(5, Color.RED);
        colors = map;
    }

    public static Color getColor(int i) {
        return colors.get(i);
    }
    public static HashMap<Integer, Color> getColorMap(int i) {
        return colors;
    }

}
