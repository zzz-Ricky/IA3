/*
Copyright Ann Barcomb and Khawla Shnaikat, 2024
Licensed under GPL v3
See LICENSE.txt for more information.
*/
package edu.ucalgary.oop.tests;

import org.junit.*;
import static org.junit.Assert.*;

public class InquirerTest {
    private String expectedFirstName = "Joseph";
    private String expectedLastName = "Bouillon";
    private String expectedPhoneNumber = "+1-123-456-7890";
    private Inquirer inquirer = new Inquirer(expectedFirstName, expectedLastName, expectedPhoneNumber);

    @Test // Testing object creation
    public void testObjectCreation() {
        assertNotNull(inquirer);
    }

    @Test // Testing getting the first name of the inquirer
    public void testGetFirstName() {
        assertEquals("getFirstName() should return inquirer's first name", expectedFirstName, inquirer.getFirstName());
    }

    @Test // Testing getting the last name of the inquirer
    public void testGetLastName() {
        assertEquals("getLastName() should return inquirer's last name", expectedLastName, inquirer.getLastName());
    }

    @Test // Testing getting the services phone number of the inquirer
    public void testGetServicesPhoneNum() {
        assertEquals("getServicesPhone() should return the correct Services Number", expectedPhoneNumber,
                inquirer.getServicesPhone());
    }

    @Test // Testing setting the services phone number of the inquirer
    public void testSetServicesPhoneNum() {
        String newExpectedPhone = "+9-999-999-9999";
        inquirer.setServicesPhone(newExpectedPhone);
        // Check if setServicesPhone() correctly updates the phone number
        assertEquals("setServicesPhone should update the Phone Number", newExpectedPhone,
                inquirer.getServicesPhone());
    }
}
