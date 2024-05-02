/**
 * The PersonalBelongingsPopup class represents a popup window for managing the personal belongings of disaster victims.
 * It allows users to view, add, and remove personal belongings from a selected victim.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The popup window displays a table of existing personal belongings for the selected victim,
 * provides options to add new personal belongings, and remove existing ones.
 * </p>
 * <p>
 * The personal belongings are managed through interactions with a main window (parent window),
 * a table of victims, and a table model.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class PersonalBelongingsPopup {
    private DisasterVictim selectedVictim; // Store the selected victim
    private DisasterVictim lastSelectedPerson;
    private Location selectedLocation;
    private String selectedLocationName;

    /**
     * Constructs a FamilyConnectionsPopup object with the specified parameters.
     *
     * @param supplies      The set of existing supplies.
     * @param shelters      The set of all existing locations
     * @param victimTable   The table displaying victim information.
     * @param tableModel    The table model for victim information.
     * @param victims       The list of disaster victims.
     * @param supplyManager The manager for supplies.
     * @param parentWindow  The parent window.
     */
    PersonalBelongingsPopup(HashSet<Supply> supplies, ArrayList<Location> shelters, JTable victimTable,
            DefaultTableModel tableModel, ArrayList<DisasterVictim> victims, SupplyManager supplyManager,
            DisasterVictimPage parentWindow) {

        // Handle Personal Belongings
        JFrame frame = new JFrame("Personal Belongings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        // Create table model for contained objects
        DefaultTableModel containedTableModel = new DefaultTableModel();
        JTable containedTable = new JTable(containedTableModel);
        JScrollPane containedScrollPane = new JScrollPane(containedTable);

        // Add columns to the table model
        containedTableModel.addColumn("Type");
        containedTableModel.addColumn("Quantity");

        // Populate the table model with data
        for (Supply supply : supplies) {
            containedTableModel.addRow(new Object[] {
                    supply.getDescription(),
                    supply.getQuantity()
            });
        }

        // Add the table to the frame
        frame.add(containedScrollPane);

        JButton addButton = new JButton("Add New Personal Belonging");
        addButton.addActionListener(f -> {
            // Open a new window prompting the user to create a new Supply object
            JFrame addSupplyFrame = new JFrame("Add New Personal Belonging");
            addSupplyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addSupplyFrame.setSize(400, 200);

            // Ensure locationComboBox is defined and initialized properly
            int selectedRow = victimTable.getSelectedRow();
            if (selectedRow != -1) {
                DisasterVictim selectedVictim = victims.get(selectedRow);
                this.selectedVictim = selectedVictim; // Store the selected victim
            }

            for (Location location : shelters) {
                // Check if the selected person is among the occupants of this location
                if (location.getOccupants().contains(selectedVictim)) {
                    // Get the selected location object
                    this.selectedLocationName = location.getName();
                    this.selectedLocation = location;
                }
            }

            // Create a text field to display the selected location's name
            JTextField locationTextField = new JTextField(this.selectedLocationName);
            locationTextField.setEditable(false); // Set non-editable
            locationTextField.setBackground(Color.LIGHT_GRAY); // Grey out the field

            // Ensure suppliesComboBox is defined and initialized properly
            JComboBox<String> suppliesComboBox = new JComboBox<>();

            // Populate suppliesComboBox with supplies from the selected location
            if (this.selectedLocation != null) {
                for (Supply supply : this.selectedLocation.getSupplies()) {
                    suppliesComboBox.addItem(supply.getDescription() + ": " + supply.getQuantity());
                }
            }

            JTextField quantityField = new JTextField(20);
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(actionEvent -> {
                // Get user input
                String supplyDescription = (String) suppliesComboBox.getSelectedItem();
                // Split the string based on the ":" symbol
                String[] parts = supplyDescription.split(":");
                // Take the first part, which is the supply description
                String actualDescription = parts[0].trim();
                String quantityString = quantityField.getText();

                // Check if quantityString is empty or contains only whitespace
                if (quantityString.trim().isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Quantity cannot be empty.");
                    return;
                }

                int quantity = Integer.parseInt(quantityField.getText());

                if (quantity < 1) {
                    JOptionPane.showMessageDialog(frame, "Quantity cannot be 0 or less");
                    return;
                }

                // Create a new Supply object
                Supply newSupply = new Supply(actualDescription, quantity);

                // Check if the location is valid
                if (this.selectedLocation != null) {
                    if (selectedRow != -1) {
                        // check if a row is still selected. Done to remedy the row deselecting itself
                        this.selectedVictim = victims.get(selectedRow);
                        this.lastSelectedPerson = victims.get(selectedRow);
                        // Add the new supply to the victim's personal belongings
                        this.selectedVictim.addPersonalBelonging(newSupply, this.selectedLocation, supplyManager);
                        // Refresh the personal belongings table
                        refreshPersonalBelongingsTable(selectedVictim.getPersonalBelongings(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims, shelters);
                        // Close the window after adding the new personal belonging
                        addSupplyFrame.dispose();
                    } else {
                        if (lastSelectedPerson != null) {
                            lastSelectedPerson.addPersonalBelonging(newSupply, this.selectedLocation, supplyManager);
                            // Refresh the personal belongings table
                            refreshPersonalBelongingsTable(lastSelectedPerson.getPersonalBelongings(),
                                    containedTableModel);
                            // Refresh the main victims table
                            parentWindow.refreshTable(victims, shelters);
                            // Close the window after adding the new personal belonging
                            addSupplyFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Please select a row to add a new Supply Item.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(addSupplyFrame, "Selected location is not valid.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Add components to the frame
            GridLayout layout = new GridLayout(0, 2);
            addSupplyFrame.setLayout(layout);

            // Add components
            addSupplyFrame.add(new JLabel("Location Name:"));
            addSupplyFrame.add(locationTextField);
            addSupplyFrame.add(new JLabel("Supply Type:"));
            addSupplyFrame.add(suppliesComboBox);
            addSupplyFrame.add(new JLabel("Quantity:"));
            addSupplyFrame.add(quantityField);
            addSupplyFrame.add(saveButton);

            // Set the frame visible
            addSupplyFrame.setLocationRelativeTo(null);
            addSupplyFrame.setVisible(true);
        });
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = containedTable.getSelectedRow();

                if (selectedVictim != null && selectedRow != -1) {
                    String itemName = containedTable.getValueAt(selectedRow, 0).toString();
                    Supply supplyToRemove = null;
                    for (Supply supply : selectedVictim.getPersonalBelongings()) {
                        if (supply.getDescription().equals(itemName)) {
                            supplyToRemove = supply;
                            break;
                        }
                    }
                    if (supplyToRemove != null) {
                        // Remove the supply from the victim's personal belongings
                        selectedVictim.removePersonalBelonging(supplyToRemove);
                        // Refresh the personal belongings table
                        refreshPersonalBelongingsTable(selectedVictim.getPersonalBelongings(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims, shelters);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selected personal belonging not found.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a personal belonging to remove.",
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
     * Refreshes the supplies record table with updated data.
     *
     * @param supplies            The updated set of supplies.
     * @param containedTableModel The table model for family connections.
     */
    private void refreshPersonalBelongingsTable(HashSet<Supply> supplies, DefaultTableModel containedTable) {
        // Clear the existing rows
        containedTable.setRowCount(0);

        // Populate the table model with updated data
        for (Supply supply : supplies) {
            containedTable.addRow(new Object[] {
                    supply.getDescription(),
                    supply.getQuantity()
            });
        }
        // Notify the table of the changes
        containedTable.fireTableDataChanged();
    }
}
