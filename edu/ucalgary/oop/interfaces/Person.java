package edu.ucalgary.oop;

import java.util.HashSet;
import java.util.ArrayList;

import edu.ucalgary.oop.DisasterVictim;
import edu.ucalgary.oop.FamilyRelation;

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

    public void addFamilyConnection(FamilyRelation familyConnection) {
        // finish this later
    }

    public void removeFamilyConnection(FamilyRelation familyConnection) {
        // finish this later
    }
}
