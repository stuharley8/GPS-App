/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Stuart Harley
 * Created: 4/16/2020
 */

package gps;

/**
 * Class represents one row of the table
 */
public class TableRow {

    private String col1;
    private String col2;
    private String col3;

    /**
     * Constructor for a table row with three columns
     * @param col1 the column 1 value
     * @param col2 the column 2 value
     * @param col3 the column 3 value
     */
    public TableRow(String col1, String col2, String col3) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
    }

    public String getCol1() {
        return col1;
    }

    public String getCol2() {
        return col2;
    }

    public String getCol3() {
        return col3;
    }
}