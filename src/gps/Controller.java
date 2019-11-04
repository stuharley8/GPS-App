/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller Class for the GPS FXML
 */
public class Controller {

    //@FXML
    //private dropDown displayDropDown;

    @FXML
    private Label metricsLabel;

    @FXML
    public void displayOnAction() {

    }

    /**
     * Allows the user to close the application
     */
    @FXML
    public void exitOnAction() {
        Platform.exit();
    }

    @FXML
    public void loadOnAction() {

    }

}