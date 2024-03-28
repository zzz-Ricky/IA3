package edu.ucalgary.oop;

import java.util.HashSet;
import java.util.ArrayList;

import edu.ucalgary.oop.DisasterVictim;
import edu.ucalgary.oop.FamilyRelation;
import edu.ucalgary.oop.Classes.FamilyRelationManager;

abstract class Person {
    private String firstName;
    private String lastName;
    private HashSet<FamilyRelation> familyConnections;
    private String genderPronoun;
    private ArrayList<String> genderOptions;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return genderPronoun;
    }

    public void setGender(String genderPronoun) {
        this.genderPronoun = genderPronoun;
    }

    public HashSet<FamilyRelation> getFamilyConnections() {
        return familyConnections;
    }

    public void setFamilyConnections(HashSet<FamilyRelation> familyConnections) {
        this.familyConnections = familyConnections;
    }

    public void addFamilyConnection(FamilyRelation familyConnection, FamilyRelationManager manager) {
        boolean isValid = manager.CheckInRelationship(familyConnection, this);
        if (isValid) {
            this.familyConnections.add(familyConnection);
        } else {
            throw new IllegalArgumentException("Cannot add a personal relationship a person is not in");
        }
    }

    public void removeFamilyConnection(FamilyRelation familyConnection) {
        this.familyConnections.remove(familyConnection);
    }
}
