/**
 * The DateManageMent interface defines methods for managing dates.
 * Implementing classes must provide functionality to set and get dates, as well as validate date formats.
 * <p>
 * Date formats must be in a specific format and adhere to validation rules.
 * </p>
 * <p>
 * This interface is part of the edu.ucalgary.oop package.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */
package edu.ucalgary.oop;

public interface DateManageMent {

    /**
     * Sets the date to the specified value.
     *
     * @param newDate The new date to be set.
     */
    public void setDate(String newDate);

    /**
     * Retrieves the currently set date.
     *
     * @return The currently set date.
     */
    public String getDate();

    /**
     * Validates the format of the provided date string.
     *
     * @param date The date string to be validated.
     * @return true if the date string is in a valid format, false otherwise.
     */
    public boolean validateDate(String date);
}
