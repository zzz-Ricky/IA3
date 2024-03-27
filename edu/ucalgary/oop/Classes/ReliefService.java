package edu.ucalgary.oop;

public class ReliefService {
    private Inquirer inquirer;
    private DisasterVictim missingPerson;
    private String dateOfInquiry;
    private String infoProvided;
    private Location lastKnownLocation;

    public ReliefService(Inquirer inquirer, DisasterVictim missingPerson, String dateOfInquiry, String infoProvided, Location lastKnownLocation){
        for(int i = 0; i < dateOfInquiry.length(); i++){
            if(i == 4 && dateOfInquiry.charAt(4) != '-'){
                throw new IllegalArgumentException("Date of Inquiry must be in the format YYYY-MM-DD");
            }
            if(i == 7 && dateOfInquiry.charAt(7) != '-'){
                throw new IllegalArgumentException("Date of Inquiry must be in the format YYYY-MM-DD");
            }
            else if(i != 4 && i != 7){
                if(!Character.isDigit(dateOfInquiry.charAt(i))){
                    throw new IllegalArgumentException("Date of Inquiry must be in the format YYYY-MM-DD");
                }
            }
        }    
        this.inquirer = inquirer;
        this.missingPerson = missingPerson;
        this.dateOfInquiry = dateOfInquiry;
        this.infoProvided = infoProvided;
        this.lastKnownLocation = lastKnownLocation;
    }
    public Inquirer getInquirer(){
        return inquirer;
    }
    public void setInquirer(Inquirer inquirer){
        this.inquirer = inquirer;
    }
    public DisasterVictim getMissingPerson(){
        return missingPerson;
    }
    public void setMissingPerson(DisasterVictim missingPerson){
        this.missingPerson = missingPerson;
    }
    public String getDateOfInquiry(){
        return dateOfInquiry;
    }
    public void setDateOfInquiry(String dateOfInquiry){
        for(int i = 0; i < dateOfInquiry.length(); i++){
            if(i == 4 && dateOfInquiry.charAt(4) != '-'){
                throw new IllegalArgumentException("Date of Inquiry must be in the format YYYY-MM-DD");
            }
            if(i == 7 && dateOfInquiry.charAt(7) != '-'){
                throw new IllegalArgumentException("Date of Inquiry must be in the format YYYY-MM-DD");
            }
            else if(i != 4 && i != 7){
                if(!Character.isDigit(dateOfInquiry.charAt(i))){
                    throw new IllegalArgumentException("Date of Inquiry must be in the format YYYY-MM-DD");
                }
            }
        }   
        this.dateOfInquiry = dateOfInquiry;
    }
    public String getInfoProvided(){
        return infoProvided;
    }
    public void setInfoProvided(String infoProvided){
        this.infoProvided = infoProvided;
    }
    public Location getLastKnownLocation(){
        return lastKnownLocation;
    }
    public void setLastKnownLocation(Location lastKnownLocation){
        this.lastKnownLocation = lastKnownLocation;
    }
    public String getLogDetails(){
        String log = String.format("Inquirer: %s, Missing Person: %s, Date of Inquiry: %s, Info Provided: %s, Last Known Location: %s"
        ,inquirer.getFirstName(), missingPerson.getFirstName(), dateOfInquiry, infoProvided, lastKnownLocation.getName());
        return log;
    }
}