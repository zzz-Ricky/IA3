/*
Copyright Ann Barcomb and Khawla Shnaikat, 2024
Licensed under GPL v3
See LICENSE.txt for more information.
*/
package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class SupplyTest {
    String expectedType = "Blanket";
    int expectedQuantity = 5;
    private Supply supply = new Supply(expectedType, expectedQuantity);

    @Test // Testing object creation
    public void testObjectCreation() {
        assertNotNull(supply);
    }

    @Test // Testing getting the type of the supply
    public void testGetType() {
        assertEquals("getType should return the correct type", expectedType, supply.getDescription());
    }

    @Test // Testing setting the type of the supply
    public void testSetType() {
        supply.setType("Food");
        assertEquals("setType should update the type", "Food", supply.getDescription());
    }

    @Test // Testing getting the quantity of the supply
    public void testGetQuantity() {
        assertEquals("getQuantity should return the correct quantity", expectedQuantity, supply.getQuantity());
    }

    @Test // Testing setting the quantity of the supply
    public void testSetQuantity() {
        supply.setQuantity(20);
        assertEquals("setQuantity should update the quantity", 20, supply.getQuantity());
    }
}
