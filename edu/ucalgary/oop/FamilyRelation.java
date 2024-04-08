/**
 * The DisasterVictim class represents a relationship between two People
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The DisasterVictim class stores information relating to the members of a
 * given relationship and the nature of the relationship listed.
 * </p>
 * <p>
 * The class provides methods to manage and retrieve information about the
 * members of the relationship and its description.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

public class FamilyRelation {
    private DisasterVictim personOne;
    private String relationshipTo;
    private DisasterVictim personTwo;
    
    /**
     * Constructs a new FamilyRelation object with the specified parameters and adds it to the
     * FamilyRelationManager.
     *
     * @param personOne The first person in the relationship.
     * @param relationshipTo The type of relationship between the two persons.
     * @param personTwo The second person in the relationship.
     * @param manager The FamilyRelationManager responsible for managing family relations.
     */
    public FamilyRelation(DisasterVictim personOne, String relationshipTo, DisasterVictim personTwo,
            FamilyRelationManager manager) {
        // implicitly, we want to define a FamilyRelationManager class in main before
        // running this. Thus, we force new relations to be created using it.
        this.personOne = personOne;
        this.relationshipTo = relationshipTo;
        this.personTwo = personTwo;
        manager.addRelationship(this);
    }

    public DisasterVictim getPersonOne() {
        return personOne;
    }

    public void setPersonOne(DisasterVictim personOne, FamilyRelationManager manager) {
        try {
            this.personOne.removeFamilyConnection(this);
        } catch (Exception e) {
            System.out.println("Something went wrong with de-linking person 1.");
        } finally {
            this.personOne = personOne;
            personOne.addFamilyConnection(this, manager);
            manager.validateRelationship(this);
        }
    }

    public String getRelationshipTo() {
        return relationshipTo;
    }

    public void setRelationshipTo(String relationshipTo) {
        this.relationshipTo = relationshipTo;
    }

    public DisasterVictim getPersonTwo() {
        return personTwo;
    }

    public void setPersonTwo(DisasterVictim personTwo, FamilyRelationManager manager) {
        try {
            this.personTwo.removeFamilyConnection(this);
        } catch (Exception e) {
            System.out.println("Something went wrong with de-linking person 2.");
        } finally {
            this.personTwo = personTwo;
            personTwo.addFamilyConnection(this, manager);
            manager.validateRelationship(this);
        }
    }
}
