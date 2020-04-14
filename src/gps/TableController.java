/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Stuart Harley
 * Created: 4/11/2020
 */

package gps;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;

import java.util.List;

/**
 * Controller Class for the Table.fxml window
 */
public class TableController {

    private Track currentTrack;
    private static String trackName = "Track";
    private static String data = "Data";

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
     * @param choices the list of track name that have been loaded in
     */
    @FXML
    public void loadTrackChoices(List<String> choices) {
        EventHandler<ActionEvent> handler = this::selectTrack;
        trackSelectMenu.getItems().clear();
        for(String track : choices) {
            trackSelectMenu.getItems().add(createMenuItem(track, handler));
        }
    }

    @FXML
    public void selectTrack(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        if(!menuItem.getText().equals("Track Select")) {  // Gets the menu as well so this checks for the menuItem
            trackName = menuItem.getText();
            setTitleText();
            for(Track track : )
        }
    }

    private MenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem result = new MenuItem(text);
        result.setOnAction(handler);
        return result;
    }

    @FXML
    private void setTitleText() {
        titleLabel.setText(trackName + " : " + data);
    }

    @FXML
    public void selectData(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        data = menuItem.getText();
        setTitleText();
        updateTable();
    }

    @FXML
    private void updateTable() {
        switch(data) {
            case "Times at Various Speeds":
                col1.setText("Speed");
                col2.setText("Time (min)");
                col3.setText("% Total Time");
        }
    }
}
