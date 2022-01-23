package utils;


import model.Contact;
import model.Country;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database query class to select countries, divisions, and contacts from the database and create objects from these queries.
 * Class is abstract due to no need to initialize or use objects from this class.
 *
 * @author Aaron Kephart
 */

public abstract class ComboBoxDB {
    /**
     * Select all countries and order in ascending order. Using the country ID and country, construct a new country
     * object.  Add this object to the countries list in the List Manager class.
     * @throws SQLException if SQL query runs into any issue while running
     */
    public static void selectCountry() throws SQLException {
        String selectCountry = "SELECT * FROM Countries ORDER BY Country;";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCountry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Country country = new Country(rs.getInt("Country_ID"
            ), rs.getString("Country"));
            ListManager.addCountry(country);
        }
    }

    /**
     * Takes an integer id and queries the database to find the country with specified id
     * and returns a country object.  Used for the modify customer screen.
     * @param id the country id
     * @return the chosen country based on country ID
     * @throws SQLException in the event there is any issue with the SQL query.
     */
    public static Country chooseCountry(int id) throws SQLException {
        String selectCountry = "SELECT * FROM countries WHERE Country_ID=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCountry);
        Country country = null;
        try {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                country = new Country(rs.getInt("Country_ID"
                ), rs.getString("Country"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return country;
        }

    /**
     * Select all divisions and order in ascending order based on division name.
     * @throws SQLException if SQL query runs into any issue while running.
     */
    public static void selectDivision() throws SQLException {
        String selectCountry = "SELECT * FROM First_Level_Divisions ORDER BY Division";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCountry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Division division = new Division(rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    rs.getInt("Country_ID"));
            ListManager.addDivision(division);
        }
    }
    /**
     * Takes an integer id and queries the database to find the division with specified id
     * and returns a Division object.  Used for the modify customer screen.
     * @param id the division ID
     * @return division containing the division ID
     * @throws SQLException in the event there is any issue executing the SQL query.
     */
    public static Division chooseDivision(int id) throws SQLException {
        String selectCountry = "SELECT * FROM first_level_divisions WHERE Division_ID=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCountry);
        Division division = null;
        try {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                division = new Division(rs.getInt("Division_ID"
                ), rs.getString("Division"), rs.getInt("Country_ID"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return division;
    }
    /**
     * Select all contacts from the database.  Create contact objects and add them to the observable list.
     * Used primarily to populate combo boxes.
     * @throws SQLException in the event there is any issue with the SQL query
     */
    public static void selectContact() throws SQLException {
        String selectContact = "SELECT * FROM Contacts ORDER BY Contact_Name";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectContact);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Contact contact = new Contact(rs.getInt("Contact_ID"),
                    rs.getString("Contact_Name"),
                    rs.getString("Email"));
            ListManager.addContact(contact);
        }
    }

    /**
     * Takes an integer id and queries the database to find the country with specified id
     * and returns a Contact object.  Used for the modify appointment screen.
     * @param contact the contact name
     * @return contact containing the contact ID
     * @throws SQLException in the event there is any issue executing the SQL query.
     */
    public static Contact chooseContact(String contact) throws SQLException {
        String selectCountry = "SELECT * FROM contacts WHERE Contact_Name=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCountry);
        Contact con = null;
        try {
            ps.setString(1, contact);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                con = new Contact(rs.getInt("Contact_ID"
                ), rs.getString("Contact_Name"), rs.getString("Email"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }


}

