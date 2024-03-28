package edu.ucalgary.oop;

import java.util.ArrayList;

public class ReliefService {
    private Inquirer inquirer;
    private ArrayList<Inquiry> inquiries;

    public ReliefService(Inquirer inquirer, ArrayList<Inquiry> inquiries) {
        this.inquirer = inquirer;
        this.inquiries = inquiries;
    }

    public void addInquiry(Inquiry inquiry) {
        inquiries.add(inquiry);
    }

    public void removeInquiry(Inquiry inquiry) {
        inquiries.remove(inquiry);
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