/**
 * The FamilyConnectionsPopup class represents a popup window for managing the family relations of disaster victims.
 * It allows users to view, add, and remove family relations for a selected victim.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The popup window displays a table of existing family relations for the selected victim,
 * provides options to add new family relations, and remove existing ones.
 * </p>
 * <p>
 * The family relations are managed through interactions with a main window (parent window),
 * a table of victims, and a table model.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

public class FamilyConnectionsPopup {
    private DisasterVictim lastSelectedPerson; // Used to cache the selectedVictim, prevents edge case bugs

    /**
     * Constructs a FamilyConnectionsPopup object with the specified parameters.
     *
     * @param connections   The set of existing family connections.
     * @param victimTable   The table displaying victim information.
     * @param tableModel    The table model for victim information.
     * @param victims       The list of disaster victims.
     * @param familyManager The manager for family relations.
     * @param locations     The list of locations.
     * @param parentWindow  The parent window.
     */
    public FamilyConnectionsPopup(HashSet<FamilyRelation> connections, JTable victimTable, DefaultTableModel tableModel,
            ArrayList<DisasterVictim> victims, FamilyRelationManager familyManager, ArrayList<Location> locations,
            DisasterVictimPage parentWindow) {

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
            // Open a new window prompting the user to create a new FamilyRelation object
            JFrame addConnectionFrame = new JFrame("Add New Family Connection");
            addConnectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addConnectionFrame.setSize(300, 200);

            // Input fields for the new FamilyRelation object
            JTextField firstIDField = new JTextField(20);
            JTextField relationField = new JTextField(20);
            JTextField secondIDField = new JTextField(20);
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

            // Create a save button within the new secondary popup window.
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(actionEvent -> {
                // Create a new FamilyRelation object and add it to the table
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
                parentWindow.refreshTable(victims, locations);
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
            addConnectionFrame.setLocationRelativeTo(null);
            addConnectionFrame.setVisible(true);
        });

        // Create a remove button within the new secondary popup window.
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = containedTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Get the data of the selected row
                    String personOneName = (String) containedTable.getValueAt(selectedRow, 0);
                    String relationshipTo = (String) containedTable.getValueAt(selectedRow, 1);
                    String personTwoName = (String) containedTable.getValueAt(selectedRow, 2);

                    DisasterVictim personOne = null;
                    DisasterVictim personTwo = null;

                    // Find the FamilyRelation object corresponding to the selected row
                    for (FamilyRelation relation : familyManager.getRelationshipRecord()) {
                        if (relation.getPersonOne().getFirstName().equals(personOneName) &&
                                relation.getRelationshipTo().equals(relationshipTo) &&
                                relation.getPersonTwo().getFirstName().equals(personTwoName)) {

                            // Get the DisasterVictim objects from the FamilyRelation
                            personOne = relation.getPersonOne();
                            personTwo = relation.getPersonTwo();

                            // Remove the family relation from the victims
                            personOne.removeFamilyConnection(relation);
                            personTwo.removeFamilyConnection(relation);

                            // Remove the family relation from the familyManager registry
                            familyManager.removeRelationship(relation);
                            // Refresh the family connection table
                            refreshFamilyRelationTable(familyManager.getRelationshipRecord(), containedTableModel);
                            // Refresh the main victims table
                            parentWindow.refreshTable(victims, locations);
                            return; // Exit the method after removing the relation
                        }
                    }

                    // If the relation was not found
                    JOptionPane.showMessageDialog(frame, "Selected family connection not found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a family connection to remove.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add the button to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton, BorderLayout.NORTH);
        buttonPanel.add(removeButton, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(containedScrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Refreshes the family relation table with updated data.
     *
     * @param connections         The updated set of family connections.
     * @param containedTableModel The table model for family connections.
     */
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
