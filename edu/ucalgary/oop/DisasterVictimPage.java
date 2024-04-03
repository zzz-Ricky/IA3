package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class DisasterVictimPage extends JPanel {
    private JTable victimTable;
    private DefaultTableModel tableModel;

    public DisasterVictimPage() {
        setLayout(new BorderLayout());

        // Sample data - replace this with your actual data
        ArrayList<DisasterVictim> victims = getDisasterVictims();

        // Create table model with column names
        String[] columnNames = {"First Name", "Last Name", "Date of Birth / Age", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table model with data
        for (DisasterVictim victim : victims) {
            Object[] rowData = {victim.getFirstName(), victim.getLastName(), victim.getDateOfBirth_Age(), victim.getDescription()};
            tableModel.addRow(rowData);
        }

        // Create the table
        victimTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(victimTable);

        // Add mouse listener to handle cell clicks
        victimTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = victimTable.rowAtPoint(e.getPoint());
                int col = victimTable.columnAtPoint(e.getPoint());
                Object value = victimTable.getValueAt(row, col);

                // Check if clicked cell contains HashSet or ArrayList
                if (value instanceof HashSet || value instanceof ArrayList) {
                    // Open a new window with a separate table containing all contained objects
                    JFrame frame = new JFrame("Contained Objects");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(400, 300);

                    // Create table model for contained objects
                    DefaultTableModel containedTableModel = new DefaultTableModel();
                    JTable containedTable = new JTable(containedTableModel);
                    JScrollPane containedScrollPane = new JScrollPane(containedTable);

                    // Add columns to the table model
                    containedTableModel.addColumn("Contained Object");

                    // Populate the table model with data
                    if (value instanceof HashSet) {
                        HashSet<?> set = (HashSet<?>) value;
                        for (Object obj : set) {
                            containedTableModel.addRow(new Object[]{obj.toString()});
                        }
                    } else if (value instanceof ArrayList) {
                        ArrayList<?> list = (ArrayList<?>) value;
                        for (Object obj : list) {
                            containedTableModel.addRow(new Object[]{obj.toString()});
                        }
                    }

                    // Add the table to the frame
                    frame.add(containedScrollPane);
                    frame.setVisible(true);
                }
            }
        });

        // Button to create a new DisasterVictim object
        JButton addButton = new JButton("Add New Rescued Person");
        addButton.addActionListener(e -> {
            // Open a new window prompting the user to create a new DisasterVictim object
            JFrame addVictimFrame = new JFrame("Add New Rescued Person");
            addVictimFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addVictimFrame.setSize(300, 200);
            addVictimFrame.setLayout(new FlowLayout());

            // Input fields for the new DisasterVictim object
            JTextField firstNameField = new JTextField(20);
            JTextField entryDateField = new JTextField(20);
            JButton saveButton = new JButton("Save");

            // Action listener for the save button
            saveButton.addActionListener(actionEvent -> {
                // Create a new DisasterVictim object and add it to the table
                String firstName = firstNameField.getText();
                String entryDate = entryDateField.getText();
                System.out.println("DEBUG");
                DisasterVictim newVictim = new DisasterVictim(firstName, entryDate);
                Object[] rowData = {newVictim.getFirstName(), "", newVictim.getDateOfBirth_Age(), ""};
                tableModel.addRow(rowData);
                addVictimFrame.dispose(); // Close the window after adding the victim
            });

            // Add components to the frame
            addVictimFrame.add(new JLabel("First Name:"));
            addVictimFrame.add(firstNameField);
            addVictimFrame.add(new JLabel("Entry Date:"));
            addVictimFrame.add(entryDateField);
            addVictimFrame.add(saveButton);

            // Set the frame visible
            addVictimFrame.setVisible(true);
        });

        // Add components to the panel
        add(addButton, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Sample method to generate dummy data (replace with actual data retrieval)
    private ArrayList<DisasterVictim> getDisasterVictims() {
        // Populate with some dummy data
        ArrayList<DisasterVictim> victims = new ArrayList<>();
        // Add your logic to fetch actual data here
        return victims;
    }
}
