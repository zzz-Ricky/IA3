package edu.ucalgary.oop;

public class MedicalRecord implements DateManageMent, InfoManagement {
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;

    public MedicalRecord(Location location, String treatmentDetails, String dateOfTreatment) {
        this.location = location;
        this.treatmentDetails = treatmentDetails;
        validateDate(dateOfTreatment);
        this.dateOfTreatment = dateOfTreatment;
    }
    
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    @Override
    public String getDescription() {
        return treatmentDetails;
    }
    
    @Override
    public void setDescription(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }
    
    @Override
    public String getDate() {
        return dateOfTreatment;
    }
    
    @Override
    public void setDate(String dateOfTreatment) {
        validateDate(dateOfTreatment);
        this.dateOfTreatment = dateOfTreatment;
    }
}
