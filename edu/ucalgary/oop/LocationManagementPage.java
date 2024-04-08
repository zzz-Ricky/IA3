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

    // Method to update the table with supplies from a location
    private void updateTable(Location workLocation) {
        inventoryTableModel.setRowCount(0); // Clear previous data
        for (Supply supply : workLocation.getSupplies()) {
            inventoryTableModel.addRow(new Object[]{supply.getDescription(), supply.getQuantity()});
        }
    }
}
