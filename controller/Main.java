package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import utils.CustomerDB;
import utils.ListManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Customer screen to display customers and also offer navigation to other parts of the program
 * in particular, reports, or appointments.  Also allows user to manipulate customers by
 * creating, modifying, or deleting.
 *
 * @author Aaron Kephart
 */
public class Main implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TableView<Customer> customers;

    @FXML
    private TableColumn<Customer, Integer> customerID;

    @FXML
    private TableColumn<Customer, String> customerName;

    @FXML
    private TableColumn<Customer, String> customerAddress;

    @FXML
    private TableColumn<Customer, String> customerPostalCode;

    @FXML
    private TableColumn<Customer, String> customerPhone;

    @FXML
    private TableColumn<Customer, String> customerDivision;

    @FXML
    private TableColumn<Customer, String> country;

    /**
     * When the create customer button is pressed, display the Add Customer screen.
     * @param actionEvent the "Create" button is pressed
     * @throws IOException in the event there is any issue displaying the Add customer screen.
     */
    public void createCustomerBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * When the modify customer button is pressed, display the Modify Customer screen.
     * @param actionEvent the modify customer button is pressed
     * @throws IOException in the event there is any issue displaying the Modify customer screen.
     */
    public void modifyCustomerBtn(ActionEvent actionEvent) throws IOException {
        ModifyCustomer.setModifyCustomer(customers.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/ModifyCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * When the delete customer button is pressed, delete the selected customer and display a custom message.
     * Delete customer from the database, reload customers.
     * @param actionEvent the "Delete" button is pressed
     * @throws SQLException in the event there is any issue executing the deletion
     */
    public void deleteCustomerBtn(ActionEvent actionEvent) throws SQLException {
        Customer deleteCustomer = customers.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setContentText("Customer '"+ deleteCustomer.getCustomerName() + "has been deleted.");
        alert.showAndWait();
        CustomerDB.delete(deleteCustomer);
    }

    /**
     * When the "Appointments" button is pressed, display the appointments screen.
     * @param actionEvent the "Appointment" button is pressed
     * @throws IOException in the event there is any issue displaying the appointments screen
     */
    public void appointmentsBtn(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/CustomerAppointments.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * On initialize, select and populate the customer table columns with data.
     * @param url the url
     * @param rb the resourcebundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            CustomerDB.select();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customers.setItems(ListManager.getAllCustomers());
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));

    }

    /**
     * When the appointments by type button is pressed, display the appointments by type/month screen.
     * @param actionEvent the first button, "Report by type" is pressed
     * @throws IOException in the even there is any issue displaying the appointments by type screen.
     */
    public void report1(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Report1Type.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * When the appointments by contact button is pressed, display the report by contact (Contact Schedule) screen.
     * @param actionEvent the second button, appointments by contact is pressed
     * @throws IOException in the even there is any issue displaying the appointments by contact screen.
     */
    public void report2(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Report2Contact.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**
     * When the appointments by user button is pressed, display the appointments by user screen.
     * @param actionEvent the third button, "Appointments by user" is pressed
     * @throws IOException in the even there is any issue displaying the report by user screen.
     */
    public void report3(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Report3User.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
