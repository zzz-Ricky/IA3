/**
 * The DietaryRestrictions class represents various dietary restrictions that can be assigned to a person.
 * It provides an enumeration of common dietary restriction codes and methods to get the assigned restriction.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */
package edu.ucalgary.oop;

public class DietaryRestrictions {

    /**
     * An enumeration of common dietary restriction codes.
     */
    public enum DietaryRestriction {
        AVML, // Asian Vegetarian Meal
        DBML, // Diabetic Meal
        GFML, // Gluten-Free Meal
        KSML, // Kosher Meal
        LSML, // Low Sodium Meal
        MOML, // Muslim Meal
        PFML, // Peanut-Free Meal
        VGML, // Vegetarian Meal
        VJML // Vegan Meal
    }

    private DietaryRestriction restriction;

    /**
     * Constructs a new DietaryRestrictions object with the specified restriction.
     *
     * @param restriction The dietary restriction to be assigned.
     */
    public DietaryRestrictions(DietaryRestriction restriction) {
        this.restriction = restriction;
    }

    /**
     * Retrieves the dietary restriction assigned to this object.
     *
     * @return The dietary restriction assigned.
     */
    public DietaryRestriction getRestriction() {
        return restriction;
    }
}
