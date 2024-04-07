package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class FamilyConnectionsPopup {
    private DisasterVictimPage parentWindow;
    private JFrame frame;
    private DefaultTableModel containedTableModel;
    private JTable containedTable;
    private JButton addButton;
    private DisasterVictim lastSelectedPerson;

    public FamilyConnectionsPopup(HashSet<FamilyRelation> connections, JTable victimTable, DefaultTableModel tableModel,
            ArrayList<DisasterVictim> victims, FamilyRelationManager familyManager, DisasterVictimPage parentWindow) {
        this.parentWindow = parentWindow;

        // Handle Family Connections
        JFrame frame = new JFrame("Family Connections");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        // Create table model for contained objects
        DefaultTableModel containedTableModel = new DefaultTableModel();
        JTable containedTable = new JTable(containedTableModel);
        JScrollPane containedScrollPane = new JScrollPane(containedTable);

        // Add columns to the table model
        containedTableModel.addColumn("Person One");
        containedTableModel.addColumn("Relationship To");
        containedTableModel.addColumn("Person Two");

        // Populate the table model with data
        for (FamilyRelation connection : connections) {
            containedTableModel.addRow(new Object[] {
                    connection.getPersonOne().getFirstName(),
                    connection.getRelationshipTo(),
                    connection.getPersonTwo().getFirstName()
            });
        }

        // Add the button to the frame
        JButton addButton = new JButton("Add New Family Connection");
        addButton.addActionListener(f -> {
            // Open a new window prompting the user to create a new DisasterVictim object
            JFrame addConnectionFrame = new JFrame("Add New Family Connection");
            addConnectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addConnectionFrame.setSize(300, 200);
            addConnectionFrame.setLayout(new FlowLayout());

            // Input fields for the new DisasterVictim object
            JTextField firstIDField = new JTextField(20);
            JTextField relationField = new JTextField(20);
            JTextField secondIDField = new JTextField(20);
            JButton saveButton = new JButton("Save");
            int selectedRow = victimTable.getSelectedRow();
            if (selectedRow != -1) {
                // Retrieve the ID of the person from the corresponding row
                int personID = (int) tableModel.getValueAt(selectedRow, 6);
                // cache selected row so repeated relationship additions can be performed.
                lastSelectedPerson = victims.get(selectedRow);
                firstIDField.setText(String.valueOf(personID));
                firstIDField.setEditable(false); // Set non-editable
                firstIDField.setBackground(Color.LIGHT_GRAY); // Grey out the field
            } else {
                if (lastSelectedPerson != null) {
                    int personID = lastSelectedPerson.getAssignedSocialID();
                    firstIDField.setText(String.valueOf(personID));
                    firstIDField.setEditable(false); // Set non-editable
                    firstIDField.setBackground(Color.LIGHT_GRAY); // Grey out the field
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Please select a row to add a new family connection.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            // Action listener for the save button
            saveButton.addActionListener(actionEvent -> {

                // Create a new DisasterVictim object and add it to the table
                int id1 = Integer.parseInt(firstIDField.getText());
                String relation = relationField.getText();
                int id2 = Integer.parseInt(secondIDField.getText());

                DisasterVictim person1 = null;
                DisasterVictim person2 = null;

                // Find the DisasterVictim objects corresponding to the provided IDs
                for (DisasterVictim victim : victims) {
                    if (victim.getAssignedSocialID() == (id1)) {
                        person1 = victim;
                    }
                    if (victim.getAssignedSocialID() == (id2)) {
                        person2 = victim;
                    }
                    // Break the loop if both persons are found
                    if (person1 != null && person2 != null) {
                        break;
                    }
                }

                // If either person is not found, handle the error appropriately
                if (person1 == null || person2 == null) {
                    JOptionPane.showMessageDialog(addConnectionFrame, "One or both persons not found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create a new FamilyRelation object and add it to the list
                FamilyRelation newRelation = new FamilyRelation(person1, relation, person2, familyManager);
                person1.addFamilyConnection(newRelation, familyManager);
                refreshFamilyRelationTable(person1.getFamilyConnections(), containedTableModel);
                parentWindow.refreshTable(victims);
                addConnectionFrame.dispose(); // Close the window after adding the new family relation
            });

            // Add components to the frame

            GridLayout layout = new GridLayout(0, 2);
            addConnectionFrame.setLayout(layout);

            addConnectionFrame.add(new JLabel("ID of Person A:"));
            addConnectionFrame.add(firstIDField);
            addConnectionFrame.add(new JLabel("Relationship Type:"));
            addConnectionFrame.add(relationField);
            addConnectionFrame.add(new JLabel("ID of Person B:"));
            addConnectionFrame.add(secondIDField);
            addConnectionFrame.add(saveButton);

            // Set the frame visible
            addConnectionFrame.setVisible(true);
        });
        frame.add(addButton, BorderLayout.NORTH);
        frame.add(containedScrollPane);
        frame.setVisible(true);
    }

    private void refreshFamilyRelationTable(HashSet<FamilyRelation> connections, DefaultTableModel containedTable) {
        // Clear the existing rows
        containedTable.setRowCount(0);

        // Populate the table model with updated data
        for (FamilyRelation connection : connections) {
            containedTable.addRow(new Object[] {
                    connection.getPersonOne().getFirstName(),
                    connection.getRelationshipTo(),
                    connection.getPersonTwo().getFirstName()
            });
        }
        // Notify the table of the changes
        containedTable.fireTableDataChanged();
    }
}
