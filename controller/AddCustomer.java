package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Division;
import utils.CustomerDB;
import utils.ListManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The AddCustomer controller class is the controller for the AddCustomer screen.  Allows users to add/create
 * new customers to the database.
 *
 * @author Aaron Kephart
 */

public class AddCustomer implements Initializable {

    //Customer information
    Parent scene;
    Stage stage;
    @FXML
    private TextField name;

    @FXML
    private TextField address;

    @FXML
    private TextField id;

    @FXML
    private ComboBox<Country> countryCombo;

    @FXML
    private ComboBox<Division> divisionCombo;

    @FXML
    private TextField postalCode;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;


    /**
     * Initialize class.  On initiation, populate the countryCombo box with countries
     * @param url the url
     * @param resourceBundle the associated resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(ListManager.getAllCountries());
    }

    /**
     * When the save button is pressed, add customer information to the customer DB, and then display the main screen.
     * @param actionEvent save button click
     * @throws SQLException in the event there is an issue creating the new customer in the database
     * @throws IOException in the event there is an IO exception when displaying the main FXML screen
     */
    public void onActionSave(ActionEvent actionEvent) throws SQLException, IOException {
        CustomerDB.add(name.getText(), address.getText(), postalCode.getText(), phoneNumber.getText(), countryCombo.getValue(), divisionCombo.getValue());

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * When the cancel button is pressed, display the main screen
     * @param actionEvent "cancel" button
     * @throws IOException in the event there is an issue displaying the main screen.
     */
    public void onActionDisplayMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**
     * When country combo items are selected or changed, automatically populate the division combo box.
     * @param actionEvent combo item box item selection
     */
    public void countryComboAction(ActionEvent actionEvent) {
        divisionCombo.setItems(ListManager.getSelectedDivisions(countryCombo.getValue().getCountryID()));
    }
}
