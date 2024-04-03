package edu.ucalgary.oop;

import java.util.HashSet;

import edu.ucalgary.oop.DisasterVictim;
import edu.ucalgary.oop.Location;

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

    public void giveSupply(DisasterVictim person,
            Supply supply, Location location) {
        HashSet<Supply> localSupplies = location.getSupplies();
        if (checkSupplyAvailability(localSupplies, supply)) {
            for (Supply localSupply : localSupplies) {
                if (localSupply.getDescription().equals(supply.getDescription())) {
                    localSupply.setQuantity(localSupply.getQuantity() - supply.getQuantity());
                    break;
                }
            }
        }
    }
}
