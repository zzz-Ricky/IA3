/**
 * The LocationManagementPage class represents a UI panel for managing supplies at a specific location.
 * It extends the JPanel class and provides functionality to view, add, and update supplies.
 * <p>
 * This class displays a table of supplies, allowing users to view the current inventory at the specified location.
 * Users can also add new supplies by entering the supply type and quantity through a popup window.
 * </p>
 * <p>
 * The class is designed to work with a Location object representing the work location where the supplies are managed.
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
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LocationManagementPage extends JPanel {
    private DefaultTableModel inventoryTableModel;
    private JTable inventoryTable;

    public LocationManagementPage(Location workLocation) {
        setLayout(new BorderLayout());

        // Create table model for Inventory
        inventoryTableModel = new DefaultTableModel();
        inventoryTableModel.addColumn("Type");
        inventoryTableModel.addColumn("Quantity");
        inventoryTable = new JTable(inventoryTableModel);

        // Initialize row sorter for inventory table
        TableRowSorter<DefaultTableModel> inventorySorter = new TableRowSorter<>(inventoryTableModel);
        inventoryTable.setRowSorter(inventorySorter);

        // Populate the table with supplies from workLocation
        updateTable(workLocation);

        // Add the table to the layout
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);
        add(inventoryScrollPane, BorderLayout.CENTER);

        // Create and add "Add Supply" button
        JButton addSupplyButton = new JButton("Add Supply");
        addSupplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create popup window for adding supply
                JFrame addSupplyFrame = new JFrame("Add Supply");
                addSupplyFrame.setSize(400, 200);
                addSupplyFrame.setLayout(new GridLayout(0, 2));
                addSupplyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JTextField supplyTypeField = new JTextField();
                JTextField quantityField = new JTextField();
                JButton saveButton = new JButton("Save");

                addSupplyFrame.add(new JLabel("Supply Type:"));
                addSupplyFrame.add(supplyTypeField);
                addSupplyFrame.add(new JLabel("Quantity:"));
                addSupplyFrame.add(quantityField);
                addSupplyFrame.add(saveButton);

             // Action listener for the save button
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String type = supplyTypeField.getText();
                        String quantityString = quantityField.getText();
                        // Check if input is whitespace
                        if (type.trim().isEmpty() || quantityString.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(LocationManagementPage.this, "Input cannot be whitespace.");
                            return; // Exit the method
                        }
                        try {
                            int quantity = Integer.parseInt(quantityString);
                            // Add the supply to the location
                            Supply newSupply = new Supply(type, quantity);
                            workLocation.addSupply(newSupply);
                            updateTable(workLocation);
                            addSupplyFrame.dispose(); // Close the popup window
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(LocationManagementPage.this, "Invalid quantity. Please enter a number.");
                        }
                    }
                });


                addSupplyFrame.setLocationRelativeTo(LocationManagementPage.this); // Center the popup relative to the main window
                addSupplyFrame.setVisible(true);
            }
        });
        add(addSupplyButton, BorderLayout.SOUTH);
    }
    
    /**
     * Updates the table with the supplies from the specified location.
     * <p>
     * This method clears the existing data in the inventory table model and populates the table
     * with the supplies retrieved from the given work location. Each row in the table represents
     * a supply, with columns for the supply type and quantity.
     * </p>
     * <p>
     * After updating the table, the UI is refreshed to reflect the changes.
     * </p>
     *
     * @param workLocation The location from which to retrieve the supplies to populate the table.
     */
    // Method to update the table with supplies from a location
    private void updateTable(Location workLocation) {
        inventoryTableModel.setRowCount(0); // Clear previous data
        for (Supply supply : workLocation.getSupplies()) {
            inventoryTableModel.addRow(new Object[]{supply.getDescription(), supply.getQuantity()});
        }
    }
}
