package edu.ucalgary.oop;
public class MedicalRecord {
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;

    public MedicalRecord(Location location, String treatmentDetails, String dateOfTreatment) {
        for(int i = 0; i < dateOfTreatment.length(); i++){
            if(i == 4 && dateOfTreatment.charAt(4) != '-'){
                throw new IllegalArgumentException("Date of treatment must be in the format YYYY-MM-DD");
            }
            if(i == 7 && dateOfTreatment.charAt(7) != '-'){
                throw new IllegalArgumentException("Date of treatment must be in the format YYYY-MM-DD");
            }
            else if(i != 4 && i != 7){
                if(!Character.isDigit(dateOfTreatment.charAt(i))){
                    throw new IllegalArgumentException("Date of treatment must be in the format YYYY-MM-DD");
                }
            }
        }        
        this.location = location;
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment = dateOfTreatment;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public String getTreatmentDetails() {
        return treatmentDetails;
    }
    public void setTreatmentDetails(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }
    public String getDateOfTreatment(){
        return dateOfTreatment;
    }
    public void setDateOfTreatment(String dateOfTreatment) {
        for(int i = 0; i < dateOfTreatment.length(); i++){
            if(i == 4 && dateOfTreatment.charAt(4) != '-'){
                throw new IllegalArgumentException("Date of treatment must be in the format YYYY-MM-DD");
            }
            if(i == 7 && dateOfTreatment.charAt(7) != '-'){
                throw new IllegalArgumentException("Date of treatment must be in the format YYYY-MM-DD");
            }
            else if(i != 4 && i != 7){
                if(!Character.isDigit(dateOfTreatment.charAt(i))){
                    throw new IllegalArgumentException("Date of treatment must be in the format YYYY-MM-DD");
                }
            }
        }   
        this.dateOfTreatment = dateOfTreatment;
    }
}
