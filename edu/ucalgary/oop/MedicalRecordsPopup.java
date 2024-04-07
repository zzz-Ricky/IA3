package edu.ucalgary.oop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MedicalRecordsPopup {

    private DisasterVictimPage parentWindow;
    private JFrame frame;
    private DefaultTableModel containedTableModel;
    private JTable containedTable;
    private JButton addButton;
    private DisasterVictim lastSelectedPerson;

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
            addMedicalFrame.setLayout(new FlowLayout());

            // Input fields for the new DisasterVictim object
            JComboBox<String> locationComboBox = new JComboBox<>();
            for (Location location : shelters) {
                locationComboBox.addItem(location.getName());
            }
            JTextField treatmentField = new JTextField(20);
            JTextField dateField = new JTextField(20);
            JButton saveButton = new JButton("Save");

            // Action listener for the save button
            saveButton.addActionListener(actionEvent -> {
                // Create a new MedicalRecord object and add it to the corresponding victim
                String locationName = (String) locationComboBox.getSelectedItem();
                String treatment = treatmentField.getText();
                String date = dateField.getText();

                Location location = null;
                for (Location loc : shelters) {
                    if (loc.getName().equals(locationName)) {
                        location = loc;
                        break;
                    }
                }

                if (location != null) {
                    int selectedRow = victimTable.getSelectedRow();
                    if (selectedRow != -1) {
                        DisasterVictim victim = victims.get(selectedRow);
                        lastSelectedPerson = victims.get(selectedRow);
                        MedicalRecord newMedicalRecord = new MedicalRecord(location, treatment, date);
                        victim.addMedicalRecord(newMedicalRecord);
                        // Refresh the Medical Record table
                        refreshMedicalRecordTable(victim.getMedicalRecords(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims);
                        // Close the window after adding the new medical record
                        addMedicalFrame.dispose();
                    } else {
                        if (lastSelectedPerson != null) {
                            MedicalRecord newMedicalRecord = new MedicalRecord(location, treatment, date);
                            lastSelectedPerson.addMedicalRecord(newMedicalRecord);
                            // Refresh the Medical Record table
                            refreshMedicalRecordTable(lastSelectedPerson.getMedicalRecords(),
                                    containedTableModel);
                            // Refresh the main victims table
                            parentWindow.refreshTable(victims);
                            // Close the window after adding the new medical record
                            addMedicalFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Please select a row to add a new Medical Record Item.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
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
            addMedicalFrame.add(locationComboBox);
            addMedicalFrame.add(new JLabel("Treatment Details:"));
            addMedicalFrame.add(treatmentField);
            addMedicalFrame.add(new JLabel("Treatment Date:"));
            addMedicalFrame.add(dateField);
            addMedicalFrame.add(saveButton);

            // Set the frame visible
            addMedicalFrame.setVisible(true);
        });

        frame.add(addButton, BorderLayout.NORTH);
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
}
