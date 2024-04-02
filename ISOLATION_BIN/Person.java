package edu.ucalgary.oop;

import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.ucalgary.oop.DisasterVictim;
import edu.ucalgary.oop.ExternalFileIO;
import edu.ucalgary.oop.FamilyRelation;

abstract class Person implements ExternalFileIO {
    private String firstName;
    private String lastName;
    private HashSet<FamilyRelation> familyConnections;
    private String genderPronoun;
    static ArrayList<String> genderOptions;

    static {
        genderOptions = new ArrayList<>();
        loadGenderOptions();
    }

    public Person() {
        this.firstName = "";
        this.lastName = "";
        this.familyConnections = new HashSet<>();
        this.genderPronoun = "";
    }

    private void loadGenderOptions() {
        if (genderOptions.isEmpty()) {
            ExternalFileIO fileIO = new ExternalFileIO();
            this.genderOptions = fileIO.readGenderOptionsFromFile("GenderOptions.txt");
        }
    }

    public ArrayList<String> readGenderOptionsFromFile(String filename) {
        ArrayList<String> genderOptions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line contains a single gender option
                genderOptions.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading gender options from file: " + e.getMessage());
            // Handle the error appropriately, e.g., by using default gender options
        }
        return genderOptions;
    }

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
