/**
 * The ReliefService class represents a service responsible for managing relief inquiries
 * and associated inquirer information.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The ReliefService class facilitates the addition and removal of inquiries,
 * as well as retrieval of inquirer and inquiry information.
 * </p>
 * 
 * @author Ricky Huynh
 * @version 1.0
 * @since 2024-04-07
 */

package edu.ucalgary.oop;

import java.util.ArrayList;

public class ReliefService {
    private Inquirer inquirer;
    private ArrayList<Inquiry> inquiries;

    /**
     * Constructs a ReliefService object with the specified inquirer and list of
     * inquiries.
     *
     * @param inquirer  The inquirer associated with the relief service.
     * @param inquiries The list of inquiries managed by the relief service.
     */
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