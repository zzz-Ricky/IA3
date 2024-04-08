/**
 * The FamilyRelationManager class manages the transactions of adding new relationships between person derived objects
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The FamilyRelationManager class stores a registry of all existing family relations.
 * This is done in order to prevent relationship paradoxes and conflicts.
 * </p>
 * <p>
 * The class provides methods to manage and verify family relationship objects.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import java.util.ArrayList;
import java.util.HashSet;

import edu.ucalgary.oop.DisasterVictim;

public class FamilyRelationManager {

    private static HashSet<FamilyRelation> relationshipRecord;
    
    /**
     * Constructs a new FamilyRelationManager object.
     * Initializes the relationship record as an empty HashSet.
     */
    public FamilyRelationManager() {
        relationshipRecord = new HashSet<FamilyRelation>();
    }
    
    /**
     * Validates a new family relationship to prevent duplicates.
     *
     * @param relationship The family relationship to validate.
     * @return true if the relationship is valid and does not already exist, otherwise false.
     * @throws IllegalArgumentException if the relationship already exists in the record.
     */
    public boolean validateRelationship(FamilyRelation relationship) {
        // Check if the relationship already exists in the record, to prevent duplicates
        for (FamilyRelation relation : relationshipRecord) {
            DisasterVictim personOne = relation.getPersonOne();
            DisasterVictim personTwo = relation.getPersonTwo();
            // We check both combinations of person 1 and 2 to ensure if it already exists
            if ((personOne.equals(relationship.getPersonOne()) && personTwo.equals(relationship.getPersonTwo())) ||
                    (personOne.equals(relationship.getPersonTwo()) && personTwo.equals(relationship.getPersonOne()))) {
                throw new IllegalArgumentException(
                        "Relationship already exists between " + personOne.getFirstName() + " and "
                                + personTwo.getFirstName());
            }
        }
        return true;
    }
    
    /**
     * Checks if a person is involved in a given family relationship.
     *
     * @param relationship The family relationship to check.
     * @param person       The person to check.
     * @return true if the person is involved in the relationship, otherwise false.
     */
    public boolean checkInRelationship(FamilyRelation relationship, Person person) {
        if ((person.equals(relationship.getPersonOne()) ||
                (person.equals(relationship.getPersonTwo())))) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Adds a new family relationship to the manager.
     *
     * @param relationship The family relationship to add.
     */
    public void addRelationship(FamilyRelation relationship) {
        validateRelationship(relationship);
        relationshipRecord.add(relationship);
    }
    
    /**
     * Removes a family relationship from the manager.
     *
     * @param relationship The family relationship to remove.
     */
    public void removeRelationship(FamilyRelation relationship) {
        relationshipRecord.remove(relationship);
    }
    
    /**
     * Retrieves all family relationships involving a given person.
     *
     * @param person The person to retrieve relationships for.
     * @return A HashSet containing all family relationships involving the person.
     */
    public HashSet<FamilyRelation> getRelationships(DisasterVictim person) {
        HashSet<FamilyRelation> personalRelations = new HashSet<FamilyRelation>();
        for (FamilyRelation relation : relationshipRecord) {
            DisasterVictim personOne = relation.getPersonOne();
            DisasterVictim personTwo = relation.getPersonTwo();
            if (personOne.equals(person)) {
                personalRelations.add(relation);
            }
            if (personTwo.equals(person)) {
                personalRelations.add(relation);
            }
        }
        return personalRelations;
    }
    
    /**
     * Retrieves the entire set of family relationships managed by this manager.
     *
     * @return A HashSet containing all family relationships.
     */
    public HashSet<FamilyRelation> getRelationshipRecord(){
    	return relationshipRecord;
    }
}
