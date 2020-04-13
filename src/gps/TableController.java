/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Stuart Harley
 * Created: 4/11/2020
 */

package gps;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;

import static gps.Controller.choiceBoxList;

/**
 * Controller Class for the Table.fxml window
 */
public class TableController {

    @FXML
    private Menu trackSelectMenu;

    @FXML
    private Label titleLabel;

    @FXML
    private TableColumn col1;

    @FXML
    private TableColumn col2;

    @FXML
    private TableColumn col3;

    /**
     * Adds the loaded tracks to the select track menu dropdown
     */
    @FXML
    public void loadTrackChoices() {
        trackSelectMenu.getItems().clear();
        for(String track : choiceBoxList) {
            trackSelectMenu.getItems().add(new MenuItem(track));
        }
    }
}
