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
import model.Contact;
import utils.ListManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * Report 2
 * Schedule for each contact in organization including appointment ID, title, type, and description, start, end, customer ID.
 * Populate table view with information and allow user to manipulate.
 *
 * @author Aaron Kephart
 */
public class Report2Contact implements Initializable {

    @FXML
    private TableView<Appointment> appointments;

    @FXML
    private TableColumn<Appointment, Integer> id;

    @FXML
    private TableColumn<Appointment, String> title;

    @FXML
    private TableColumn<Appointment, String> description;

    @FXML
    private TableColumn<Appointment, String> type;

    @FXML
    private TableColumn<Appointment, Timestamp> start;

    @FXML
    private TableColumn<Appointment, Timestamp> end;

    @FXML
    private TableColumn<Appointment, Integer> customerID;

    @FXML
    private Label count;

    @FXML
    private ComboBox<Contact> typeCombo;

    /**
     * Initialize the table with appointments
     * @param url the URL
     * @param resourceBundle the associated ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointments.setItems(ListManager.getAllAppointments());
        typeCombo.setItems(ListManager.getAllContacts());
        id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));


    }

    /**
     * On action of the combo box selector.  Update the table with selected items, querying the database if necessary.
     * @param actionEvent the event "action"
     * @throws SQLException in the event there are any issues with the SQL query.
     */
    public void typeComboOnAction(ActionEvent actionEvent) throws SQLException {
        appointments.getItems().clear();
        appointments.setItems(ListManager.getContactAppointment(typeCombo.getValue()));
        count.setText(String.valueOf(appointments.getItems().size()));
    }

    /**
     * Back to the main menu
     * @param actionEvent clicking the main menu button.
     * @throws IOException in the event the main menu cannot be displayed.
     */
    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
