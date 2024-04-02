package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserInterface extends JFrame {
    private DefaultTableModel inquiryTableModel;
    private JTable inquiryTable;
    private DefaultTableModel logTableModel;
    private JTable logTable;
    private AccessDatabaseLogs database;
    private JButton saveEditsButton;
    private JButton discardEditsButton;
    private JButton removeButton;

    public UserInterface() {
        setTitle("Inquiry Logs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        database = new AccessDatabaseLogs();

        // Create table model for INQUIRER
        inquiryTableModel = new DefaultTableModel();
        inquiryTableModel.addColumn("First Name");
        inquiryTableModel.addColumn("Last Name");
        inquiryTableModel.addColumn("Phone Number");
        inquiryTable = new JTable(inquiryTableModel);

        // Create table model for INQUIRY_LOG
        logTableModel = new DefaultTableModel();
        logTableModel.addColumn("First Name");
        logTableModel.addColumn("Last Name");
        logTableModel.addColumn("Call Date");
        logTableModel.addColumn("Details");
        logTable = new JTable(logTableModel);

        // Fetch inquiries and logs from database and add to tables
        updateInquiryTable();
        updateLogTable();

        // Create scroll panes for tables
        JScrollPane inquiryScrollPane = new JScrollPane(inquiryTable);
        JScrollPane logScrollPane = new JScrollPane(logTable);

        // Create panel to hold the scroll panes
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(createPanelWithHeading("Inquirers", inquiryScrollPane));
        panel.add(createPanelWithHeading("Inquiry Logs", logScrollPane));
        getContentPane().add(panel, BorderLayout.CENTER);

        // Add button to add new inquirer
        JButton addInquirerButton = new JButton("Add Inquirer");
        addInquirerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = JOptionPane.showInputDialog(UserInterface.this, "Enter first name:");
                String lastName = JOptionPane.showInputDialog(UserInterface.this, "Enter last name:");
                String phoneNumber = JOptionPane.showInputDialog(UserInterface.this, "Enter phone number:");
                if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty()) {
                    // Add the new inquirer to the database and update the table
                    database.addInquirer(firstName, lastName, phoneNumber);
                    updateInquiryTable();
                } else {
                    JOptionPane.showMessageDialog(UserInterface.this, "Please fill in all fields.");
                }
            }
        });

        // Add button to add new inquiry log
        JButton addInquiryLogButton = new JButton("Add Inquiry Log");
        addInquiryLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = JOptionPane.showInputDialog(UserInterface.this, "Enter first name:");
                String lastName = JOptionPane.showInputDialog(UserInterface.this, "Enter last name:");
                String callDate = JOptionPane.showInputDialog(UserInterface.this, "Enter call date (YYYY-MM-DD):");
                String details = JOptionPane.showInputDialog(UserInterface.this, "Enter details:");
                if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty() && callDate != null && !callDate.isEmpty() && details != null && !details.isEmpty()) {
                    // Add the new inquiry log to the database and update the table
                    database.addInquiryLog(firstName, lastName, callDate, details);
                    updateLogTable();
                } else {
                    JOptionPane.showMessageDialog(UserInterface.this, "Please fill in all fields.");
                }
            }
        });

     // Add button to remove selected row
        removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = inquiryTable.getSelectedRow();
                if (selectedRow != -1) {
                    // If the inquiry table is selected, remove the inquirer
                    String firstName = inquiryTableModel.getValueAt(selectedRow, 0).toString();
                    String lastName = inquiryTableModel.getValueAt(selectedRow, 1).toString();
                    String phoneNumber = inquiryTableModel.getValueAt(selectedRow, 2).toString();
                    database.removeInquirer(firstName, lastName, phoneNumber);
                    updateInquiryTable();
                } else {
                    // If the log table is selected, remove the inquiry log
                    selectedRow = logTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String firstName = logTableModel.getValueAt(selectedRow, 0).toString();
                        String lastName = logTableModel.getValueAt(selectedRow, 1).toString();
                        String details = logTableModel.getValueAt(selectedRow, 3).toString();
                        database.removeInquiryLog(firstName, lastName, details);
                        updateLogTable();
                    } else {
                        JOptionPane.showMessageDialog(UserInterface.this, "Please select an item to remove.");
                    }
                }
            }
        });




        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(addInquirerButton);
        buttonPanel.add(addInquiryLogButton);
        buttonPanel.add(removeButton);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);

        // Enable cell selection
        inquiryTable.setCellSelectionEnabled(true);
        logTable.setCellSelectionEnabled(true);

        // Add panel for save/discard edits buttons (initially hidden)
        JPanel saveDiscardPanel = new JPanel(new GridLayout(1, 2));
        saveEditsButton = new JButton("Save Edits");
        discardEditsButton = new JButton("Discard Edits");
        saveEditsButton.setVisible(false);
        discardEditsButton.setVisible(false);
        saveDiscardPanel.add(saveEditsButton);
        saveDiscardPanel.add(discardEditsButton);
        getContentPane().add(saveDiscardPanel, BorderLayout.SOUTH);

        // Add action listeners to save and discard edits buttons
        saveEditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Iterate over rows in the inquiry table
                    for (int i = 0; i < inquiryTableModel.getRowCount(); i++) {
                        int inquirerId = Integer.parseInt(inquiryTableModel.getValueAt(i, 0).toString());
                        String firstName = inquiryTableModel.getValueAt(i, 1).toString();
                        String lastName = inquiryTableModel.getValueAt(i, 2).toString();
                        String phoneNumber = inquiryTableModel.getValueAt(i, 3).toString();

                        // Update the inquirer record in the database
                        database.updateInquirer(inquirerId, firstName, lastName, phoneNumber);
                    }

                    // Iterate over rows in the log table
                    for (int i = 0; i < logTableModel.getRowCount(); i++) {
                        // Assuming the callDate and details are not editable in the table
                        int inquirerId = Integer.parseInt(logTableModel.getValueAt(i, 0).toString());
                        String callDate = logTableModel.getValueAt(i, 2).toString();
                        String details = logTableModel.getValueAt(i, 3).toString();

                        // Update the inquiry log record in the database
                        database.updateInquiryLog(inquirerId, callDate, details);
                    }

                    // Hide save/discard buttons
                    saveEditsButton.setVisible(false);
                    discardEditsButton.setVisible(false);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(UserInterface.this, "Invalid input. Please enter valid numbers.");
                }
            }
        });

        discardEditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Discard edits and revert changes
                updateInquiryTable();
                updateLogTable();
                saveEditsButton.setVisible(false);
                discardEditsButton.setVisible(false);
            }
        });
    }

    // Method to create a panel with a heading title
    private JPanel createPanelWithHeading(String title, JScrollPane scrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Method to update the inquiry table
    private void updateInquiryTable() {
        inquiryTableModel.setRowCount(0);
        ArrayList<String[]> inquiries = database.getAllInquiries();
        for (String[] inquiry : inquiries) {
            inquiryTableModel.addRow(inquiry);
        }
    }

    // Method to update the log table
    private void updateLogTable() {
        logTableModel.setRowCount(0);
        ArrayList<String[]> logs = database.getAllInquiryLogs();
        for (String[] log : logs) {
            logTableModel.addRow(log);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInterface userInterface = new UserInterface();
            userInterface.setVisible(true);
        });
    }
}
