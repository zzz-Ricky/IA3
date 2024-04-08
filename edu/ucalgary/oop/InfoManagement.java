/**
 * The InfoManagement interface defines methods for changing descriptive string attributes.
 * Implementing classes must provide functionality to set and get string fields.
 * <p>
 * This interface is part of the edu.ucalgary.oop package.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

/**
 * The InfoManagement interface provides methods for managing information
 * description.
 */
public interface InfoManagement {

    /**
     * Sets the description.
     *
     * @param newDescription The new description to set.
     */
    public void setDescription(String newDescription);

    /**
     * Retrieves the description.
     *
     * @return The description.
     */
    public String getDescription();

}
