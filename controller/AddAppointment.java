package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Contact;
import model.Customer;
import model.User;
import utils.AppointmentsDB;
import utils.ComboBoxDB;
import utils.CustomerDB;
import utils.ListManager;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Add Appointment controller for the AddAppointment screen.
 * Allows users to enter appointments and assign them to customers
 *
 * @author Aaron Kephart
 */
public class AddAppointment implements Initializable {

    @FXML
    private TextField title;

    @FXML
    private TextField Description;

    @FXML
    private TextField id;

    @FXML
    private ComboBox<Contact> contactCombo;

    @FXML
    private TextField Location;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<LocalTime> start;

    @FXML
    private ComboBox<LocalTime> end;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    void onActionSave(ActionEvent event) {

    }

    /**
     * Initialize method.  Populate the combo boxes for type, contact, customer, and user.
     * @param url the url
     * @param resourceBundle the resourcebundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeCombo.getItems().clear();
        typeCombo.getItems().addAll("Planning Session", "De-Briefing", "General Discussion");
        contactCombo.setItems(ListManager.getAllContacts());
        customerComboBox.setItems(ListManager.getAllCustomers());
        userComboBox.setItems(ListManager.getAllUsers());

    }

    /**
     * Check to ensure the form is filled out first.  If not, create an error string and display to user in an alert box
     * Once ensuring the form is filled out in its entirety, call the database class to check overlapping appointments.
     * @param actionEvent on the submission
     */
    public void onActionSave(javafx.event.ActionEvent actionEvent) throws SQLException, IOException {
        String error = "Alert - Issues with the following fields exist: \n";
        Boolean err = false;
        Timestamp timeStart = null;
        Timestamp timeEnd = null;


        if (title.getText().isBlank()) {
            err = true;
            error = error + "Title is blank\n";
        }
        if (Description.getText().isBlank()) {
            err = true;
            error += "Description is blank\n";
        }
        if (Location.getText().isBlank()) {
            err = true;
            error += "Location is blank\n";
        }
        if (typeCombo.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a type.\n";
        }
        if (contactCombo.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a contact.\n";
        }
        if (customerComboBox.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a customer.\n";
        }
        if (userComboBox.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a user.\n";
        }
        if (date.getValue() == null) {
            err = true;
            error += "Please select a date.\n";
        }
        if (start.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a start time.\n";
        } else if (date.getValue() != null) {
            timeStart = Timestamp.valueOf(LocalDateTime.of(date.getValue().getYear(), date.getValue().getMonth(), date.getValue().getDayOfMonth(), start.getValue().getHour(), start.getValue().getMinute()));
        }
        if (end.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select an end time.\n";
        } else if (date.getValue() != null) {
            timeEnd = Timestamp.valueOf(LocalDateTime.of(date.getValue().getYear(), date.getValue().getMonth(), date.getValue().getDayOfMonth(), end.getValue().getHour(), end.getValue().getMinute()));
        }

        if (err) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error in Submission");
            alert.setContentText(error);
            alert.showAndWait();
        } else {
            if (AppointmentsDB.checkOverlappingAppointments(timeStart, timeEnd, customerComboBox.getValue())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Overlapping Appointments!");
                alert.setContentText("There is an overlapping appointment.  Please try to adjust appointment times.");
                alert.showAndWait();
            } else {
               AppointmentsDB.add(title.getText(),
                        Description.getText(),
                        Location.getText(),
                        typeCombo.getValue(),
                        timeStart,
                        timeEnd,
                        userComboBox.getValue(),
                        customerComboBox.getValue(),
                        contactCombo.getValue());

                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/view/CustomerAppointments.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
        }
    }

    /**
     * Display the main menu when 'cancel' is pressed.
     * @param actionEvent the cancel button
     * @throws IOException in the event there are errors displaying the main screen
     */
    public void onActionDisplayMainMenu(javafx.event.ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/CustomerAppointments.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * When a date is selected, populate combo boxes with time selections based solely on the end users time zone.
     * This means that validation does not need to occur on time selection.  Logically knowing that 8 a.m - 10 p.m. is
     * exactly 14 hours, we can then add time selections to the list.
     * @param actionEvent
     */
    public void dateOnAction(javafx.event.ActionEvent actionEvent) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime businessOpen = LocalDateTime.of(date.getValue().getYear(), date.getValue().getMonth(),
                date.getValue().getDayOfMonth(), 8, 0);
        ZoneId zoneOfBusiness = ZoneId.of("America/New_York");
        ZoneId zoneofCustomer = ZoneId.systemDefault();

        ZonedDateTime businessStart = businessOpen.atZone(zoneOfBusiness);
        ZonedDateTime customerStart = businessStart.withZoneSameInstant(zoneofCustomer);

        LocalTime customerTime = LocalTime.of(customerStart.getHour(), customerStart.getMinute());

        System.out.println(dtf.format(businessStart));
        System.out.println(dtf.format(customerStart));

        for (int z = 0; z < 15; z++) {
            start.getItems().add(customerTime.plusHours(z));
            end.getItems().add(customerTime.plusHours(z));
        }

    }
}
