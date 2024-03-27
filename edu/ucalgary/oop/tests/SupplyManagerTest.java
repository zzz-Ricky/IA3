package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;

public class SupplyManagerTest {
    private DisasterVictim missingPerson;
    private Location location;
    private HashSet<Supply> supplies;
    private Supply supplyA;
    private Supply supplyB;
    private Supply supplyC;

    @Before
    public void setUp() {
        missingPerson = new DisasterVictim("Jane", "2024-01-25");
        supplyA = new Supply("Band Aids", 10);
        supplyB = new Supply("Water Bottles", 6);
        supplyC = new Supply("Hand Sanitizer", 2);
        supplies = new HashSet<>();
        supplies.add(supplyA);
        supplies.add(supplyB);
        supplies.add(supplyC);
        location = new Location("University of Calgary", "2500 University Dr NW");
        location.setSupplies(supplies);
    }

    @Test // Testing transferring a single supply from a location to a missingPerson
    public void testGiveSupply() {
        int initialQuantityAtA = supplyA.getQuantity();
        int supplyAQuantityReceived = 0;
        SupplyManager.giveSupply(missingPerson, supplyA, location);
        assertTrue("PersonalBelongings of missing person should contain the item given from location",
                missingPerson.getPersonalBelongings().contains(supplyA));
        assertEquals("Quantity of supplyA at the location should decrease by 1 after giving to missing person",
                initialQuantityAtA - 1, supplyA.getQuantity());

        for (Supply supply : missingPerson.getPersonalBelongings()) {
            if (supply.getDescription().equals(supplyA.getDescription())) {
                supplyAQuantityReceived = supply.getQuantity();
                break;
            }
        }
        assertEquals("Quantity of supplyA received by missing person should be 1",
                1, supplyAQuantityReceived);
    }

    @Test // Testing transferring a specified hashset of supplies from a location to a
          // missingPerson
    public void testTransferSupplies() {
        SupplyManager.transferSupplies(missingPerson, supplies, location);
        assertEquals("PersonalBelongings of missing person should match the one set in setup",
                supplies, missingPerson.getPersonalBelongings());
    }
}
