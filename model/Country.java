package model;

/**
 * Country holder class for country objects to be added to a ComboBox
 *
 * @author Aaron Kephart
 */
public class Country {
    int countryID;
    String country;

    /**
     * Country constructor
     * @param countryID the country ID
     * @param country the country name
     */
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

//Setters and getters for the country
    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Necessary to display only the country variable in the combo box.
     * @return Country name only when the combo box is populated (toString)
     */
    @Override
    public String toString(){
        return(country);
    }
}
