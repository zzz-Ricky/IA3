package edu.ucalgary.oop;

import java.util.HashSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.ucalgary.oop.FamilyRelation;

abstract class Person implements ExternalFileIO {
    private String firstName;
    private String lastName;
    private HashSet<FamilyRelation> familyConnections;
    private String genderPronoun;
    static ArrayList<String> genderOptions;

    public Person(String firstName, String lastName, String genderPronoun) {
        this.firstName = firstName;
        this.lastName = lastName != null ? lastName : ""; // Assign an empty string if lastName is null
        this.genderPronoun = genderPronoun != null ? genderPronoun : ""; // Assign an empty string if genderPronoun is null
        this.familyConnections = new HashSet<>();
        this.loadGenderOptions(); // Load gender options from file
    }


    // Method to load gender options from file
    private void loadGenderOptions() {
        if (genderOptions == null || genderOptions.isEmpty()) {
            genderOptions = readFile();
        }
    }

 // Method to read gender options from file
    @Override
    public ArrayList<String> readFile() {
        ArrayList<String> genderOptions = new ArrayList<>();
        String filePath = "GenderOptions.txt"; // Assuming the file is in the same directory as the class
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
