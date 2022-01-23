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
import utils.AppointmentsDB;
import utils.ListManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * Main view of appointments.  Allows users to view all appointments by week, month, or all based on
 * radio buttons.  Also allows users to create, modify, or delete appointments.
 *
 * @author Aaron Kephart
 */
public class CustomerAppointments implements Initializable {

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
    private RadioButton all;

    @FXML
    private ToggleGroup appointmentView;

    @FXML
    private RadioButton weekly;

    @FXML
    private RadioButton monthly;

    /**
     * Initialization screen.  Populate appointments table view.
     * @param url the url
     * @param resourceBundle the resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            AppointmentsDB.select();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        appointments.setItems(ListManager.getAllAppointments());
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


    }

    /**
     * Display the create appointment screen when user clicks "Create" in the interface.
     * @param actionEvent "Create" button is clicked
     * @throws IOException in the event there are any issues displaying the create appointment screen.
     */
    public void createAppointmentBtn(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Method to handle displaying the modify appointment screen when the "Modify" button is clicked
     * @param actionEvent "Modify" button clicked
     * @throws IOException in the event there are any issues displaying the modify appointment screen.
     */
    public void modifyAppointmentBtn(ActionEvent actionEvent) throws IOException {
        ModifyAppointment.setModifyAppointment(appointments.getSelectionModel().getSelectedItem());

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/ModifyAppointment.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Handles when the delete button is pressed.  Deletes the appointment from the database, and the list.
     * Also display a custom message containing the type of appointment canceled and the ID
     * @param actionEvent the delete appointment button is pressed
     * @throws SQLException in the event there is an issue executing the delete query
     */
    public void deleteAppointmentBtn(ActionEvent actionEvent) throws SQLException {
        int appointmentID = appointments.getSelectionModel().getSelectedItem().getAppointmentID();
        String appointmentType = appointments.getSelectionModel().getSelectedItem().getType();
        AppointmentsDB.delete(appointments.getSelectionModel().getSelectedItem());
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Appointment Deleted");
        alert.setContentText("You deleted the following appointment:\n" +
            "Appointment ID:  " + appointmentID
        + "\nType:  " + appointmentType);
        alert.showAndWait();
    }

    /**
     * Radio button for "All" appointments.  When clicked, display all appointments.
     * @param actionEvent "All" radio button is clicked
     * @throws SQLException in the event there is any issue executing the SQL query to load the table
     */
    public void allOnAction(ActionEvent actionEvent) throws SQLException {
        appointments.getItems().clear();
        AppointmentsDB.select();
        appointments.setItems(ListManager.getAllAppointments());
    }
    /**
     * Radio button for "Weekly" appointments.  When clicked, display week of appointments based on the current
     * user's week.
     * @param actionEvent "Weekly" radio button is clicked
     * @throws SQLException in the event there is any issue executing the SQL query to load the table
     */
    public void weeklyOnAction(ActionEvent actionEvent) throws SQLException {
        appointments.getItems().clear();
        AppointmentsDB.selectWeekly();
        appointments.setItems(ListManager.getWeeklyAppointments());
    }
    /**
     * Radio button for "Monthly" appointments.  When clicked, display all appointments for the current month.
     * @param actionEvent "Monthly" radio button is clicked
     * @throws SQLException in the event there is any issue executing the SQL query to load the table
     */
    public void monthlyOnAction(ActionEvent actionEvent) throws SQLException {
        appointments.getItems().clear();
        AppointmentsDB.selectMonthly();
        appointments.setItems(ListManager.getMonthlyAppointments());
    }

    /**
     * Button to allow user to go back to the main screen, where customer table is located.
     * @param actionEvent "Back to Customers" button is clicked
     * @throws IOException in the event there is any issues with displaying the main screen.
     */

    public void onActionBackToMain(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
