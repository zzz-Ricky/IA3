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

public class DisasterVictimTest {
    private DisasterVictim victim;
    private HashSet<Supply> suppliesToSet;
    private String expectedFirstName = "Freda";
    private String EXPECTED_ENTRY_DATE = "2024-01-18";
    private String invalidDate = "15/13/2024";
    private String expectedComments = "Needs medical attention and speaks 2 languages";

    @Before
    public void setUp() {
        victim = new DisasterVictim(expectedFirstName, EXPECTED_ENTRY_DATE);
        suppliesToSet = new HashSet<>();
        suppliesToSet.add(new Supply("Water Bottle", 10));
        suppliesToSet.add(new Supply("Blanket", 5));
    }

    @Test // Testing constructor with a valid entry date
    public void testConstructorWithValidEntryDate() {
        String validEntryDate = "2024-01-18";
        DisasterVictim victim = new DisasterVictim("Freda", validEntryDate);
        assertNotNull("Constructor should successfully create an instance with a valid entry date",
                victim);
        assertEquals("Constructor should set the entry date correctly", validEntryDate,
                victim.getEntryDate());
    }

    @Test(expected = IllegalArgumentException.class)
    // Testing constructor with an invalid entry date format
    public void testConstructorWithInvalidEntryDateFormat() {
        String invalidEntryDate = "18/01/2024";
        new DisasterVictim("Freda", invalidEntryDate);
        // Expecting IllegalArgumentException due to invalid date format
    }

    @Test // Testing the method to set date of birth
    public void testSetDateOfBirth() {
        String newDateOfBirth = "1987-05-21";
        victim.setDateOfBirth(newDateOfBirth);
        // Verify that the date of birth is correctly updated
        assertEquals("setDateOfBirth should correctly update the date of birth",
                newDateOfBirth, victim.getDateOfBirth_Age());
    }

    @Test // Testing the method to set approximate age
    public void testSetApproxAge() {
        String newApproxAge = "36";
        victim.setApproxAge(newApproxAge);
        // Verify that the approximate age is correctly updated
        assertEquals("setApproxAge should correctly update the approximate age",
                newApproxAge, victim.getDateOfBirth_Age());
    }

    @Test(expected = IllegalArgumentException.class)
    // Testing setting date of birth with an invalid format
    public void testSetDateOfBirthWithInvalidFormat() {
        victim.setDateOfBirth(invalidDate); // This format should cause an exception
    }

    @Test // Testing setting and getting first name
    public void testSetAndGetFirstName() {
        String newFirstName = "Alice";
        victim.setFirstName(newFirstName);
        // Verify that the first name is correctly updated and retrieved
        assertEquals("setFirstName should update and getFirstName should return the new first name",
                newFirstName, victim.getFirstName());
    }

    @Test // Testing setting and getting last name
    public void testSetAndGetLastName() {
        String newLastName = "Smith";
        victim.setLastName(newLastName);
        // Verify that the last name is correctly updated and retrieved
        assertEquals("setLastName should update and getLastName should return the new last name",
                newLastName, victim.getLastName());
    }

    @Test // Testing getting comments
    public void testGetComments() {
        victim.setDescription(expectedComments);
        // Verify that the comments are correctly retrieved
        assertEquals("getComments should return the initial correct comments",
                expectedComments, victim.getDescription());
    }

    @Test // Testing setting comments
    public void testSetComments() {
        victim.setDescription(expectedComments);
        String newComments = "Has a minor injury on the left arm";
        victim.setDescription(newComments);
        // Verify that the comments are correctly updated
        assertEquals("setComments should update the comments correctly",
                newComments, victim.getDescription());
    }

    @Test // Testing getting assigned social ID
    public void testGetAssignedSocialID() {
        // Create new victims to test social ID assignment
        DisasterVictim newVictim = new DisasterVictim("Kash", "2024-01-21");
        int expectedSocialId = newVictim.getAssignedSocialID() + 1;
        DisasterVictim actualVictim = new DisasterVictim("Adeleke", "2024-01-22");
        // Verify that the assigned social ID is as expected
        assertEquals("getAssignedSocialID should return the expected social ID",
                expectedSocialId, actualVictim.getAssignedSocialID());
    }

    @Test // Testing getting entry date
    public void testGetEntryDate() {
        // Verify that the entry date is correctly retrieved
        assertEquals("getEntryDate should return the expected entry date",
                EXPECTED_ENTRY_DATE, victim.getEntryDate());
    }

    @Test // Testing setting and getting gender
    public void testSetAndGetGender() {
        String newGender = "male";
        victim.setGender(newGender);
        // Verify that the gender is correctly updated and retrieved
        assertEquals("setGender should update and getGender should return the new gender",
                newGender.toLowerCase(), victim.getGender());
    }

    @Test // Testing setting gender with an invalid option
    public void testSetWithInvalidGenderOption() {
        String newGender = "ab_cefg&*$^&*#df12134";
        victim.setGender(newGender);
        // Expecting IllegalArgumentException due to invalid date format
    }

    @Test // Testing adding a family connection
    public void testAddFamilyConnection() {
        // Create victims and a family relation for testing
        DisasterVictim victim1 = new DisasterVictim("Jane", "2024-01-20");
        DisasterVictim victim2 = new DisasterVictim("John", "2024-01-22");
        FamilyRelationManager familyManager = new FamilyRelationManager();
        FamilyRelation relation = new FamilyRelation(victim2, "parent", victim1, familyManager);
        HashSet<FamilyRelation> expectedRelations = new HashSet<>();
        expectedRelations.add(relation);
        victim2.setFamilyConnections(expectedRelations);
        HashSet<FamilyRelation> testFamily = victim2.getFamilyConnections();
        boolean correct = false;
        // Verify that the family connection is correctly added
        if (testFamily != null && testFamily.contains(relation)) {
            correct = true;
        }
        assertTrue("addFamilyConnection should add a family relationship", correct);
    }

    @Test // Testing adding a personal belonging
    public void testAddPersonalBelonging() {
    	Location testLocation = new Location("Shelter Z", "1234 Shelter Ave");
    	SupplyManager manager = new SupplyManager();
        Supply newSupply = new Supply("Emergency Kit", 1);
        testLocation.addSupply(newSupply);
        victim.addPersonalBelonging(newSupply, testLocation, manager);
        HashSet<Supply> testSupplies = victim.getPersonalBelongings();
        boolean correct = false;
        // Verify that the personal belonging is correctly added
        for (Supply supply : testSupplies) {
            if (supply.equals(newSupply)) {
                correct = true;
            }
        }
        assertTrue("addPersonalBelonging should add the supply to personal belongings",
                correct);
    }

    @Test // Testing removing a family connection
    public void testRemoveFamilyConnection() {
        // Create victims and family relations for testing
        DisasterVictim victim1 = new DisasterVictim("Jane", "2024-01-20");
        DisasterVictim victim2 = new DisasterVictim("John", "2024-01-22");
        FamilyRelationManager familyManager = new FamilyRelationManager();
        FamilyRelation relation1 = new FamilyRelation(victim, "sibling", victim1, familyManager);
        FamilyRelation relation2 = new FamilyRelation(victim, "sibling", victim2, familyManager);
        HashSet<FamilyRelation> expectedRelations = new HashSet<>();
        expectedRelations.add(relation2);
        HashSet<FamilyRelation> originalRelations = new HashSet<>();
        originalRelations.add(relation1);
        originalRelations.add(relation2);
        victim.setFamilyConnections(originalRelations);
        victim.addFamilyConnection(relation1, familyManager);
        victim.addFamilyConnection(relation2, familyManager);
        victim.removeFamilyConnection(relation1);
        HashSet<FamilyRelation> testFamily = victim.getFamilyConnections();
        boolean correct = true;
        // Verify that the family connection is correctly removed
        if (testFamily.contains(relation1)) {
            correct = false;
        }
        assertTrue("removeFamilyConnection should remove the family member", correct);
    }

    @Test // Testing removing a personal belonging
    public void testRemovePersonalBelonging() {
    	Location testLocation = new Location("Shelter Z", "1234 Shelter Ave");
    	SupplyManager manager = new SupplyManager();
        Supply supplyToRemove = new Supply("Emergency Kit", 1);
        testLocation.addSupply(supplyToRemove);
        victim.addPersonalBelonging(supplyToRemove, testLocation, manager);
        victim.removePersonalBelonging(supplyToRemove);
        HashSet<Supply> testSupplies = victim.getPersonalBelongings();
        boolean correct = true;
        // Verify that the personal belonging is correctly removed
        for (Supply supply : testSupplies) {
            if (supply.equals(supplyToRemove)) {
                correct = false;
            }
        }
        assertTrue("removePersonalBelonging should remove the supply from personal belongings",
                correct);
    }

    @Test // Testing setting family connections
    public void testSetFamilyConnection() {
    	FamilyRelationManager familyManager = new FamilyRelationManager();
        DisasterVictim victim1 = new DisasterVictim("Jane", "2024-01-20");
        DisasterVictim victim2 = new DisasterVictim("John", "2024-01-22");
        FamilyRelation relation = new FamilyRelation(victim1, "sibling", victim2, familyManager);
        HashSet<FamilyRelation> expectedRelations = new HashSet<>();
        expectedRelations.add(relation);
        victim1.setFamilyConnections(expectedRelations);
        boolean correct = true;
        HashSet<FamilyRelation> actualRecords = victim1.getFamilyConnections();
        if (expectedRelations.size() != actualRecords.size()) {
            correct = false;
        } else {
            // Convert sets to arrays for comparison
            FamilyRelation[] expectedArray = expectedRelations.toArray(new FamilyRelation[0]);
            FamilyRelation[] actualArray = actualRecords.toArray(new FamilyRelation[0]);
            // Check each element for equality
            for (int i = 0; i < actualArray.length; i++) {
                if (!expectedRelations.contains(expectedArray[i])) {
                    correct = false;
                    break;
                }
            }
        }
        assertTrue("Family relation should be set", correct);
    }

    @Test // Testing setting medical records
    public void testSetMedicalRecords() {
        Location testLocation = new Location("Shelter Z", "1234 Shelter Ave");
        MedicalRecord testRecord = new MedicalRecord(testLocation, "test for strep",
                "2024-02-09");
        boolean correct = true;
        ArrayList<MedicalRecord> newRecords = new ArrayList<>();
        newRecords.add(testRecord);
        victim.setMedicalRecords(newRecords);
        ArrayList<MedicalRecord> actualRecords = victim.getMedicalRecords();
        // Verify that the medical records are correctly updated
        if (newRecords.size() != actualRecords.size()) {
            correct = false;
        } else {
            for (int i = 0; i < newRecords.size(); i++) {
                if (!actualRecords.get(i).equals(newRecords.get(i))) {
                    correct = false;
                    break;
                }
            }
        }
        assertTrue("setMedicalRecords should correctly update medical records",
                correct);
    }

    @Test // Testing setting personal belongings
    public void testSetPersonalBelongings() {
        Supply one = new Supply("Tent", 1);
        Supply two = new Supply("Jug", 3);
        HashSet<Supply> newSupplies = new HashSet<>();
        newSupplies.add(one);
        newSupplies.add(two);
        boolean correct = true;
        victim.setPersonalBelongings(newSupplies);
        HashSet<Supply> actualSupplies = victim.getPersonalBelongings();
        // Verify that the personal belongings are correctly updated
        if (!newSupplies.equals(actualSupplies)) {
            correct = false;
        }
        assertTrue("setPersonalBelongings should correctly update personal belongings",
                correct);
    }

    @Test // Testing getting dietary preferences
    public void testGetDietaryPreferences() {
        victim.addDietaryPreference(DietaryRestrictions.DietaryRestriction.DBML);
        victim.addDietaryPreference(DietaryRestrictions.DietaryRestriction.GFML);
        ArrayList<DietaryRestrictions> preferences = victim.getDietaryPreference();
        assertNotNull("getDietaryPreference should not return null", preferences);

        // Verify that the returned ArrayList contains the added dietary preferences
        assertTrue("getDietaryPreference should return the added dietary preferences",
                preferences.stream().anyMatch(d -> d.getRestriction() == DietaryRestrictions.DietaryRestriction.DBML));
        assertTrue("getDietaryPreference should return the added dietary preferences",
                preferences.stream().anyMatch(d -> d.getRestriction() == DietaryRestrictions.DietaryRestriction.GFML));
    }

    @Test // Testing adding a dietary preference
    public void testAddDietaryPreference() {
        victim.addDietaryPreference(DietaryRestrictions.DietaryRestriction.AVML);
        // Verify that the dietary preference is correctly added
        assertTrue("addDietaryPreference should add a dietary preference",
                victim.getDietaryPreference().stream().anyMatch(d -> d.getRestriction() == DietaryRestrictions.DietaryRestriction.AVML));
    }

    @Test // Testing removing a dietary preference
    public void testRemoveDietaryPreference() {
        victim.addDietaryPreference(DietaryRestrictions.DietaryRestriction.AVML);
        victim.removeDietaryPreference(DietaryRestrictions.DietaryRestriction.AVML);
        // Verify that the dietary preference is correctly removed
        assertFalse("removeDietaryPreference should remove a dietary preference",
                victim.getDietaryPreference().stream().anyMatch(d -> d.getRestriction() == DietaryRestrictions.DietaryRestriction.AVML));
    }


}
