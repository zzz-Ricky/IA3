package edu.ucalgary.oop;

import java.util.ArrayList;

public class ReliefService {
    private Inquirer inquirer;
    private ArrayList<Inquiry> inquiries;

    public ReliefService(Inquirer inquirer, ArrayList<Inquiry> inquiries) {
        this.inquirer = inquirer;
        this.inquiries = inquiries;
    }

    addInquiry(Inquiry inquiry){

    }

    removeInquiry(Inquiry inquiry){

    }

    public void setInquiries(ArrayList<Inquiry> inquiries) {
        this.inquiries = inquiries;
    }

    public ArrayList<Inquiry> getInquiries() {
        return inquiries;
    }

    public Inquirer getInquirer() {
        return inquirer;
    }
}