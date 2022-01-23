package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import utils.*;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Modify appointment controller for the Modify Appointment screen.  Populates the modified appointment details based
 * on the selected appointment
 *
 * @author Aaron Kephart
 */
public class ModifyAppointment implements Initializable {
static Appointment modifyAppointment;

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
     * On initialize, load the types, contacts, customer, and user combo boxes.
     * Set all of the fields to be the passed "modifyAppointment" from the CustomerAppointments screen controller.
     * Verify appointment is not overlapping with another utilizing database functions to check.
     * @param url the url
     * @param resourceBundle the resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeCombo.getItems().clear();
        typeCombo.getItems().addAll("Planning Session", "De-Briefing", "General Discussion");
        contactCombo.setItems(ListManager.getAllContacts());
        customerComboBox.setItems(ListManager.getAllCustomers());
        userComboBox.setItems(ListManager.getAllUsers());

        id.setText(String.valueOf(modifyAppointment.getAppointmentID()));
        title.setText(String.valueOf(modifyAppointment.getTitle()));
        Description.setText(String.valueOf(modifyAppointment.getDescription()));
        Location.setText(String.valueOf(modifyAppointment.getLocation()));
        try {
            contactCombo.setValue(ComboBoxDB.chooseContact(modifyAppointment.getContact()));
            customerComboBox.setValue(CustomerDB.chooseCustomer(modifyAppointment.getCustomerID()));
            userComboBox.setValue(UserDB.chooseUser(modifyAppointment.getUserID()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        typeCombo.setValue(modifyAppointment.getType());
        date.setValue(modifyAppointment.getStart().toLocalDateTime().toLocalDate());
        populateStartEnd();
        start.setValue(modifyAppointment.getStart().toLocalDateTime().toLocalTime());
        end.setValue(modifyAppointment.getEnd().toLocalDateTime().toLocalTime());
    }

    /**
     * Check overlapping appointments, check appointment times are within range of business hours, validate
     * to ensure the form is filled out completely.
     * @param actionEvent the save button is pressed
     */
    public void onActionSave(javafx.event.ActionEvent actionEvent) throws SQLException, IOException {
        String error = "Alert - Issues with the following fields exist: \n";
        Boolean err = false;
        Timestamp timeStart = null;
        Timestamp timeEnd = null;



        if(title.getText().isBlank()) {
            err = true;
            error = error + "Title is blank\n";
        }
        if(Description.getText().isBlank()) {
            err = true;
            error += "Description is blank\n";
        }
        if(Location.getText().isBlank()) {
            err = true;
            error += "Location is blank\n";
        }
        if(typeCombo.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a type.\n";
        }
        if(contactCombo.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a contact.\n";
        }
        if(customerComboBox.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a customer.\n";
        }
        if(userComboBox.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a user.\n";
        }
        if(date.getValue() == null) {
            err = true;
            error += "Please select a date.\n";
        }
        if(start.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select a start time.\n";
        } else if(date.getValue() != null){
            timeStart = Timestamp.valueOf(LocalDateTime.of(date.getValue().getYear(), date.getValue().getMonth(), date.getValue().getDayOfMonth(), start.getValue().getHour(), start.getValue().getMinute()));
        }
        if(end.getSelectionModel().isEmpty()) {
            err = true;
            error += "Please select an end time.\n";
        } else if(date.getValue() != null) {
            timeEnd = Timestamp.valueOf(LocalDateTime.of(date.getValue().getYear(), date.getValue().getMonth(),date.getValue().getDayOfMonth(),end.getValue().getHour(), end.getValue().getMinute()));
        }

        if(err) {
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
                AppointmentsDB.update(modifyAppointment.getAppointmentID(),
                        title.getText(),
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
     * On cancel click, display the main menu
     * @param actionEvent the cancel button being clicked
     * @throws IOException in the event there is any issue displaying the main menu.
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
     * @param actionEvent the date is changed
     */
    public void dateOnAction(javafx.event.ActionEvent actionEvent) {
        populateStartEnd();
    }

    /**
     * Set the appointment "modifyAppointment" from the selection screen.
     * @param apt the selected appointment from the CustomerAppointments screen.
     */
    public static void setModifyAppointment(Appointment apt) {
        modifyAppointment = apt;
    }

    /**
     * Populate the start and end time selections.  Needs to be called after the date field is actually filled.
     */
    public void populateStartEnd() {
        start.getItems().clear();
        end.getItems().clear();
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
