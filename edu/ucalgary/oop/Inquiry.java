/**
 * The Inquiry class represents an inquiry made by an inquirer
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The Inquiry class stores information relating to the disaster victim which
 * a given inquirer is looking for. This includes the date of the inquiry, 
 * a written description statement, and a last known (shelter) location.
 * </p>
 * <p>
 * The class provides methods to set and get information relating to this class.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

public class Inquiry implements DateManageMent, InfoManagement {
    private DisasterVictim missingPerson;
    private String dateOfInquiry;
    private String description;
    private Location lastKnownLocation;
    
    /**
     * Constructs a new Inquiry object with the specified parameters.
     *
     * @param missingPerson    The missing person associated with the inquiry.
     * @param dateOfInquiry    The date of the inquiry in the format "YYYY-MM-DD".
     * @param description      The description of the inquiry.
     * @param lastKnownLocation The last known location of the missing person.
     */
    public Inquiry(DisasterVictim missingPerson,
            String dateOfInquiry, String description, Location lastKnownLocation) {
        this.missingPerson = missingPerson;
        validateDate(dateOfInquiry);
        this.dateOfInquiry = dateOfInquiry;
        this.description = description;
        this.lastKnownLocation = lastKnownLocation;
    }
    

    /**
     * Validates a date string in the format "YYYY-MM-DD".
     *
     * @param date The date string to validate.
     * @return true if the date is valid, otherwise false.
     * @throws IllegalArgumentException if the date format is invalid or if the date values are not within valid ranges.
     */
    @Override
    public boolean validateDate(String date) {
        // Check if the date has the date in the correct format such as "2024-01-18"
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date must be in the format YYYY-MM-DD");
        }

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        // Check if the date numbers are possible real times.
        if (month < 1 || month > 12 || day < 1 || day > 31 ||
                (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) ||
                (month == 2 && (day > 29 || (day > 28 && !(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)))))) {
            throw new IllegalArgumentException("An Invalid date was given, must be a valid year, month, and day");
        }

        return true;
    }

    public DisasterVictim getMissingPerson() {
        return missingPerson;
    }

    public void setMissingPerson(DisasterVictim missingPerson) {
        this.missingPerson = missingPerson;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setDate(String dateOfInquiry) {
        validateDate(dateOfInquiry);
        this.dateOfInquiry = dateOfInquiry;
    }

    @Override
    public String getDate() {
        return dateOfInquiry;
    }

}
