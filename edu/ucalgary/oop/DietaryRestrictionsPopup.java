package edu.ucalgary.oop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DietaryRestrictionsPopup {
    private DisasterVictimPage parentWindow;
    private DisasterVictim lastSelectedPerson;

    DietaryRestrictionsPopup(ArrayList<DietaryRestrictions> restrictions, JTable victimTable,
            DefaultTableModel tableModel, ArrayList<DisasterVictim> victims, DisasterVictimPage parentWindow) {
        this.parentWindow = parentWindow;

        // Handle Dietary Restrictions
        JFrame frame = new JFrame("Dietary Restrictions");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        // Create table model for contained objects
        DefaultTableModel containedTableModel = new DefaultTableModel();
        JTable containedTable = new JTable(containedTableModel);
        JScrollPane containedScrollPane = new JScrollPane(containedTable);

        // Add columns to the table model
        containedTableModel.addColumn("Preferences");

        // Populate the table model with data
        for (DietaryRestrictions restriction : restrictions) {
            containedTableModel.addRow(new Object[] {
                    restriction.getRestriction()
            });
        }
        // Add the table to the frame
        frame.add(containedScrollPane);
        JButton addButton = new JButton("Add New Dietary Restriction");
        addButton.addActionListener(f -> {
            // Open a new window prompting the user to add a new Dietary Restrictions
            JFrame addDietPreferenceFrame = new JFrame("Add New Dietary Preference");
            addDietPreferenceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addDietPreferenceFrame.setSize(300, 200);
            addDietPreferenceFrame.setLayout(new FlowLayout());

            ArrayList<Object> possibleDietOptions = new ArrayList<Object>();
            for (DietaryRestrictions.DietaryRestriction restriction : DietaryRestrictions.DietaryRestriction
                    .values()) {
                possibleDietOptions.add(restriction);
            }

            JComboBox<Object> restrictionComboBox = new JComboBox<>();
            for (Object restriction : possibleDietOptions) {
                restrictionComboBox.addItem(restriction.toString());
            }

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(actionEvent -> {
                String restrictionName = (String) restrictionComboBox.getSelectedItem();
                // Check if restrictionName is not null before proceeding
                if (restrictionName != null) {
                    int selectedRow = victimTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Check if a row is still selected. Done to remedy the row deselecting itself
                        DisasterVictim victim = victims.get(selectedRow);
                        // Cache selected row so repeated supply additions can be performed.
                        lastSelectedPerson = victims.get(selectedRow);
                        // Add the dietary restriction
                        DietaryRestrictions.DietaryRestriction restriction = DietaryRestrictions.DietaryRestriction
                                .valueOf(restrictionName);
                        victim.addDietaryPreference(restriction);
                        // Refresh the dietary restrictions table
                        refreshDietaryRestrictionsTable(victim.getDietaryPreference(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims);
                        addDietPreferenceFrame.dispose();
                    } else {
                        if (lastSelectedPerson != null) {
                            // Add the dietary restriction
                            DietaryRestrictions.DietaryRestriction restriction = DietaryRestrictions.DietaryRestriction
                                    .valueOf(restrictionName);
                            lastSelectedPerson.addDietaryPreference(restriction);
                            // Refresh the dietary restrictions table
                            refreshDietaryRestrictionsTable(lastSelectedPerson.getDietaryPreference(),
                                    containedTableModel);
                            // Refresh the main victims table
                            parentWindow.refreshTable(victims);
                            addDietPreferenceFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Please select a row to add a new family connection.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    // Handle the case where restrictionName is null
                    JOptionPane.showMessageDialog(frame,
                            "An unexpected error occured while fetching dietary restrictions", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
            // Add components to the frame
            GridLayout layout = new GridLayout(0, 2);
            addDietPreferenceFrame.setLayout(layout);

            // Add components
            addDietPreferenceFrame.add(new JLabel("Dietary Restriction:"));
            addDietPreferenceFrame.add(restrictionComboBox);
            addDietPreferenceFrame.add(saveButton);

            // Set the frame visible
            addDietPreferenceFrame.setVisible(true);
        });
        // Add the button to the frame
        frame.add(addButton, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private void refreshDietaryRestrictionsTable(ArrayList<DietaryRestrictions> preferences,
            DefaultTableModel containedTable) {
        // Clear the existing rows
        containedTable.setRowCount(0);

        // Populate the table model with updated data
        for (DietaryRestrictions preference : preferences) {
            containedTable.addRow(new Object[] {
                    preference.getRestriction().toString(),
            });
        }
        // Notify the table of the changes
        containedTable.fireTableDataChanged();
    }
}
