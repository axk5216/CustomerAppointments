package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.AppointmentsDB;
import utils.UserDB;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Login Controller
 *
 * The Login controller reads system settings to determine local time zone and language settings
 * and also checks the login against the database to determine if valid.
 *
 * @author Aaron Kephart
 */
public class Login implements Initializable {
    public Label username_lbl; //username label
    public Label password_lbl; //password label
    public TextField username; //username - input text
    public TextField password; //password - input text
    public Button submit_lbl; //submit label
    public Label zoneDetectedlbl; //detected zone label
    public Label zone; //the zone detected
    private String title = "Invalid Login!";
    private String headerText = "Login Failed";
    private String contents = "Invalid username or password.  Please try again!";


    /**
     * On initialize, check the Locale and pass it to the "translate" resource bundle
     * to map for language translation to English or French.
     *
     * @param url the url
     * @param resourceBundle the resourceBundle referenced
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    resourceBundle = ResourceBundle.getBundle("translate", Locale.getDefault());
    username_lbl.setText(resourceBundle.getString("username"));
    password_lbl.setText(resourceBundle.getString("password"));
    submit_lbl.setText(resourceBundle.getString("submit"));
    zoneDetectedlbl.setText(resourceBundle.getString("zone_detected"));
    zone.setText(ZoneId.systemDefault().toString());
    title = resourceBundle.getString("title");
    headerText = resourceBundle.getString("headerText");
    contents = resourceBundle.getString("contents");
    }

    /**
     * When submission button is clicked, run a query from the DBQuery class
     * to determine if the username/password is valid.  If it is invalid,
     * display an alert stating that it is invalid.  Display appointments within 15 minutes
     * @param actionEvent submit button click
     */
   public void OnSubmitClick(ActionEvent actionEvent) throws IOException {
        if (UserDB.validateUser(username.getText(), password.getText())) {
            System.out.println("Success");
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
            try {
                AppointmentsDB.getFifteenMinute();
            } catch (SQLException throwables) {
                System.out.println("major error");
                throwables.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Notification - Upcoming Appointment");
            alert.setContentText(AppointmentsDB.getAppointmentNotify());
            alert.showAndWait();
        }
        else {
            Alert invalid = new Alert(Alert.AlertType.WARNING);
            invalid.setTitle(title);
            invalid.setHeaderText(headerText);
            invalid.setContentText(contents);
            invalid.showAndWait();
        }
    }
}
