package model;

/**
 * Division class to hold first-level-division objects.
 *
 * @author Aaron Kephart
 */
public class Division {
    int divisionID;
    String division;
    int countryID;

    /**
     * Division constructor
     * @param divisionID the division ID
     * @param division the division name
     * @param countryID the associated country ID.
     */

    public Division(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
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

    /**
     * Override the toString class to display division name instead of entire object.  Used for combo boxes.
     * @return the division name only
     */
    @Override
    public String toString() {
        return(division);
    }
}
