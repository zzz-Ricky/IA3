package edu.ucalgary.oop;

import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

abstract class Person implements ExternalFileIO {
    private String firstName;
    private String lastName;
    private HashSet<FamilyRelation> familyConnections;
    private String genderPronoun;
    private static ArrayList<String> genderOptions;

    public Person(String firstName, String lastName, String genderPronoun) {
        this.firstName = firstName;
        this.lastName = lastName != null ? lastName : "";
        this.familyConnections = new HashSet<>();
        if (genderOptions == null || genderOptions.isEmpty()) {
            genderOptions = readFile();
        }
        setGender(genderPronoun); // Validate and set gender pronoun
    }

    // Method to read gender options from file
    @Override
    public ArrayList<String> readFile() {
        ArrayList<String> genderOptions = new ArrayList<>();
        String filePath = "GenderOptions.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                genderOptions.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading gender options from file: " + e.getMessage());
            e.printStackTrace();
            // Handle the error appropriately, e.g., by using default gender options
        }
        return genderOptions;
    }

    // Validate and set gender pronoun
    public void setGender(String genderPronoun) {
        if (genderOptions.contains(genderPronoun)) {
            // Sets a person's gender to a selected option if it appears on the list.
            this.genderPronoun = genderPronoun;
        } else if (genderPronoun == null) {
            // Loads a default "Unknown" gender so that the constructor can have an
            // appropriate placeholder
            this.genderPronoun = "Unknown";
        } else {
            // Otherwise, if it is invalid, throw an exception.
            throw new IllegalArgumentException("Invalid gender pronoun. Please choose from the available options.");
        }
    }

    // Getters and setters for fields
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

    public ArrayList<String> getGenderOptions() {
        return genderOptions;
    }

    public HashSet<FamilyRelation> getFamilyConnections() {
        return familyConnections;
    }

    public void setFamilyConnections(HashSet<FamilyRelation> familyConnections) {
        this.familyConnections = familyConnections;
    }

    public void addFamilyConnection(FamilyRelation familyConnection, FamilyRelationManager manager) {
        boolean isValid = manager.checkInRelationship(familyConnection, this);
        if (isValid) {
            this.familyConnections.add(familyConnection);
            if (this == familyConnection.getPersonOne()
                    && !familyConnection.getPersonTwo().getFamilyConnections().contains(familyConnection)) {
                familyConnection.getPersonTwo().addFamilyConnection(familyConnection, manager);
            } else if (this == familyConnection.getPersonTwo()
                    && !familyConnection.getPersonOne().getFamilyConnections().contains(familyConnection)) {
                familyConnection.getPersonOne().addFamilyConnection(familyConnection, manager);
            }

        } else {
            throw new IllegalArgumentException("Cannot add a personal relationship a person is not in");
        }
    }

    public void removeFamilyConnection(FamilyRelation familyConnection) {
        this.familyConnections.remove(familyConnection);
    }
}
