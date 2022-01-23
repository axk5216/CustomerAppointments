package model;

/**
 * User class to hold user objects queried from the database.
 *
 * @author Aaron Kephart
 */

public class User {
    int userID;
    String userName;

    /**
     * User constructor
     * @param userID the user ID
     * @param userName the username
     */
    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    //setters and getters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Override the toString to display in combo boxes
     * @return the username only
     */
    @Override
    public String toString() {
        return(userName);
    }
}
