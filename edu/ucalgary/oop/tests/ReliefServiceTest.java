package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class ReliefServiceTest {
    private ReliefService reliefService;
    private Inquirer inquirer;
    private DisasterVictim missingPerson;
    private Location lastKnownLocation;
    private String validDate = "2024-02-10";
    private String invalidDate = "2024/02/10";
    private String expectedInfoProvided = "Looking for family member";
    private Inquiry inquiry;
    private ArrayList<Inquiry> expectedInquiries;

    @Before
    public void setUp() {
        inquirer = new Inquirer("John", "Alex", "1234567890");
        missingPerson = new DisasterVictim("Jane", "2024-01-25");
        lastKnownLocation = new Location("University of Calgary", "2500 University Dr NW");
        inquiry = new Inquiry(missingPerson, validDate, expectedInfoProvided, lastKnownLocation);
        expectedInquiries = new ArrayList<>();
        expectedInquiries.add(inquiry);
        reliefService = new ReliefService(inquirer, expectedInquiries);
    }

    @Test // Testing object creation
    public void testObjectCreation() {
        assertNotNull("ReliefService object should not be null", reliefService);
    }

    @Test // Testing getting the inquirer
    public void testGetInquirer() {
        assertEquals("Inquirer should match the one set in setup", inquirer, reliefService.getInquirer());
    }

    @Test // Testing getting the inquiries
    public void testGetInquiries() {
        assertEquals("Missing person inquiries should match the one set in setup", expectedInquiries,
                reliefService.getInquiries());
    }

    @Test // Testing adding a new inquiry
    public void testAddInquiry() {
        DisasterVictim newMissingPerson = new DisasterVictim("Bob", "2024-02-11");
        String newValidDate = "2024-02-20";
        String newExpectedInfoProvided = "Looking for a Friend";
        Location newLastKnownLocation = new Location("University of Calgary", "2500 University Dr NW");
        Inquiry newInquiry = new Inquiry(newMissingPerson, newValidDate, newExpectedInfoProvided, newLastKnownLocation);

        reliefService.addInquiry(newInquiry);
        assertTrue("addInquiry should add the inquiry to the array list",
                reliefService.getInquiries().contains(newInquiry));
    }

    @Test // Testing removing an inquiry
    public void testRemoveInquiry() {
        DisasterVictim newMissingPerson = new DisasterVictim("Bob", "2024-02-11");
        String newValidDate = "2024-02-20";
        String newExpectedInfoProvided = "Looking for a Friend";
        Location newLastKnownLocation = new Location("University of Calgary", "2500 University Dr NW");
        Inquiry inquiryToRemove = new Inquiry(newMissingPerson, newValidDate, newExpectedInfoProvided,
                newLastKnownLocation);
    
        int initialSize = reliefService.getInquiries().size();
    
        reliefService.addInquiry(inquiryToRemove);
        reliefService.removeInquiry(inquiryToRemove);
    
        int finalSize = reliefService.getInquiries().size();
    
        assertEquals("removeInquiry should remove the inquiry from the array list", initialSize - 1, finalSize);
    }
