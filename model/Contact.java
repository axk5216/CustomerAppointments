package model;

/**
 * Contact holder class for all Contact objects.
 *
 * @author Aaron Kephart
 */
public class Contact {
    int contactID; //the contact ID
    String name, email; //Contact names and e-mails

    /**
     * Contact constructor
     * @param contactID the contact ID as specified in the database.
     * @param name the name as specified in the database.
     * @param email for the contact as specified in the database.
     */
    public Contact(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }

    //setters and getters
    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Override the toString method to display contact names only for combo boxes.
     * @return contact names.
     */
    @Override
    public String toString() {
        return(name);
    }
}
