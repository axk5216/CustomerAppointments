package utils;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * UserDB to query the database, add users to observable lists for selection, and to validate user logins.
 *
 * @author Aaron Kephart
 */
public abstract class UserDB {

public static int loggedInUserID;
public static String loggedInUserName;

    /**
     * Validates user login based on the username and password
     * LAMBDA EXPRESSION on line 42/43 to set two strings equivalent.
     *
     * @param username the supplied username
     * @param password the supplied password
     * @return true if the user is valid, false if not.
     */
    public static Boolean validateUser(String username, String password) {

        try {
            //Connection to database
            String sqlQuery = "SELECT * FROM users WHERE User_Name=? AND Password=?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
            ps.setString(1, username);
            ps.setString(2, password);


            //If a result set exists, then the combination is valid.  If it does not, then the login is invalid.
            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    loginAttempt(username, true);
                    setUser userSet = ((a, b) -> a=b);
                    userSet.user(loggedInUserName,set.getString("User_Name"));
                    loggedInUserID = set.getInt("User_ID");
                    return true;
                } else {
                    loginAttempt(username, false);
                    return false;
                }
            }

        } catch (IOException | SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Logger that logs whether or not the login was successful
     *
     * @param username the username that is passed and written in the log
     * @param success  boolean determining whether or not the attempt was successful.
     * @throws IOException I/O exception if writer fails to execute.
     */
    public static void loginAttempt(String username, Boolean success) throws IOException {

        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        fileWriter.append(LocalDateTime.now() + " " + username + " attempted to login.  SUCCESSFUL:  " + success.toString().toUpperCase() + "\n");
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Select user from the database and return
     * @throws SQLException in the event there is any issue selecting the user from the database.
     */
    public static void selectUser() throws SQLException {
        String selectCountry = "SELECT * FROM Users";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCountry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"));
            ListManager.addUser(user);
        }
    }

    /**
     * Query the database and return a user based on userID
     * @param userID the userID needing a user object
     * @throws SQLException in the event there is any issue with the execution of the SQL query
     */
    public static User chooseUser(int userID) throws SQLException {
        String selectCountry = "SELECT * FROM Users WHERE User_ID=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(selectCountry);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        User user = null;
        while (rs.next()) {
            user = new User(rs.getInt("User_ID"), rs.getString("User_Name"));
        }
        return user;
    }

    public static Integer getloggedInUserID() {

        return loggedInUserID;

    }
    public static String getLoggedInUsername() {

        return loggedInUserName;
    }

    /**
     * Lambda Interface to set two strings equal to each other.
     */
    interface setUser {
        String user(String a, String b);
    }
}
