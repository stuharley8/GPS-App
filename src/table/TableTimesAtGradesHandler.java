/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Stuart Harley
 * Created: 4/30/2020
 */

package table;

import gps.Track;

/**
 * Class handles calculating the times spent at grades that will be displayed to the table
 */
public class TableTimesAtGradesHandler {

    private Track track;
    private double totalMin = 0;
    private double negFiveLessMin = 0;
    private double negOneLessMin = 0;
    private double oneLessMin = 0;
    private double threeLessMin = 0;
    private double fiveLessMin = 0;
    private double fiveGreaterMin = 0;

    /**
     * Constructor
     * @param track the track that will have the times at grades calculated
     */
    public TableTimesAtGradesHandler(Track track) {
        this.track = track;
        calcTimesAtGrades();
    }

    private void calcTimesAtGrades() {
        for(int i = 0; i < track.getNumPoints()-1; i++) {
            long totalTime = Math.abs(track.getPoint(i+1).getDate().getTime()
                    - track.getPoint(i).getDate().getTime());
            double seconds = totalTime / 1000.0;
            double minutes = seconds / 60;
            totalMin += minutes;
            double grade = Track.gradeCalc(track.getPoint(i), track.getPoint(i+1));
            if(grade < -5) {
                negFiveLessMin += minutes;
            } else if (grade < -1) {
                negOneLessMin += minutes;
            } else if (grade < 1) {
                oneLessMin += minutes;
            } else if (grade < 3) {
                threeLessMin += minutes;
            } else if (grade < 5) {
                fiveLessMin += minutes;
            } else {
                fiveGreaterMin += minutes;
            }
        }
    }

    public double getNegFiveLessMin() {
        return TableController.round(negFiveLessMin, 2);
    }

    public double getNegOneLessMin() {
        return TableController.round(negOneLessMin, 2);
    }

    public double getOneLessMin() {
        return TableController.round(oneLessMin, 2);
    }

    public double getThreeLessMin() {
        return TableController.round(threeLessMin, 2);
    }

    public double getFiveLessMin() {
        return TableController.round(fiveLessMin, 2);
    }

    public double getFiveGreaterMin() {
        return TableController.round(fiveGreaterMin, 2);
    }

    public double getPercentNegFiveLess() {
        return TableController.round(negFiveLessMin/totalMin*100, 2);
    }

    public double getPercentNegOneLess() {
        return TableController.round(negOneLessMin/totalMin*100, 2);
    }

    public double getPercentOneLess() {
        return TableController.round(oneLessMin/totalMin*100, 2);
    }

    public double getPercentThreeLess() {
        return TableController.round(threeLessMin/totalMin*100, 2);
    }

    public double getPercentFiveLess() {
        return TableController.round(fiveLessMin/totalMin*100, 2);
    }

    public double getPercentFiveGreater() {
        return TableController.round(fiveGreaterMin/totalMin*100, 2);
    }
}
