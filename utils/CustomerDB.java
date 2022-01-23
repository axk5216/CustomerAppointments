package utils;


import model.Contact;
import model.Country;
import model.Customer;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Customer database class used to query the customer tables to select, add, customers, or delete customers.
 *
 * @author Aaron Kephart
 */
public abstract class CustomerDB {
    /**
     * Select all customers form the customer table, create new customer objects from the Customer class,
     * and add the new customers to the customer observable list in the ListManager class.
     * @throws SQLException
     */
    public static void select() throws SQLException {
        ListManager.clearCustomer();
        String selectCustomer ="SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, countries.Country, first_level_divisions.Division, countries.Country_ID, first_level_divisions.Division_ID " +
                "FROM customers " +
                "INNER JOIN first_level_divisions USING (Division_ID)\n" +
                "INNER JOIN countries USING (Country_ID);";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCustomer);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Customer customer = new Customer(rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    rs.getString("countries.Country"),
                    rs.getString("Division"),
                    rs.getInt("Country_ID"),
                    rs.getInt("Division_ID"));
            ListManager.addCustomer(customer);
        }
    }

    /**
     * Add a new customer to the database, to the customer table.  Add newly created customer to the
     * observable list.
     *
     * @param customerName the customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param phone customer phone number
     * @param country customer country
     * @param division customer division information
     * @throws SQLException in the event there is any issue with the SQL query
     */
    public static void add(String customerName, String address, String postalCode, String phone, Country country, Division division) throws SQLException {
        String addCustomer ="INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES (?,?,?,?,?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(addCustomer, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, division.getDivisionID());
        int i = ps.executeUpdate();

        ResultSet resultSet = ps.getGeneratedKeys();
        int customerID = 0;
        if (resultSet.next()) {
            customerID = resultSet.getInt(1);
        }
        Customer customer = new Customer(customerID, customerName, address, postalCode, phone, country.getCountry(), division.getDivision(), division.getCountryID(), division.getDivisionID());
        ListManager.addCustomer(customer);
    }

    /**
     * Update customer in the database with new information.
     * @param cust the customer being updated
     * @throws SQLException in the event there is any issue with the SQL query.
     */
    public static void update(Customer cust) throws SQLException {
        String updateCustomer ="UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? "
                + "WHERE Customer_ID=?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(updateCustomer);
        ps.setString(1, cust.getCustomerName());
        ps.setString(2, cust.getAddress());
        ps.setString(3, cust.getPostalCode());
        ps.setString(4, cust.getPhone());
        ps.setInt(5, cust.getDivisionID());
        ps.setInt(6, cust.getCustomerID());

        ps.executeUpdate();
        ListManager.reloadCustomer();
    }

    /**
     * Delete customer entirely from the database and the Listmanager.
     * @param cust customer to be deleted
     * @throws SQLException in the event there is any issue with the SQL query.
     */
    public static void delete(Customer cust) throws SQLException {
        String deleteAppointments ="DELETE FROM appointments WHERE Customer_ID=?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteAppointments);
        ps.setInt(1, cust.getCustomerID());

        String deleteCustomer ="DELETE FROM customers WHERE Customer_ID=?;";
        PreparedStatement ps1 = JDBC.connection.prepareStatement(deleteCustomer);
        ps1.setInt(1, cust.getCustomerID());

        ps.executeUpdate();
        ps1.executeUpdate();
        ListManager.getAllCustomers().remove(cust);

    }

    /**
     * Find one customer from the database and return that particular customer
     * @param custID the customer ID
     * @return customer matching customer ID from the database
     * @throws SQLException in the event there is any issue with the SQL query
     */
    public static Customer chooseCustomer(int custID) throws SQLException {
        String selectCustomer ="SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, countries.Country, first_level_divisions.Division, countries.Country_ID, first_level_divisions.Division_ID " +
                "FROM customers " +
                "INNER JOIN first_level_divisions USING (Division_ID)\n" +
                "INNER JOIN countries USING (Country_ID)" +
                "WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCustomer);
        ps.setInt(1, custID);
        ResultSet rs = ps.executeQuery();
        Customer cust = null;
        while(rs.next()) {
            cust = new Customer(rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    rs.getString("countries.Country"),
                    rs.getString("Division"),
                    rs.getInt("Country_ID"),
                    rs.getInt("Division_ID"));
        }
        return cust;
    }

}
