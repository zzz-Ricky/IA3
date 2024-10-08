/**
 * The SupplyManager class manages the transfer and allocation of supplies to disaster victims.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The SupplyManager class provides methods to check the availability of supplies,
 * transfer supplies to disaster victims, and allocate supplies from a location.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 2024-04-07
 */

package edu.ucalgary.oop;

import java.util.HashSet;

public class SupplyManager {

    /**
     * Checks the availability of a supply in a location.
     *
     * @param locationSupply The set of supplies available at the location.
     * @param supply         The supply to be checked for availability.
     * @return True if the supply is available in sufficient quantity, false
     *         otherwise.
     * @throws IllegalArgumentException If the supply does not exist in the location
     *                                  or is insufficient.
     */
    private boolean checkSupplyAvailability(HashSet<Supply> locationSupply, Supply supply) {
        String supplyName = supply.getDescription();
        for (Supply item : locationSupply) {
            if (item.getDescription().equals(supplyName)) {
                if (item.getQuantity() >= supply.getQuantity()) {
                    return true;
                } else {
                    throw new IllegalArgumentException(
                            "Not enough " + supplyName + " in the location (Needed "
                                    + supply.getQuantity() + " but only have "
                                    + item.getQuantity());
                }
            }
        }
        throw new IllegalArgumentException("A supply must exist in the location it is taken from");
    }

    /**
     * Transfers supplies from a location to a disaster victim.
     *
     * @param person   The disaster victim receiving the supplies.
     * @param supplies The set of supplies to be transferred.
     * @param location The location from which supplies are transferred.
     */
    public void transferSupplies(DisasterVictim person,
            HashSet<Supply> supplies, Location location) {

        HashSet<Supply> localSupplies = location.getSupplies();
        HashSet<Supply> victimSupplies = person.getPersonalBelongings();
        for (Supply item : supplies) {
            if (checkSupplyAvailability(localSupplies, item)) {
                for (Supply localSupply : localSupplies) {
                    if (localSupply.getDescription().equals(item.getDescription())) {
                        localSupply.setQuantity(localSupply.getQuantity() - item.getQuantity());
                        break;
                    }
                }
            }
        }
        location.setSupplies(localSupplies);
        victimSupplies.addAll(supplies);
        person.setPersonalBelongings(victimSupplies);
    }

    /**
     * Allocates a supply to a disaster victim from a location.
     *
     * @param person   The disaster victim receiving the supply.
     * @param supply   The supply to be allocated.
     * @param location The location from which the supply is allocated.
     */
    public void giveSupply(DisasterVictim person, Supply supply, Location location) {
        HashSet<Supply> localSupplies = location.getSupplies();
        if (checkSupplyAvailability(localSupplies, supply)) {
            for (Supply supplyItem : localSupplies) {
                if (supplyItem.getDescription().equals(supply.getDescription())) {
                    supplyItem.setQuantity(supplyItem.getQuantity() - supply.getQuantity());
                    if (supplyItem.getQuantity() == 0) {
                        location.removeSupply(supplyItem);
                    }

                    HashSet<Supply> personSupplies = person.getPersonalBelongings();
                    for (Supply supplyItem2 : personSupplies) {
                        if (supplyItem2.getDescription().equals(supply.getDescription())) {
                            supplyItem2.setQuantity(supplyItem2.getQuantity() + supply.getQuantity());
                            personSupplies.add(supplyItem2); // Corrected line
                            return; // Optimization: Break out of the loop once supply is updated
                        }
                    }
                    // If the supply is not found in person's belongings, add it
                    personSupplies.add(new Supply(supply.getDescription(), supply.getQuantity()));
                    return; // Optimization: Break out of the loop after adding supply
                }
            }
        }
    }
}
