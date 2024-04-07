package edu.ucalgary.oop;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class PersonalBelongingsPopup {
    private DisasterVictimPage parentWindow;
    private DisasterVictim lastSelectedPerson;

    PersonalBelongingsPopup(HashSet<Supply> supplies, ArrayList<Location> shelters, JTable victimTable,
            DefaultTableModel tableModel, ArrayList<DisasterVictim> victims, SupplyManager supplyManager,
            DisasterVictimPage parentWindow) {
        this.parentWindow = parentWindow;

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
            // Open a new window prompting the user to create a new DisasterVictim object
            JFrame addSupplyFrame = new JFrame("Add New Personal Belonging");
            addSupplyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addSupplyFrame.setSize(400, 200);

            // Ensure locationComboBox is defined and initialized properly
            JComboBox<String> locationComboBox = new JComboBox<>();
            int selectedRow = victimTable.getSelectedRow();
            DisasterVictim selectedVictim = victims.get(selectedRow);
            
            for (Location location : shelters) {
                // Check if the selected person is among the occupants of this location
                if (location.getOccupants().contains(selectedVictim)) {
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

            // Ensure suppliesComboBox is defined and initialized properly
            JComboBox<String> suppliesComboBox = new JComboBox<>();
            // Get the selected location object
            Location selectedLocation = null;
            for (Location location : shelters) {
                if (location.getName().equals(selectedLocationName)) {
                    selectedLocation = location;
                    break;
                }
            }
            // Populate suppliesComboBox with supplies from the selected location
            if (selectedLocation != null) {
                for (Supply supply : selectedLocation.getSupplies()) {
                    suppliesComboBox.addItem(supply.getDescription() + ": " + supply.getQuantity());
                }
            }

            JTextField quantityField = new JTextField(20);
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(actionEvent -> {
                // Get user input
                String locationName = (String) locationComboBox.getSelectedItem();
                String supplyDescription = (String) suppliesComboBox.getSelectedItem();
                // Split the string based on the ":" symbol
                String[] parts = supplyDescription.split(":");
                // Take the first part, which is the supply description
                String actualDescription = parts[0].trim();
                int quantity = Integer.parseInt(quantityField.getText());

                // Find the selected location
                Location location = null;
                for (Location loc : shelters) {
                    if (loc.getName().equals(locationName)) {
                        location = loc;
                        break;
                    }
                }

                // Create a new Supply object
                Supply newSupply = new Supply(actualDescription, quantity);

                // Check if the location is valid
                if (location != null) {
                    if (selectedRow != -1) {
                        // check if a row is still selected. Done to remedy the row deselecting itself
                        DisasterVictim victim = victims.get(selectedRow);
                        // Add the new supply to the victim's personal belongings
                        victim.addPersonalBelonging(newSupply, location, supplyManager);
                        // Refresh the personal belongings table
                        refreshPersonalBelongingsTable(victim.getPersonalBelongings(), containedTableModel);
                        // Refresh the main victims table
                        parentWindow.refreshTable(victims, shelters);
                        // Close the window after adding the new personal belonging
                        addSupplyFrame.dispose();
                    } else {
                        if (lastSelectedPerson != null) {
                            lastSelectedPerson.addPersonalBelonging(newSupply, location, supplyManager);
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
        // Add the button to the frame
        frame.add(addButton, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

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
