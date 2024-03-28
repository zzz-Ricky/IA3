package edu.ucalgary.oop.Classes;

import java.util.ArrayList;
import java.util.HashSet;

import edu.ucalgary.oop.DisasterVictim;

public class FamilyRelationManager {

    private static HashSet<FamilyRelation> relationshipRecord;

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
                                + personTwo.getFirstName();
            }
        }
        return true;
    }

    public boolean checkInRelationship(FamilyRelation relationship, DisasterVictim person) {
        if ((person.equals(relationship.getPersonOne()) ||
                (person.equals(relationship.getPersonTwo())))) {
            return true;
        } else {
            return false;
        }
    }

    public void addRelationship(FamilyRelation relationship) {
        validateRelationship(relationship);
        relationshipRecord.add(relationship);
    }

    public void removeRelationship(FamilyRelation relationship) {
        relationshipRecord.remove(relationship);
    }

    public HashSet<FamilyRelation> getRelationships(DisasterVictim person){
        HashSet<FamilyRelation> personalRelations = new HashSet<FamilyRelation>;
        for (FamilyRelation relation : relationshipRecord) {
            DisasterVictim personOne = relation.getPersonOne();
            DisasterVictim personTwo = relation.getPersonTwo();
            if (personOne.equals(person)){
                personalRelations.add(relation);
            }
            if (personTwo.equals(person)){
                personalRelations.add(relation);
            }
        return personalRelations;
    }
    }
}
