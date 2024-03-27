package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.tools.JavaFileManager.Location;

public class InquiryTest {
    private Inquiry inquiry;
    private DisasterVictim missingPerson;
    private Location lastKnownLocation;
    private String validDate = "2024-02-10";
    private String invalidDate = "2024/02/10";
    private String expectedInfoProvided = "Looking for family member";

    @Before
    public void setUp() {
        // Initializing test objects before each test method
        missingPerson = new DisasterVictim("Jane", "2024-01-25");
        lastKnownLocation = new Location("University of Calgary", "2500 University Dr NW");
        inquiry = new Inquiry(missingPerson, validDate, expectedInfoProvided, lastKnownLocation);
    }

    @Test // Testing object creation
    public void testObjectCreation() {
        assertNotNull("Inquiry object should not be null", inquiry);
    }

    @Test // Testing getting the missingPerson object attribute
    public void testGetMissingPerson() {
        assertEquals("getMissingPerson should return the correct Person",
                missingPerson, inquiry.getMissingPerson());
    }

    @Test // Testing getting the lastKnownLocation object attribute
    public void testGetLastKnownLocation() {
        assertEquals("gettLastKnownLocation should return the correct Location",
                lastKnownLocation, inquiry.getLastKnownLocation());
    }

    @Test // Testing getting the description attribute
    public void testGetDescription() {
        assertEquals("getDescription should return the correct description",
                expectedInfoProvided, inquiry.getDescription());
    }

    @Test // Testing getting the dateOfInquiry attribute
    public void testGetDateOfInquiry() {
        assertEquals("getDate should return the correct date of inquiry",
                validDate, inquiry.getDate());
    }

    @Test // Testing setting the missingPerson of the inquiry
    public void testSetMissingPerson() {
        DisasterVictim newExpectedPerson = new DisasterVictim("Tom", "2024-02-09");
        inquiry.setMissingPerson(newExpectedPerson);
        assertEquals("setMissingPerson should update the missingPerson",
                newExpectedPerson.getName(), inquiry.getMissingPerson().getName());
    }

    @Test // Testing setting the lastKnownLocation of the inquiry
    public void testSetLastKnownLocation() {
        Location newExpectedLocation = new Location("Shelter B", "150 8 Ave NW ");
        inquiry.setLastKnownLocation(newExpectedLocation);
        assertEquals("setLastKnownLocation should update the lastKnownLocation",
                newExpectedLocation.getName(), inquiry.getLastKnownLocation().getName());
    }

    @Test // Testing setting the description of the inquiry
    public void testSetDescription() {
        String newInfoProvided = "Looking for their child";
        inquiry.setDescription(newInfoProvided);
        assertEquals("setDescription should update the description", newInfoProvided,
                inquiry.getDescription());
    }

    @Test // Testing setting the dateOfInquiry of the inquiry
    public void testSetDateOfInquiry() {
        String newExpectedDateOfInquiry = "2024-02-05";
        inquiry.setDate(newExpectedDateOfInquiry);
        assertEquals("setDate should update date of inquiry",
                newExpectedDateOfInquiry, inquiry.getDate());
    }

    @Test // Testing setting date of inquiry with an invalid format
    public void testSetDateOfInquiryWithInvalidFormat() {
        boolean correctValue = false;
        String failureReason = "no exception was thrown";

        try {
            inquiry.setDate(invalidDate); // Should throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            correctValue = true;
        } catch (Exception e) {
            failureReason = "the wrong type of exception was thrown";
        }

        String message = "setDate() should throw an IllegalArgumentException with invalid date format '"
                + invalidDate + "' but " + failureReason + ".";
        assertTrue(message, correctValue);
    }
}
