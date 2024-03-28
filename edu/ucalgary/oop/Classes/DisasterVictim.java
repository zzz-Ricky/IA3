package edu.ucalgary.oop;

import java.util.HashSet;
import java.util.ArrayList;

public class DisasterVictim extends Person implements IDateManageMent {
    private String DateOfBirth_Age;
    private String description;
    private int ASSIGNED_SOCIAL_ID;
    private ArrayList<MedicalRecord> medicalRecords;
    private String ENTRY_DATE;
    private HashSet<Supply> personalBelongings;
    private ArrayList<DietaryRestrictions> dietaryPreference;
    static int counter;

    public DisasterVictim(String firstName, String ENTRY_DATE) {
        super(firstName);
        validateDate(ENTRY_DATE);
        this.ENTRY_DATE = ENTRY_DATE;
        this.medicalRecords = new ArrayList<>();
        this.personalBelongings = new HashSet<>();
        this.dietaryPreference = new ArrayList<>();
        this.ASSIGNED_SOCIAL_ID = counter;
        counter++;
    }

    public boolean validateDate(String date) {
        // Check if the date has the date in the correct format such as "2024-01-18"
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date must be in the format YYYY-MM-DD");
        }

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        // Check if the date numbers are possible real times.
        if (month < 1 || month > 12 || day < 1 || day > 31 ||
                (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) ||
                (month == 2 && (day > 29 || (day > 28 && !(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)))))) {
            throw new IllegalArgumentException("An Invalid date was given, must be a valid year, month, and day");
        }

        return true;
    }

    public String getDateOfBirth_Age() {
        return DateOfBirth_Age;
    }

    public ArrayList<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public String getENTRY_DATE() {
        return ENTRY_DATE;
    }

    public int getASSIGNED_SOCIAL_ID() {
        return ASSIGNED_SOCIAL_ID;
    }

    public HashSet<Supply> getPersonalBelongings() {
        return personalBelongings;
    }

    public ArrayList<DietaryRestrictions> getDietaryPreference() {
        return dietaryPreference;
    }

    public void setDateOfBirth(String DateOfBirth) {
        validateDate(DateOfBirth);
        this.DateOfBirth_Age = DateOfBirth;
    }

    public void setApproxAge(String age) {
        try {
            // assuming that our given age is some number like "18"
            // assumed to be years, as it would be far more common than any other
            // unit of time
            int parsedAge = Integer.parseInt(age);
            if (parsedAge > 0) {
                this.DateOfBirth_Age = age + " years";
            } else {
                throw new IllegalArgumentException("Age must be a positive non-zero integer.");
            }
        } catch (NumberFormatException e) {
            // assuming that our given age is some string like "three months"
            // Useful for when the approximate age is a guess or some specific phrase.
            this.DateOfBirth_Age = age;
        }
    }

    public void setMedicalRecords(ArrayList<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public void setPersonalBelongings(HashSet<Supply> personalBelongings) {
        this.personalBelongings = personalBelongings;
    }

    public void addPersonalBelonging(Supply supply, Location location, SupplyManager manager) {
        manager.giveSupply(this, supply, location);
    }

    public void removePersonalBelonging(Supply supply) {
        this.personalBelongings.remove(supply);
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecords.add(medicalRecord);
    }

    public void removeMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecords.remove(medicalRecord);
    }

    public void addDietaryPreference(DietaryRestrictions preference) {
        this.dietaryPreference.add(preference);
    }

    public void removeDietaryPreference(DietaryRestrictions preference) {
        this.dietaryPreference.remove(preference);
    }
}