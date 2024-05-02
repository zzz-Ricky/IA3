/**
 * The Supply class represents a type of supply used in disaster relief operations.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The Supply class stores information about the type and quantity of a particular supply.
 * </p>
 * <p>
 * This class implements the InfoManagement interface, providing methods to manage and retrieve supply information.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 2024-04-07
 */

package edu.ucalgary.oop;

public class Supply implements InfoManagement {
    private String type;
    private int quantity;

    /**
     * Constructs a Supply object with the specified type and quantity.
     *
     * @param type     The type of supply.
     * @param quantity The quantity of the supply.
     */
    public Supply(String type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public String getDescription() {
        return type;
    }

    @Override
    public void setDescription(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            // Ideally, this error case should never be called due to SupplyManager class,
            // but just in
            // case.
            throw new IllegalArgumentException("Cannot set a supply to a negative quantity.");
        }
    }

}
