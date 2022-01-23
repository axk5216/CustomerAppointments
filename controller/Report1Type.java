package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import utils.ListManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * Report 1
 * Total number of customer appointments by type and month.  Can sort by type.
 * Populate table view with information and allow user to manipulate.
 *
 * @author Aaron Kephart
 */
public class Report1Type implements Initializable {

    @FXML
    private TableView<Appointment> appointments;

    @FXML
    private TableColumn<Appointment, Integer> id;

    @FXML
    private TableColumn<Appointment, String> title;

    @FXML
    private TableColumn<Appointment, String> description;

    @FXML
    private TableColumn<Appointment, String> location;

    @FXML
    private TableColumn<Appointment, String> contact;

    @FXML
    private TableColumn<Appointment, String> type;

    @FXML
    private TableColumn<Appointment, Timestamp> start;

    @FXML
    private TableColumn<Appointment, Timestamp> end;

    @FXML
    private TableColumn<Appointment, Integer> customerID;

    @FXML
    private TableColumn<Appointment, Integer> userID;

    @FXML
    private Label count;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private Button backToMainMenu;

    /**
     * Initialize the table with appointments
     *
     * LAMBDA Expression - set button action back to main menu
     *
     * @param url the URL
     * @param resourceBundle the associated ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointments.setItems(ListManager.getAllAppointments());
        typeCombo.getItems().addAll("Planning Session", "De-Briefing", "General Discussion");
        id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));

        backToMainMenu.setOnAction((event) -> {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = null;
            try {
                scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        });

    }

    /**
     * On action of the combo box selector.  Update the table with selected items, querying the database if necessary.
     * @param actionEvent the event "action"
     * @throws SQLException in the event there are any issues with the SQL query.
     */
    public void typeComboOnAction(ActionEvent actionEvent) throws SQLException {
        appointments.getItems().clear();
        appointments.setItems(ListManager.getTypeAppointment(typeCombo.getValue()));


        count.setText(String.valueOf(appointments.getItems().size()));
    }
}
