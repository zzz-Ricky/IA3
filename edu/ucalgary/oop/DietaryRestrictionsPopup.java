/**
 * The DietaryRestrictionsPopup class represents a popup window for managing dietary restrictions of disaster victims.
 * It allows users to view, add, and remove dietary restrictions for a selected victim.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The popup window displays a table of existing dietary restrictions for the selected victim,
 * provides options to add new dietary restrictions, and remove existing ones.
 * </p>
 * <p>
 * The dietary restrictions are managed through interactions with a main window (parent window),
 * a table of victims, and a table model.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DietaryRestrictionsPopup {
    private DisasterVictimPage parentWindow;
    private DisasterVictim selectedVictim; // Changed to class-level field
    private DisasterVictim lastSelectedPerson; //Used to cache the selectedVictim, prevents edge case bugs
    
    /**
     * Constructs a new DietaryRestrictionsPopup with the specified parameters.
     *
     * @param restrictions   The list of existing dietary restrictions.
     * @param victimTable    The table of disaster victims.
     * @param tableModel     The table model for the victim table.
     * @param victims        The list of disaster victims.
     * @param locations      The list of locations.
     * @param parentWindow   The parent window (main window).
     */
    DietaryRestrictionsPopup(ArrayList<DietaryRestrictions> restrictions, JTable victimTable,
            DefaultTableModel tableModel, ArrayList<DisasterVictim> victims, ArrayList<Location> locations, DisasterVictimPage parentWindow) {
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
                        selectedVictim = victims.get(selectedRow); // Store the selected victim
                        lastSelectedPerson = victims.get(selectedRow);
                        // Add the dietary restriction
                        DietaryRestrictions.DietaryRestriction restriction = DietaryRestrictions.DietaryRestriction
                                .valueOf(restrictionName);
                        selectedVictim.addDietaryPreference(restriction);
                        // Refresh the dietary restrictions table
                        refreshDietaryRestrictionsTable(selectedVictim.getDietaryPreference(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims, locations);
                        addDietPreferenceFrame.dispose();
                    } else {
                        if (lastSelectedPerson != null) {
                            // Add the dietary restriction
                            DietaryRestrictions.DietaryRestriction restriction = DietaryRestrictions.DietaryRestriction
                                    .valueOf(restrictionName);
                            selectedVictim.addDietaryPreference(restriction);
                            // Refresh the dietary restrictions table
                            refreshDietaryRestrictionsTable(selectedVictim.getDietaryPreference(),
                                    containedTableModel);
                            // Refresh the main victims table
                            parentWindow.refreshTable(victims, locations);
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
            addDietPreferenceFrame.setLocationRelativeTo(null);
            addDietPreferenceFrame.setVisible(true);
        });
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedPreferenceRow = containedTable.getSelectedRow();
                
                if (selectedVictim != null && selectedPreferenceRow != -1) {
                    String preferenceName = containedTable.getValueAt(selectedPreferenceRow, 0).toString();
                    DietaryRestrictions.DietaryRestriction preference = DietaryRestrictions.DietaryRestriction.valueOf(preferenceName);
                    // Remove the dietary restriction from the victim's preferences
                    selectedVictim.removeDietaryPreference(preference);
                    // Refresh the dietary restrictions table
                    refreshDietaryRestrictionsTable(selectedVictim.getDietaryPreference(), containedTableModel);
                    // Refresh the main victims table
                    parentWindow.refreshTable(victims, locations);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a victim and a dietary restriction to remove.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Add the button to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton, BorderLayout.NORTH);
        buttonPanel.add(removeButton, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Refreshes the dietary restrictions table with updated data.
     *
     * @param preferences   The list of dietary preferences to display.
     * @param containedTable    The table model for the dietary restrictions table.
     */
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
