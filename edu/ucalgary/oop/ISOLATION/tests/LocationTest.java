/*
Copyright Ann Barcomb and Khawla Shnaikat, 2024
Licensed under GPL v3
See LICENSE.txt for more information.
*/
package edu.ucalgary.oop.tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;

public class LocationTest {
    private Location location;
    private DisasterVictim victim;
    private Supply supply;

    @Before
    public void setUp() {
        location = new Location("Shelter A", "1234 Shelter Ave");
        victim = new DisasterVictim("John Doe", "2024-01-01");
        supply = new Supply("Water Bottle", 10);
    }

    @Test // Testing object creation
    public void testConstructor() {
        assertNotNull("Constructor should create a non-null Location object", location);
        assertEquals("Constructor should set the name correctly", "Shelter A", location.getName());
        assertEquals("Constructor should set the address correctly", "1234 Shelter Ave", location.getAddress());
    }

    @Test // Testing setting shelter name
    public void testSetName() {
        String newName = "Shelter B";
        location.setName(newName);
        assertEquals("setName should update the name of the location", newName, location.getName());
    }

    @Test // Testing setting shelter address
    public void testSetAddress() {
        String newAddress = "4321 Shelter Blvd";
        location.setAddress(newAddress);
        assertEquals("setAddress should update the address of the location", newAddress, location.getAddress());
    }

    @Test // Testing adding shelter occupants
    public void testAddOccupant() {
        location.addOccupant(victim);
        assertTrue("Disaster victim should be added to the occupants list",
                location.getOccupants().contains(victim));
    }

    @Test // Testing removing shelter occupants
    public void testRemoveOccupant() {
        location.addOccupant(victim);
        location.removeOccupant(victim);
        assertFalse("Disaster victim should be removed from the occupants list",
                location.getOccupants().contains(victim));
    }

    @Test // Testing setting and getting shelter occupants
    public void testSetAndGetOccupants() {
        HashSet<DisasterVictim> newOccupants = new HashSet<>();
        newOccupants.add(victim);
        location.setOccupants(newOccupants);
        assertTrue("setOccupants should replace the occupants list with the new list",
                location.getOccupants().containsAll(newOccupants));
    }

    @Test // Testing adding supplies to the shelter object
    public void testAddSupply() {
        location.addSupply(supply);
        assertTrue("Supply should be added to the supplies list",
                location.getSupplies().contains(supply));
    }

    @Test // Testing removing supplies from the shelter object
    public void testRemoveSupply() {
        location.addSupply(supply);
        location.removeSupply(supply);
        assertFalse("Supply should be removed from the supplies list",
                location.getSupplies().contains(supply));
    }

    @Test // Testing setting and getting shelter supplies
    public void testSetAndGetSupplies() {
        HashSet<Supply> newSupplies = new HashSet<>();
        newSupplies.add(supply);
        location.setSupplies(newSupplies);
        assertTrue("setSupplies should replace the supplies list with the new list",
                location.getSupplies().containsAll(newSupplies));
    }
}
