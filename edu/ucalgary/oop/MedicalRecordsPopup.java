package edu.ucalgary.oop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MedicalRecordsPopup {

    private DisasterVictimPage parentWindow;
    private JFrame frame;
    private DefaultTableModel containedTableModel;
    private JTable containedTable;
    private DisasterVictim selectedVictim; // Changed to class-level field
    private Location location;
    
    public MedicalRecordsPopup(ArrayList<MedicalRecord> records, ArrayList<Location> shelters, JTable victimTable,
            DefaultTableModel tableModel, ArrayList<DisasterVictim> victims, DisasterVictimPage parentWindow) {
        this.parentWindow = parentWindow;
        // Handle Medical Record
        JFrame frame = new JFrame("Medical Records");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        // Create table model for contained objects
        DefaultTableModel containedTableModel = new DefaultTableModel();
        JTable containedTable = new JTable(containedTableModel);
        JScrollPane containedScrollPane = new JScrollPane(containedTable);

        // Add columns to the table model
        containedTableModel.addColumn("Location");
        containedTableModel.addColumn("Treatment Details");
        containedTableModel.addColumn("Date of Treatment");

        // Populate the table model with data
        for (MedicalRecord record : records) {
            containedTableModel.addRow(new Object[] {
                    record.getLocation().getName(),
                    record.getDescription(),
                    record.getDate()
            });
        }

        // Add the table to the frame
        frame.add(containedScrollPane);

        JButton addButton = new JButton("Add New Medical Record");
        addButton.addActionListener(f -> {
            // Open a new window prompting the user to create a new DisasterVictim object
            JFrame addMedicalFrame = new JFrame("Add New Medical Record");
            addMedicalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addMedicalFrame.setSize(400, 200);
            
            JComboBox<String> locationComboBox = new JComboBox<>();
            int selectedRow = victimTable.getSelectedRow();
            if (selectedRow != -1) {
            this.selectedVictim = victims.get(selectedRow); // Store the selected victim
            }
            
            // Input fields for the new MedicalRecord object
            for (Location location : shelters) {
                // Check if the selected person is among the occupants of this location
                if (location.getOccupants().contains(this.selectedVictim)) {
                    locationComboBox.setSelectedItem(location.getName());
                    locationComboBox.addItem(location.getName());
                }  
            }

            // Get the selected location's name
            String selectedLocationName = (String) locationComboBox.getSelectedItem();

            // Create a text field to display the selected location's name
            JTextField locationTextField = new JTextField(selectedLocationName);
            locationTextField.setEditable(false); // Set non-editable
            locationTextField.setBackground(Color.LIGHT_GRAY); // Grey out the field
            
            JTextField treatmentField = new JTextField(20);
            JTextField dateField = new JTextField(20);
            JButton saveButton = new JButton("Save");

            // Action listener for the save button
            saveButton.addActionListener(actionEvent -> {
                // Create a new MedicalRecord object and add it to the corresponding victim
                String locationName = (String) locationComboBox.getSelectedItem();
                String treatment = treatmentField.getText();
                String date = dateField.getText();
                
                for (Location loc : shelters) {
                    if (loc.getName().equals(locationName)) {
                        this.location = loc;
                        break;
                    }
                }

                if (this.location != null) {
                        DisasterVictim victim = this.selectedVictim; // Use the stored selected victim
                        MedicalRecord newMedicalRecord = new MedicalRecord(location, treatment, date);
                        victim.addMedicalRecord(newMedicalRecord);
                        // Refresh the Medical Record table
                        refreshMedicalRecordTable(victim.getMedicalRecords(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims, shelters);
                        // Close the window after adding the new medical record
                        addMedicalFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(addMedicalFrame, "Selected location is not valid.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Add components to the frame
            GridLayout layout = new GridLayout(0, 2);
            addMedicalFrame.setLayout(layout);

            // Add components
            addMedicalFrame.add(new JLabel("Location Name:"));
            addMedicalFrame.add(locationTextField);
            addMedicalFrame.add(new JLabel("Treatment Details:"));
            addMedicalFrame.add(treatmentField);
            addMedicalFrame.add(new JLabel("Treatment Date:"));
            addMedicalFrame.add(dateField);
            addMedicalFrame.add(saveButton);

            // Set the frame visible
            addMedicalFrame.setLocationRelativeTo(null);
            addMedicalFrame.setVisible(true);
        });
        
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedMedicalRecordRow = containedTable.getSelectedRow();
                
                if (selectedVictim != null && selectedMedicalRecordRow != -1) {
                    String itemName = containedTable.getValueAt(selectedMedicalRecordRow, 1).toString();
                    MedicalRecord medicalRecord = findMedicalRecordByName(itemName, selectedVictim.getMedicalRecords());
                    if (medicalRecord != null) {
                        // Remove the medical record from the victim's records
                        selectedVictim.removeMedicalRecord(medicalRecord);
                        // Refresh the Medical Record table
                        refreshMedicalRecordTable(selectedVictim.getMedicalRecords(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims, shelters);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selected medical record not found.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a victim and a medical record to remove.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton, BorderLayout.NORTH);
        buttonPanel.add(removeButton, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void refreshMedicalRecordTable(ArrayList<MedicalRecord> records, DefaultTableModel containedTable) {
        // Clear the existing rows
        containedTable.setRowCount(0);

        // Populate the table model with updated data
        for (MedicalRecord record : records) {
            containedTable.addRow(new Object[] {
                    record.getLocation().getName(),
                    record.getDescription(),
                    record.getDate()
            });
        }
        // Notify the table of the changes
        containedTable.fireTableDataChanged();
    }
    
    // Method to find a MedicalRecord object by its name
    private MedicalRecord findMedicalRecordByName(String name, ArrayList<MedicalRecord> records) {
        for (MedicalRecord record : records) {
            if (record.getDescription().equals(name)) {
                return record;
            }
        }
        return null; // If no location with the given name is found
    }
}
