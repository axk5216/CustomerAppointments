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
import model.Customer;
import model.Division;
import utils.ComboBoxDB;
import utils.CustomerDB;
import utils.ListManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Modify customer screen controller
 * Takes selected customer, and offers the ability to modify the customer parameters.
 *
 * @author Aaron Kephart
 */
public class ModifyCustomer implements Initializable {


    private static Customer modifyCustomer;

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
     * Populate the fields with the modifyCustomer data to start.
     * @param url the url
     * @param resourceBundle the resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setText(String.valueOf(modifyCustomer.getCustomerID()));
        name.setText(String.valueOf(modifyCustomer.getCustomerName()));
        address.setText(String.valueOf(modifyCustomer.getAddress()));
        postalCode.setText(String.valueOf(modifyCustomer.getPostalCode()));
        phoneNumber.setText(String.valueOf(modifyCustomer.getPhone()));
        countryCombo.setItems(ListManager.getAllCountries());
        try {
            countryCombo.setValue(ComboBoxDB.chooseCountry(modifyCustomer.getCountryID()));
            divisionCombo.setValue(ComboBoxDB.chooseDivision(modifyCustomer.getDivisionID()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Save button - take all updated fields and use setters to modify the selected customer.
     * Write changes to database using an update query.
     * @param actionEvent save butotn is pressed
     * @throws SQLException in the event there is any issue executing the update query in SQL
     * @throws IOException in the event there is any issue displaying the Main screen after save is pressed.
     */
    public void onActionSave(ActionEvent actionEvent) throws SQLException, IOException {
        //CustomerDB.add(name.getText(), address.getText(), postalCode.getText(), phoneNumber.getText(), countryCombo.getValue(), divisionCombo.getValue());
        modifyCustomer.setCustomerName(name.getText());
        modifyCustomer.setAddress(address.getText());
        modifyCustomer.setPhone(phoneNumber.getText());
        modifyCustomer.setPostalCode((postalCode.getText()));
        modifyCustomer.setDivision(divisionCombo.getValue().getDivision());
        modifyCustomer.setDivisionID(divisionCombo.getValue().getDivisionID());
        modifyCustomer.setCountryID(countryCombo.getValue().getCountryID());
        modifyCustomer.setCountry(countryCombo.getValue().getCountry());

        CustomerDB.update(modifyCustomer);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * If cancel button is pressed, display the main menu
     * @param actionEvent cancel button is pressed
     * @throws IOException in the event the main menu has an issue displaying
     */
    public void onActionDisplayMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**
     * When the country combo box is selected, then populate the first level division.
     * @param actionEvent when the Country combo box is changed or updated
     */
    public void countryComboAction(ActionEvent actionEvent) {
        divisionCombo.setItems(ListManager.getSelectedDivisions(countryCombo.getValue().getCountryID()));
    }

    /**
     * Set the customer to modify from the Main screen (selected item in table view)
     * @param customer the customer to be modified.
     */
    public static void setModifyCustomer (Customer customer) {
        modifyCustomer = customer;
    }
}