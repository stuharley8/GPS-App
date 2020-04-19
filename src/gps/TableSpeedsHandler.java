/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Stuart Harley
 * Created: 4/16/2020
 */

package gps;

/**
 * Class handles calculating the metrics displayed to the table
 */
public class TableSpeedsHandler {

    private static final double KM_TO_MILES = 0.621371;

    private Track track;
    private double totalMin = 0;
    private double threeLessMin = 0;
    private double sevenLessMin = 0;
    private double tenLessMin = 0;
    private double fifteenLessMin = 0;
    private double twentyLessMin = 0;
    private double twentyGreaterMin = 0;

    /**
     * Constructor
     * @param track the track that will have the times at speeds calculated
     */
    public TableSpeedsHandler(Track track) {
        this.track = track;
        calcTimesAtSpeeds();
    }

    private void calcTimesAtSpeeds() {
        for(int i = 0; i < track.getNumPoints()-1; i++) {
            long totalTime = Math.abs(track.getPoint(i+1).getDate().getTime()
                    - track.getPoint(i).getDate().getTime());
            double seconds = totalTime / 1000.0;
            double minutes = seconds / 60;
            double hours = minutes / 60;
            totalMin += minutes;
            double miles = track.distanceCalc(track.getPoint(i), track.getPoint(i+1)) * KM_TO_MILES;
            double mph = miles/hours;
            if(mph < 3) {
                threeLessMin += minutes;
            } else if (mph < 7) {
                sevenLessMin += minutes;
            } else if (mph < 10) {
                tenLessMin += minutes;
            } else if (mph < 15) {
                fifteenLessMin += minutes;
            } else if (mph < 20) {
                twentyLessMin += minutes;
            } else {
                twentyGreaterMin += minutes;
            }
        }
    }

    public double getThreeLessMin() {
        return Controller.round(threeLessMin, 2);
    }

    public double getSevenLessMin() {
        return Controller.round(sevenLessMin, 2);
    }

    public double getTenLessMin() {
        return Controller.round(tenLessMin, 2);
    }

    public double getFifteenLessMin() {
        return Controller.round(fifteenLessMin, 2);
    }

    public double getTwentyLessMin() {
        return Controller.round(twentyLessMin, 2);
    }

    public double getTwentyGreaterMin() {
        return Controller.round(twentyGreaterMin, 2);
    }

    public double getPercentThreeLess() {
        return Controller.round(threeLessMin/totalMin*100, 2);
    }

    public double getPercentSevenLess() {
        return Controller.round(sevenLessMin/totalMin*100, 2);
    }

    public double getPercentTenLess() {
        return Controller.round(tenLessMin/totalMin*100, 2);
    }

    public double getPercentFifteenLess() {
        return Controller.round(fifteenLessMin/totalMin*100, 2);
    }

    public double getPercentTwentyLess() {
        return Controller.round(twentyLessMin/totalMin*100, 2);
    }

    public double getPercentTwentyGreater() {
        return Controller.round(twentyGreaterMin/totalMin*100, 2);
    }
}
