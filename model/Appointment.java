package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * The appointment class is a holder class for Appointment objects.  It mimicks the database structure
 * to hold data relevant to appointments.  Also used to add to tableviews through the ListManager.
 *
 * @author Aaron Kephart
 */
public class Appointment {
    int appointmentID;
    String title, description, location, type, contact;
    Timestamp start, end;
    int customerID, userID, contactID;

    /**
     * Constructor class
     * @param appointmentID the appointment ID
     * @param title the appointment title
     * @param description the appointment description
     * @param location the appointment location
     * @param type appointment type
     * @param contact contact type
     * @param start appointment start time
     * @param end appointment end time
     * @param customerID customer ID associated with the appointment
     * @param userID user ID associated with the appointment
     * @param contactID contact ID associated with the appointment
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, String contact, Timestamp start, Timestamp end, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.contact = contact;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    //Setters and getters for all defined variables for the appointment class.

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
