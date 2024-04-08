/**
 * The Location class represents relief center that houses DisasterVictims.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
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

import java.util.HashSet;

public class Location {
    private String name;
    private String address;
    private HashSet<DisasterVictim> occupants;
    private HashSet<Supply> supplies;

    /**
     * Constructs a new Location object with the specified parameters.
     *
     * @param name    The name of the relief shelter/Location
     * @param address The address of the relief shelter/Location.
     */
    public Location(String name, String address) {
        // We are assuming that since the addition of new locations only occurs as a
        // result of configuring the initial state of the program, the operator is
        // conscious enough to not make locations with duplicate names and addresses
        this.name = name;
        this.address = address;
        this.occupants = new HashSet<>();
        this.supplies = new HashSet<>();
        // In a more comprehensive implementation, it would be ideal to create a static
        // HashSet attribute that contains all existing locations so we can check for
        // duplicates during construction. Adding a location ID will also guarantee key
        // uniqueness. This is omitted due to the considerations of the original UML
        // design
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashSet<DisasterVictim> getOccupants() {
        return occupants;
    }

    public void setOccupants(HashSet<DisasterVictim> occupants) {
        this.occupants = occupants;
    }

    public HashSet<Supply> getSupplies() {
        return supplies;
    }

    public void setSupplies(HashSet<Supply> supplies) {
        this.supplies = supplies;
    }

    public void addOccupant(DisasterVictim occupant) {
        this.occupants.add(occupant);
    }

    public void removeOccupant(DisasterVictim occupant) {
        this.occupants.remove(occupant);
    }

    /**
     * Adds a new supply to the collection.
     * <p>
     * If the supplied quantity is less than or equal to zero, an
     * IllegalArgumentException is thrown.
     * </p>
     * <p>
     * If a supply with the same description already exists in the collection, its
     * quantity is incremented
     * by the quantity of the new supply.
     * </p>
     * <p>
     * If the supply does not already exist in the collection, it is added to the
     * collection.
     * </p>
     *
     * @param newSupply The supply to add to the collection.
     * @throws IllegalArgumentException If the quantity of the new supply is less
     *                                  than or equal to zero.
     */
    public void addSupply(Supply newSupply) {
        if (newSupply.getQuantity() <= 0) {
            // Error check: Quantity must be positive
            throw new IllegalArgumentException("Quantity must be positive");
        }

        // Check if the supply already exists in the HashSet
        for (Supply existingSupply : supplies) {
            if (existingSupply.getDescription().equalsIgnoreCase(newSupply.getDescription())) {
                // Supply already exists, increment its quantity
                existingSupply.setQuantity(existingSupply.getQuantity() + newSupply.getQuantity());
                return; // Exit the method after incrementing quantity
            }
        }
        // If the supply does not exist, add it to the HashSet
        this.supplies.add(newSupply);
    }

    public void removeSupply(Supply supply) {
        // used for removing a supply without transferring it to a victim, such as
        // when de-listing it.
        this.supplies.remove(supply);
    }
}
