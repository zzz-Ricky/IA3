/*
Copyright Ann Barcomb and Khawla Shnaikat, 2024
Licensed under GPL v3
See LICENSE.txt for more information.
*/
package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class MedicalRecordTest {
    Location expectedLocation = new Location("ShelterA", "140 8 Ave NW ");
    private String expectedTreatmentDetails = "Broken arm treated";
    private String expectedDateOfTreatment = "2024-01-19";
    private String validDateOfTreatment = "2024-02-04";
    private String inValidDateOfTreatment = "2024/02/04";
    MedicalRecord medicalRecord = new MedicalRecord(expectedLocation, expectedTreatmentDetails,
            expectedDateOfTreatment);

    @Test // Testing object creation
    public void testObjectCreation() {
        assertNotNull(medicalRecord);
    }

    @Test // Testing getting Location
    public void testGetLocation() {
        assertEquals("getLocation should return the correct Location", expectedLocation, medicalRecord.getLocation());
    }

    @Test // Testing setting Location
    public void testSetLocation() {
        Location newExpectedLocation = new Location("Shelter B", "150 8 Ave NW ");
        medicalRecord.setLocation(newExpectedLocation);
        assertEquals("setLocation should update the Location", newExpectedLocation.getName(),
                medicalRecord.getLocation().getName());
    }

    @Test // Testing getting description attribute
    public void testGetTreatmentDetails() {
        assertEquals("getDescription should return the correct treatment details", expectedTreatmentDetails,
                medicalRecord.getDescription());
    }

    @Test // Testing setting description attribute
    public void testSetTreatmentDetails() {
        String newExpectedTreatment = "No surgery required";
        medicalRecord.setDescription(newExpectedTreatment);
        assertEquals("setDescription should update the treatment details", newExpectedTreatment,
                medicalRecord.getDescription());
    }

    @Test // Testing getting date of treatment
    public void testGetDateOfTreatment() {
        assertEquals("getDate should return the correct date of treatment", expectedDateOfTreatment,
                medicalRecord.getDate());
    }

    @Test // Testing setting date of treatment
    public void testSetDateOfTreatment() {
        String newExpectedDateOfTreatment = "2024-02-05";
        medicalRecord.setDate(newExpectedDateOfTreatment);
        assertEquals("setDate should update date of treatment", newExpectedDateOfTreatment, medicalRecord.getDate());
    }

    @Test // Testing setting date of treatment with an invalid format
    public void testSetDateOfTreatmentWithInvalidFormat() {
        boolean correctValue = false;
        String failureReason = "no exception was thrown";

        try {
            medicalRecord.setDate(inValidDateOfTreatment); // Should throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            correctValue = true;
        } catch (Exception e) {
            failureReason = "the wrong type of exception was thrown";
        }

        String message = "setDate() should throw an IllegalArgumentException with invalid date format '"
                + inValidDateOfTreatment + "' but " + failureReason + ".";
        assertTrue(message, correctValue);
    }

}
