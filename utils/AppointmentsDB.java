package utils;


import model.*;
import java.sql.*;

/**
 * Appointment database queries
 *
 * @author Aaron Kephart
 */

public abstract class AppointmentsDB {
    static String appointmentNotify;

    /**
     * Selects ALL appointments from the database and the contact information for each appointment.
     * Create appointment objects and add to the observable list.
     * @throws SQLException in the event there is any issue executing the SQL statement.
     */
    public static void select() throws SQLException {
        ListManager.clearAppointments();
        String selectAppointment = "SELECT Appointment_ID, title, description, location, type, contacts.contact_name, start, end, customer_ID, user_id, contact_id FROM appointments " +
                " INNER JOIN contacts USING (contact_id)";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectAppointment);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getString("contact_name"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"),
                    rs.getInt("customer_ID"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id"));
            ListManager.addAppointment(appointment);
        }
    }

    /**
     * Updates appointment objects in the database based on appointment ID.
     * @param appointmentID the appointment ID to be updated
     * @param title new title
     * @param description new description
     * @param location new location
     * @param type new type
     * @param start new start timestamp
     * @param end new end timestamp
     * @param user new assigned user
     * @param cust new assigned customer
     * @param contact new assigned contact
     * @throws SQLException in the event there is any issue executing the SQL query
     */
    public static void update(int appointmentID, String title, String description, String location, String type, Timestamp start, Timestamp end, User user, Customer cust, Contact contact) throws SQLException {
        ListManager.clearAppointments();
        String selectAppointment = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Customer_ID=?, " +
                "User_ID=?, Contact_ID=? WHERE Appointment_ID=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectAppointment);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, cust.getCustomerID());
        ps.setInt(8, user.getUserID());
        ps.setInt(9, contact.getContactID());
        ps.setInt(10, appointmentID);
        ps.executeUpdate();
        select();
    }

    /**
     * Delete appointment from the database.
     * @param appointment the appointment to remove from the database
     * @throws SQLException in the event there is any issue executing the SQL query.
     */
    public static void delete(Appointment appointment) throws SQLException {
        String deleteAppointments ="DELETE FROM appointments WHERE Appointment_ID=?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteAppointments);
        ps.setInt(1, appointment.getAppointmentID());
        ps.executeUpdate();
        ListManager.getAllAppointments().remove(appointment);
    }

    /**
     * Select appointments based on this week, of the current date.
     * @throws SQLException in the event there is any issue executing the SQL statement
     */
    public static void selectWeekly() throws SQLException {
        String selectAppointment = "SELECT Appointment_ID, title, description, location, type, contacts.contact_name, start, end, customer_ID, user_id, contact_id FROM appointments " +
                " INNER JOIN contacts USING (contact_id) WHERE YEARWEEK(start) = YEARWEEK(NOW(), 0)";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectAppointment);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getString("contact_name"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"),
                    rs.getInt("customer_ID"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id"));
            ListManager.addWeeklyAppointment(appointment);
        }
    }

    /**
     * Select appointments based upon the month of the current date
     * @throws SQLException in the event there is any issue executing the SQL query.
     */
    public static void selectMonthly() throws SQLException {
        String selectAppointment = "SELECT Appointment_ID, title, description, location, type, contacts.contact_name, start, end, customer_ID, user_id, contact_id FROM appointments " +
                " INNER JOIN contacts USING (contact_id) WHERE MONTH(start) = MONTH(NOW())";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectAppointment);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getString("contact_name"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"),
                    rs.getInt("customer_ID"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id"));
            ListManager.addMonthlyAppointment(appointment);
        }
    }

    /**
     * Get appointments starting within 15 minutes and update the appointment notify string which is referenced
     * when login occurs to display a message to the user if there is an appointment starting in 15 minutes or not.
     * @throws SQLException in the event there is any issue with the SQL query
     */
    public static void getFifteenMinute() throws SQLException {
        appointmentNotify = "There are no appointments within the next 15 minutes";
        String fifteenMinute = "SELECT * FROM appointments WHERE start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 15 MINUTE) AND User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(fifteenMinute);

        ps.setInt(1, UserDB.getloggedInUserID());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            appointmentNotify = "ALERT - Appointment within 15 minutes!\n" +
                    "Appointment ID:  " + rs.getInt("Appointment_ID") +
                    "\nAppointment Date/Time:  " + rs.getTimestamp("start");
            System.out.println("Appointment Found");
        }
    }

    /**
     * Checking overlapping appointments.  If there is an overlapping appointment, return true.
     * @param start the start timestamp
     * @param end the end timestamp
     * @param cust the customer to check overlapping appointments for.
     * @return a boolean true indicating there is an overlapping appointment, or false if not.
     * @throws SQLException in the event there is any issue with executing the SQL query.
     */
    public static Boolean checkOverlappingAppointments(Timestamp start, Timestamp end, Customer cust) throws SQLException {
        String overlap = "SELECT * FROM appointments WHERE (? = start OR ? = end) OR (? < start AND ? > end) OR " +
                "(? > start AND ? < end) OR (? > start AND ? < end) OR (? > start AND ? < end) AND Customer_ID=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(overlap);
        ps.setTimestamp(1, start);
        ps.setTimestamp(2, end);
        ps.setTimestamp(3, start);
        ps.setTimestamp(4, end);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, start);
        ps.setTimestamp(7, end);
        ps.setTimestamp(8, end);
        ps.setTimestamp(9, start);
        ps.setTimestamp(10, end);
        ps.setInt(11, cust.getCustomerID());

        System.out.println(ps.toString());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * Return the appointment notification for 15 minute appointment notifications.
     * @return String containing indication of whether or not there are any appointments within 15 minutes.
     */
    public static String getAppointmentNotify() {
        return appointmentNotify;
    }

    /**
     * Add new appointment to the database and construct a new appointment object containing the parameters passed
     * @param title the appointment title
     * @param description description
     * @param location location
     * @param type type
     * @param start start timestamp
     * @param end end timestamp
     * @param user user
     * @param cust customer
     * @param contact contact
     * @throws SQLException in the event there is any problem executing the SQL statement.
     */
    public static void add(String title, String description, String location, String type, Timestamp start, Timestamp end, User user, Customer cust, Contact contact) throws SQLException {
        String addAppointment = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?,?,?,?,?,?,?,?,?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(addAppointment, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, cust.getCustomerID());
        ps.setInt(8, user.getUserID());
        ps.setInt(9, contact.getContactID());
        int i = ps.executeUpdate();

        ResultSet resultSet = ps.getGeneratedKeys();
        int appointmentID = 0;
        if (resultSet.next()) {
            appointmentID = resultSet.getInt(1);
        }
        Appointment appointment = new Appointment(appointmentID, title, description, location, type, contact.getName(), start, end, cust.getCustomerID(), UserDB.getloggedInUserID(), contact.getContactID());
        ListManager.addAppointment(appointment);
    }

    /**
     * Select appointments based on contact
     * @param contact the contact to get appointments for
     * @throws SQLException in the event there is any issue executing the SQL statement
     */
    public static void selectContactAppointment(Contact contact) throws SQLException {
        String selectAppointment = "SELECT Appointment_ID, title, description, location, type, contacts.contact_name, start, end, customer_ID, user_id, contact_id FROM appointments " +
                " INNER JOIN contacts USING (contact_id) WHERE contact_id=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectAppointment);
        ps.setInt(1, contact.getContactID());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getString("contact_name"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"),
                    rs.getInt("customer_ID"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id"));
            ListManager.addContactAppointment(appointment);
        }
    }

    /**
     * Select appointments based upon user, create appointment objects, add to list
     * @param user the user to select appointments for
     * @throws SQLException in the event there are any issues running the SQL query
     */
    public static void selectUserAppointment(User user) throws SQLException {
        String selectAppointment = "SELECT Appointment_ID, title, description, location, type, contacts.contact_name, start, end, customer_ID, user_id, contact_id FROM appointments " +
                " INNER JOIN contacts USING (contact_id) WHERE user_id=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectAppointment);
        ps.setInt(1, user.getUserID());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getString("contact_name"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"),
                    rs.getInt("customer_ID"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id"));
            ListManager.addUserAppointment(appointment);
        }
    }

    /**
     * Select appointments based upon type, create appointment objects to add to observable list.
     * @param type the type of appointment to select
     * @throws SQLException in the event there is any issue with the SQL query
     */
    public static void selectTypeAppointment(String type) throws SQLException {
        ListManager.clearTypeAppointment();
        String selectAppointment = "SELECT Appointment_ID, title, description, location, type, contacts.contact_name, start, end, customer_ID, user_id, contact_id FROM appointments " +
                " INNER JOIN contacts USING (contact_id) WHERE type=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectAppointment);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getString("contact_name"),
                    rs.getTimestamp("start"),
                    rs.getTimestamp("end"),
                    rs.getInt("customer_ID"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id"));
            ListManager.addTypeAppointment(appointment);
        }
    }

}
