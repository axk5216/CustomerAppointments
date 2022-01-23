package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.SQLException;

/**
 * List Manager class containing all observable lists for the JavaFX controllers to add to tableviews, combo boxes,
 * or any other JavaFX object.
 *
 * @author Aaron Kephart
 */
public abstract class ListManager {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    private static ObservableList<Division> selectedDivision = FXCollections.observableArrayList();
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentType = FXCollections.observableArrayList();
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentContact = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentUser = FXCollections.observableArrayList();

    /**
     * Add customer object to the allCustomers list.
     *
     * @param cust customer to be added to the allCustomers list.
     */
    public static void addCustomer(Customer cust) {

        allCustomers.add(cust);
    }

    /**
     * Clear the customers observable list
     */
    public static void clearCustomer() {

        allCustomers.clear();
    }

    /**
     * Reload the entire customer list
     */
    public static void reloadCustomer() {
        allCustomers.clear();
        try {
            CustomerDB.select();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get the observable list of customers.
     *
     * @return observable list of customers
     */
    public static ObservableList<Customer> getAllCustomers() {

        return allCustomers;
    }

    /**
     * Get observable list of all countries
     *
     * @return observable list of all countries.
     */
    public static ObservableList<Country> getAllCountries() {

        return allCountries;
    }

    /**
     * Add country to allCountries observable list
     *
     * @param country the country to be added to the observable list.
     */
    public static void addCountry(Country country) {

        allCountries.add(country);
    }

    /**
     * Add division to observable list
     *
     * @param division the division to be added to the allDivisions list.
     */
    public static void addDivision(Division division) {

        allDivisions.add(division);
    }

    /**
     * Based on countryID, obtain the selected divisions based on the country ID.
     *
     * @param countryID the country ID for which we are seeking the divisions.
     * @return selectedDivision observable list
     */
    public static ObservableList<Division> getSelectedDivisions(int countryID) {
        selectedDivision.clear();
        for (int i = 0; i < allDivisions.size(); i++) {
            if (allDivisions.get(i).getCountryID() == countryID) {
                selectedDivision.add(allDivisions.get(i));
            }
        }
        return selectedDivision;
    }

    /**
     * Add appointment to the allAppointments observable list.
     *
     * @param appointment the appointment to be added to the observable list.
     */
    public static void addAppointment(Appointment appointment) {

        allAppointments.add(appointment);
    }

    /**
     * Add appointment based on type to the appointmentType observable list
     * @param appointment the appointment to add to the appointmentType observable list
     */
    public static void addTypeAppointment(Appointment appointment) {

        appointmentType.add(appointment);
    }

    /**
     * Clear appointments from the appointmentType observable list
     */
    public static void clearTypeAppointment() {
        appointmentType.clear();
    }

    /**
     * Get observable list of appointments
     * @param type appointment type to return
     * @return observable list of appointments based on type
     * @throws SQLException in the event there is any issue executing the SQL query
     */
    public static ObservableList<Appointment> getTypeAppointment(String type) throws SQLException {
        appointmentType.clear();
        AppointmentsDB.selectTypeAppointment(type);
        return appointmentType;
    }

    /**
     * Add appointment to the appointmentUser observable list
     * @param appointment the appointment to add to the appointmentUser list.
     */
    public static void addUserAppointment(Appointment appointment) {
        appointmentUser.add(appointment);
    }

    /**
     * Return appointments based on user
     * @param user the user to base appointments off of
     * @return observable list of appointments based on user
     * @throws SQLException in the event there is any issue with the SQL query
     */
    public static ObservableList<Appointment> getUserAppointment(User user) throws SQLException {
        appointmentUser.clear();
        AppointmentsDB.selectUserAppointment(user);
        return appointmentUser;
    }

    /**
     * Add appointment to the appointmentContact observable list
     * @param appointment the appointment to add to the contact appointment list
     */
    public static void addContactAppointment(Appointment appointment) {
        appointmentContact.add(appointment);
    }

    /**
     * Get list of appointments based upon contact
     * @param contact the contact to get appointments for
     * @return list of appointments based on supplied contact parameter
     * @throws SQLException in the event there is any issue executing the SQL query.
     */
    public static ObservableList<Appointment> getContactAppointment(Contact contact) throws SQLException {
        appointmentContact.clear();
        AppointmentsDB.selectContactAppointment(contact);
        return appointmentContact;
    }

    /**
     * Clear all appointments from the observable list "allAppointments"
     */
    public static void clearAppointments() {

        allAppointments.clear();
    }

    /**
     * Retreive all appointments.
     *
     * @return observable list of all appointments.
     */
    public static ObservableList<Appointment> getAllAppointments() {

        return allAppointments;
    }

    /**
     * Add weekly appointment to the observable list
     * @param appointment the appointment to add to the weekly appointment list.
     */
    public static void addWeeklyAppointment(Appointment appointment) {
        weeklyAppointments.add(appointment);
    }

    /**
     * Get all weekly appointments
     * @return the weekly appointments
     */
    public static ObservableList<Appointment> getWeeklyAppointments() {
        return weeklyAppointments;
    }

    /**
     * Add monthly appointment
     * @param appointment the appointment to add to the monthly observable list
     */
    public static void addMonthlyAppointment(Appointment appointment) {
        monthlyAppointments.add(appointment);
    }

    /**
     * Get all monthly appointments observable list
     * @return an observable list of monthly appointments
     */
    public static ObservableList<Appointment> getMonthlyAppointments() {
        return monthlyAppointments;
    }


    /**
     * Retrieve all contacts from the observable list.
     *
     * @return an observable list of contacts.
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /**
     * Add contact to the contacts observable list.
     *
     * @param contact the contact to add to the observable list.
     */
    public static void addContact(Contact contact) {
        allContacts.add(contact);
    }

    /**
     * Add user to observable list allUsers
     * @param user the user to add to the allUsers observable list
     */
    public static void addUser(User user) {
        allUsers.add(user);
    }

    /**
     * Return all users in an observable list
     * @return all users in observable list
     */
    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }
}
