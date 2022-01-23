package model;

import java.util.Date;

/**
 * Customer class to hold customer objects.  Customers are added to table views and modification is done to this class,
 * as well as to the database utilizing other classes.
 *
 * @author Aaron Kephart
 */
public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private String country, division;
    private int countryID, divisionID;

    /**
     * Customer constructor
     * @param customerID the customer ID defined by the database
     * @param customerName the customer name
     * @param address the customer address
     * @param postalCode the customer postal code
     * @param phone the customer phone number
     * @param country country
     * @param division division
     * @param countryID country ID
     * @param divisionID division ID
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phone, String country, String division, int countryID, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.country = country;
        this.division = division;
        this.countryID = countryID;
        this.divisionID = divisionID;
    }
//Setters and getters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String toString() {
        return (customerName);
    }
}
