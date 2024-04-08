/**
 * The DisasterVictim class represents a victim of a disaster event.
 * It extends the Person class and implements the DateManageMent and InfoManagement interfaces.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The DisasterVictim class stores information such as date of birth/age, description, assigned social ID,
 * medical records, entry date, personal belongings, and dietary preferences.
 * </p>
 * <p>
 * The class provides methods to manage and retrieve information about the victim,
 * including setting date of birth/age, adding/removing medical records, personal belongings, and dietary preferences,
 * and accessing descriptive information.
 * </p>
 * <p>
 * The class also includes methods for file input/output operations.
 * These methods are inherited from the abstract Person class.
 * 
 * Date Management methods are implemented to perform date validation operations.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import java.util.HashSet;
import java.util.ArrayList;

public class DisasterVictim extends Person implements DateManageMent, InfoManagement {
    private String DateOfBirth_Age; // Stores DOB or approximate age.
    private String description; // Additional string descriptor.
    private final int ASSIGNED_SOCIAL_ID; // immutable and fixed social ID
    private ArrayList<MedicalRecord> medicalRecords; // List of disaster victim medical records
    private String ENTRY_DATE; // Date of entry to the relief center/system
    private HashSet<Supply> personalBelongings; // List of personal Belongings transferred from a location
    private ArrayList<DietaryRestrictions> dietaryPreference; // Special Dietary Preferences
    static int counter; // Counter that increments to generate new unique social IDs

    /**
     * Constructs a new DisasterVictim with the specified first name and entry date.
     *
     * @param firstName  The first name of the victim.
     * @param ENTRY_DATE The entry date of the victim.
     */
    public DisasterVictim(String firstName, String ENTRY_DATE) {
        super(firstName, null, null); // Pass null for lastName and genderPronoun
        validateDate(ENTRY_DATE);
        this.ENTRY_DATE = ENTRY_DATE;
        this.medicalRecords = new ArrayList<>();
        this.personalBelongings = new HashSet<>();
        this.dietaryPreference = new ArrayList<>();
        this.ASSIGNED_SOCIAL_ID = counter;
        counter++;
    }

    /**
     * Validates the format of the provided date string.
     *
     * @param date The date string to be validated.
     * @return true if the date string is in a valid format, false otherwise.
     */
    @Override
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

    public String getEntryDate() {
        return ENTRY_DATE;
    }

    public int getAssignedSocialID() {
        return ASSIGNED_SOCIAL_ID;
    }

    public HashSet<Supply> getPersonalBelongings() {
        return personalBelongings;
    }

    public ArrayList<DietaryRestrictions> getDietaryPreference() {
        return dietaryPreference;
    }

    /**
     * Regular setter method for date of birth. method passes input to
     * validateDate() beforehand
     *
     * @param age The exact DOB that the field will be set to.
     */
    public void setDateOfBirth(String DateOfBirth) {
        validateDate(DateOfBirth);
        this.DateOfBirth_Age = DateOfBirth;
    }

    /**
     * Sets the DateOfBirth_Age field to a non date string.
     * This is handled in case a user inputs information that is not exact enough to
     * be a date
     *
     * @param age The approximate age that the field will be set to.
     */
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

    public void addDietaryPreference(DietaryRestrictions.DietaryRestriction preference) {
        this.dietaryPreference.add(new DietaryRestrictions(preference));
    }

    public void removeDietaryPreference(DietaryRestrictions.DietaryRestriction preference) {
        this.dietaryPreference.removeIf(d -> d.getRestriction() == preference);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    @Override
    public void writeFile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void mountFile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dismountFile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDate(String newDate) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDate() {
        // TODO Auto-generated method stub
        return null;
    }

}