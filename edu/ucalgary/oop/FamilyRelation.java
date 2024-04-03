package edu.ucalgary.oop;

public class FamilyRelation {
    private DisasterVictim personOne;
    private String relationshipTo;
    private DisasterVictim personTwo;

    public FamilyRelation(DisasterVictim personOne, String relationshipTo, DisasterVictim personTwo,
            FamilyRelationManager manager) {
        // implicitly, we want to define a FamilyRelationManager class in main before
        // running this. Thus, we force new relations to be created using it.
        this.personOne = personOne;
        this.relationshipTo = relationshipTo;
        this.personTwo = personTwo;
        manager.validateRelationship(this);
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
