package edu.ucalgary.oop;

import java.util.HashSet;

public class SupplyManager {
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
