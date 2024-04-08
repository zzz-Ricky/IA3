/**
 * The MedicalRecord class represents a medical record containing information about a treatment.
 * It implements the InfoManagement and DateManagement interfaces to manage treatment details and dates.
 * <p>
 * This class encapsulates details such as the location where the treatment occurred, treatment details,
 * and the date of the treatment.
 * </p>
 * <p>
 * Medical records are used to store and retrieve information about medical treatments for patients.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

public class MedicalRecord implements DateManageMent, InfoManagement {
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;

    /**
     * Constructs a new MedicalRecord object with the specified location, treatment
     * details, and date of treatment.
     * <p>
     * This constructor initializes a medical record with the given location where
     * the treatment occurred,
     * treatment details describing the procedure or medication administered, and
     * the date of the treatment.
     * </p>
     * <p>
     * The date of treatment must be provided in the format "YYYY-MM-DD" and is
     * validated to ensure it is a valid date.
     * If an invalid date format or an invalid date is provided, an
     * IllegalArgumentException is thrown.
     * </p>
     *
     * @param location         The location where the treatment occurred.
     * @param treatmentDetails Details of the treatment procedure or medication
     *                         administered.
     * @param dateOfTreatment  The date when the treatment occurred, in the format
     *                         "YYYY-MM-DD".
     * @throws IllegalArgumentException If the date of treatment is not in the
     *                                  correct format or represents an invalid
     *                                  date.
     */
    public MedicalRecord(Location location, String treatmentDetails, String dateOfTreatment) {
        this.location = location;
        this.treatmentDetails = treatmentDetails;
        validateDate(dateOfTreatment);
        this.dateOfTreatment = dateOfTreatment;
    }

    /**
     * Validates the format of the provided date string.
     *
     * @param date The date string to be validated.
     * @return true if the date string is in a valid format, false otherwise.
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String getDescription() {
        return treatmentDetails;
    }

    @Override
    public void setDescription(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }

    @Override
    public String getDate() {
        return dateOfTreatment;
    }

    @Override
    public void setDate(String dateOfTreatment) {
        validateDate(dateOfTreatment);
        this.dateOfTreatment = dateOfTreatment;
    }
}
