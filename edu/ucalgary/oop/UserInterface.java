package edu.ucalgary.oop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserInterface extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> inquiryList;

    public UserInterface() {
        setTitle("Inquiry Logs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create list to display inquiries
        listModel = new DefaultListModel<>();
        inquiryList = new JList<>(listModel);

        // Fetch inquiries from database and add to list
        updateInquiryList();

        JScrollPane scrollPane = new JScrollPane(inquiryList);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Add a button to add new inquiry
        JButton addButton = new JButton("Add Inquiry");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newInquiry = JOptionPane.showInputDialog(UserInterface.this, "Enter new inquiry:");
                if (newInquiry != null && !newInquiry.isEmpty()) {
                    // Add the new inquiry to the database and update the list
                    AccessDatabaseLogs.addInquiry(newInquiry);
                    updateInquiryList();
                }
            }
        });
        getContentPane().add(addButton, BorderLayout.NORTH);

        // Add a button to remove selected inquiry
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = inquiryList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedInquiry = listModel.getElementAt(selectedIndex);
                    // Remove the selected inquiry from the database and update the list
                    AccessDatabaseLogs.removeInquiry(selectedInquiry);
                    updateInquiryList();
                } else {
                    JOptionPane.showMessageDialog(UserInterface.this, "Please select an inquiry to remove.");
                }
            }
        });
        getContentPane().add(removeButton, BorderLayout.SOUTH);
    }

    // Method to update the list of inquiries displayed
    private void updateInquiryList() {
        listModel.clear();
        ArrayList<String> inquiries = AccessDatabaseLogs.getAllInquiries();
        for (String inquiry : inquiries) {
            listModel.addElement(inquiry);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInterface userInterface = new UserInterface();
            userInterface.setVisible(true);
        });
    }
}
