package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;

public class FamilyRelationManagerTest {
    private DisasterVictim personOne = new DisasterVictim("John Dalan", "2024-01-19");
    private DisasterVictim personTwo = new DisasterVictim("Jane Dalan", "2024-02-20");
    private String relationshipTo = "sibling";
    private FamilyRelation testFamilyRelationObject = new FamilyRelation(personOne,
            relationshipTo, personTwo);
    private FamilyRelationManager testFamilyRelationManager = new FamilyRelationManager();

    @Test // Testing the creation of the FamilyRelationManager object
    public void testObjectCreation() {
        testFamilyRelationManager.addRelationship(testFamilyRelationObject);
        assertNotNull(testFamilyRelationManager);
    }

    @Test // Testing setting and getting personOne in the FamilyRelation
    public void testSetAndGetPersonOne() {
        DisasterVictim newPersonOne = new DisasterVictim("New Person", "2024-03-21");
        testFamilyRelationManager.setPersonOne(testFamilyRelationObject, newPersonOne);
        assertEquals("setPersonOne should update personOne", newPersonOne,
                testFamilyRelationObject.getPersonOne());
    }

    @Test // Testing setting and getting personTwo in the FamilyRelation
    public void testSetAndGetPersonTwo() {
        DisasterVictim newPersonTwo = new DisasterVictim("Another Person", "2024-04-22");
        testFamilyRelationManager.setPersonTwo(testFamilyRelationObject, newPersonTwo);
        assertEquals("setPersonTwo should update personTwo", newPersonTwo,
                testFamilyRelationObject.getPersonTwo());
    }

    @Test // Testing setting and getting relationshipTo in the FamilyRelation
    public void testSetAndGetRelationshipTo() {
        String newRelationship = "parent";
        testFamilyRelationManager.setRelationshipTo(testFamilyRelationObject, newRelationship);
        assertEquals("setRelationshipTo should update the relationship", newRelationship,
                testFamilyRelationObject.getRelationshipTo());
    }

    @Test // Testing removing a relationship from the FamilyRelationManager
    public void testRemoveAndGetRelationships() {
        DisasterVictim newPersonOne = new DisasterVictim("Bob Ross", "2024-01-18");
        DisasterVictim newPersonTwo = new DisasterVictim("Tree Little", "2024-02-10");
        String relationshipTo = "friends";
        FamilyRelation newFamilyRelationObject = new FamilyRelation(newPersonOne,
                relationshipTo, newPersonTwo);

        // Adding a new relationship, removing it, and checking if it's removed
        testFamilyRelationManager.addRelationship(newFamilyRelationObject);
        testFamilyRelationManager.removeRelationship(newFamilyRelationObject);

        HashSet<FamilyRelation> relationshipRelations = testFamilyRelationManager.getRelationships();
        boolean correct = true;

        for (FamilyRelation relation : relationshipRelations) {
            if (relation.equals(newFamilyRelationObject)) {
                correct = false;
                break;
            }
        }
        assertTrue("removeRelationship should remove the relation from relationshipRelations",
                correct);
    }

    @Test(expected = IllegalArgumentException.class)\
    // Testing adding an invalid relationship (duplicate) 
    public void testAddInvalidRelationship() {
        DisasterVictim newPersonOne = new DisasterVictim("Bob Ross", "2024-01-18");
        DisasterVictim newPersonTwo = new DisasterVictim("Tree Little", "2024-02-10");
        String relationshipTo = "friends";
        FamilyRelation newFamilyRelationObject = new FamilyRelation(newPersonOne,
                relationshipTo, newPersonTwo);

        try {
        // Adding the same relationship twice should ideally throw an IllegalArgumentException
        testFamilyRelationManager.addRelationship(newFamilyRelationObject);
        testFamilyRelationManager.addRelationship(newFamilyRelationObject);
        } catch (IllegalArgumentException e) {
            correctValue = true;
        } catch (Exception e) {
            failureReason = "the wrong type of exception was thrown";
        }
        String message = "addRelationship() should throw an IllegalArgumentException with duplicate entries but " 
        + failureReason + ".";
        assertTrue(message, correctValue);
    }
}
