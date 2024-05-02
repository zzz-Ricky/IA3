/**
 * The DisasterVictimPage class represents a UI tab that allows users to add, remove,and update Disaster Victim Data.
 * It extends the JPanel class.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The class provides methods to refresh tabular data and draw object data from cell information.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class DisasterVictimPage extends JPanel {
    private JTable victimTable;
    private DefaultTableModel tableModel;
    private ArrayList<Integer> immutableRows; // Cache for immutable rows. This is used to grey out rows

    /**
     * Constructs a new DisasterVictimPage with the specified parameters.
     * 
     * @param locations     The list of locations where the disaster victims are
     *                      located.
     * @param victims       The list of disaster victims.
     * @param workLocation  The location where the user works.
     * @param familyManager The FamilyRelationManager instance to manage family
     *                      relations.
     * @param supplyManager The SupplyManager instance to manage supplies.
     */
    public DisasterVictimPage(ArrayList<Location> locations, ArrayList<DisasterVictim> victims, Location workLocation,
            FamilyRelationManager familyManager, SupplyManager supplyManager) {
        setLayout(new BorderLayout());
        this.immutableRows = new ArrayList<>(); // Initialize immutableRows

        // Create table model with column names
        String[] columnNames = { "First Name", "Last Name", "Family Connections", "Gender Pronoun",
                "Date of Birth / Age", "Description",
                "Social ID", "Medical Records", "Entry Date", "Personal Belongings", "Dietary Preference", "Location" };
        tableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table model with data
        for (int i = 0; i < victims.size(); i++) {
            DisasterVictim victim = victims.get(i);
            Location victimLocation = getLocationOfVictim(victim, locations);
            Object[] rowData = { victim.getFirstName(), victim.getLastName(), victim.getFamilyConnections(),
                    victim.getGender(),
                    victim.getDateOfBirth_Age(), victim.getDescription(), victim.getAssignedSocialID(),
                    victim.getMedicalRecords(), victim.getEntryDate(), victim.getPersonalBelongings(),
                    victim.getDietaryPreference(), victimLocation.getName() };
            tableModel.addRow(rowData);

            // Cache immutable rows
            if (workLocation != null && !victimLocation.equals(workLocation)) {
                immutableRows.add(i);
            }
        }
        // Create the table
        victimTable = new JTable(tableModel) {
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                int modelColumn = convertColumnIndexToModel(column);
                // Make rows immutable for locations different from workLocation
                if (workLocation != null) {
                    String victimLocationName = (String) getValueAt(row, 11);
                    Location victimLocation = findLocationByName(victimLocationName, locations);
                    if (!victimLocation.equals(workLocation)) {
                        return null;
                    }
                }
                if (modelColumn == 3) { // Gender Pronoun column
                    JComboBox<String> comboBox = new JComboBox<>(
                            victims.get(row).getGenderOptions().toArray(new String[0]));
                    return new DefaultCellEditor(comboBox);
                }
                if (modelColumn == 11) { // Location column
                    JComboBox<String> comboBox = new JComboBox<>(getLocationNames(locations));
                    return new DefaultCellEditor(comboBox);
                }
                if (column == 2 || column == 6 || column == 7 || (column >= 8 && column < 11)) {
                    return null; // Return null to prevent text editing of the cell
                }

                return super.getCellEditor(row, column);
            }
        };
        TableRowSorter<DefaultTableModel> Sorter = new TableRowSorter<>(tableModel);
        victimTable.setRowSorter(Sorter);
        JScrollPane scrollPane = new JScrollPane(victimTable);

        // Add mouse listener to handle cell clicks
        victimTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Locate the mouse pointer and in turn, the selected cell
                int row = victimTable.rowAtPoint(e.getPoint());
                int col = victimTable.columnAtPoint(e.getPoint());
                String columnName = victimTable.getColumnName(col);
                Object victimLocationNameObject = victimTable.getValueAt(row, 11);
                String victimLocationName = (victimLocationNameObject != null) ? victimLocationNameObject.toString()
                        : "";
                // Pre-load the location of the victim. It is used to identify the victim and
                // their options within the popup window.
                Location victimLocation = findLocationByName(victimLocationName, locations);
                if (workLocation != null && !victimLocation.equals(workLocation)) {
                    return;
                }
                // Handle the different popup cases for each column.
                if (columnName.equals("Family Connections") || col == 2) {
                    // Create a FamilyConnectionsPopup
                    HashSet<FamilyRelation> familyConnections = victims.get(row).getFamilyConnections();
                    new FamilyConnectionsPopup(familyConnections, victimTable, tableModel, victims, familyManager,
                            locations,
                            DisasterVictimPage.this);
                } else if (columnName.equals("Medical Record") || col == 7) {
                    // Create a MedicalRecordsPopup
                    ArrayList<MedicalRecord> medicalRecords = victims.get(row).getMedicalRecords();
                    new MedicalRecordsPopup(medicalRecords, locations, victimTable, tableModel, victims,
                            DisasterVictimPage.this);
                } else if (columnName.equals("Personal Belongings") || col == 9) {
                    // Create a PersonalBelongingsPopup
                    HashSet<Supply> personalBelongings = victims.get(row).getPersonalBelongings();
                    new PersonalBelongingsPopup(personalBelongings, locations, victimTable, tableModel, victims,
                            supplyManager, DisasterVictimPage.this);
                } else if (columnName.equals("Dietary Restrictions") || col == 10) {
                    // Create a DietaryRestrictionsPopup
                    ArrayList<DietaryRestrictions> restrictions = victims.get(row).getDietaryPreference();
                    new DietaryRestrictionsPopup(restrictions, victimTable, tableModel, victims, locations,
                            DisasterVictimPage.this);
                }
            }
        });

        // Add table model listener to detect cell changes
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && column >= 0) {
                    // Get the updated value from the table model
                    Object updatedValue = tableModel.getValueAt(row, column);
                    // Update the corresponding DisasterVictim object
                    DisasterVictim victim = victims.get(row);
                    switch (column) {
                        case 0: // First Name column
                            victim.setFirstName((String) updatedValue);
                            break;
                        case 1: // Last Name column
                            victim.setLastName((String) updatedValue);
                            break;
                        case 3: // Gender Pronoun column
                            victim.setGender((String) updatedValue);
                            break;
                        case 4: // DOB/Age column
                            try {
                                // Attempt to set the Date of Birth
                                victim.setDateOfBirth((String) updatedValue);
                            } catch (IllegalArgumentException a) {
                                JOptionPane.showMessageDialog(DisasterVictimPage.this,
                                        "Invalid Date of Birth format. The entry was logged as an approxmiate age",
                                        "Warning", JOptionPane.ERROR_MESSAGE);
                                // Optionally, you can revert the cell value to its previous value
                                victim.setApproxAge((String) updatedValue);
                            }
                            break;

                        case 5: // Description column
                            victim.setDescription((String) updatedValue);
                            break;

                        case 6: // Social ID
                            tableModel.setValueAt(victim.getAssignedSocialID(), row, column);
                            JOptionPane.showMessageDialog(DisasterVictimPage.this,
                                    "Social ID is an immutable value. It cannot be changed. ", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            break;
                        case 11: // Location column
                            // Get the updated location name from the table model
                            String updatedLocationName = (String) updatedValue;
                            // Find the corresponding Location object
                            Location updatedLocation = findLocationByName(updatedLocationName, locations);
                            // Update the location of the victim
                            if (updatedLocation != null) {
                                // Remove the victim from the previous location
                                getLocationOfVictim(victim, locations).removeOccupant(victim);
                                // Add the victim to the new location
                                updatedLocation.addOccupant(victim);
                            }
                            break;

                    }
                }
            }
        });

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            // Initialize a new cell renderer capable of actually drawing grey table cells.
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                // Check if the cell should be rendered as immutable
                if (immutableRows.contains(row)) {
                    // Render the cell with a gray background
                    component.setBackground(Color.LIGHT_GRAY);
                    component.setForeground(Color.BLACK);
                } else {
                    // Render the cell with a java default (white) background
                    component.setBackground(table.getBackground());
                    component.setForeground(table.getForeground());
                }
                return component;
            }
        };

        // Loop that calls every cell in the main table. Immutable cells should never
        // change while at a single location so we only need to call it once
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            victimTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Button to create a new DisasterVictim object
        JButton addButton = new JButton("Add New Rescued Person");
        addButton.addActionListener(p -> {
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
                DisasterVictim newVictim = new DisasterVictim(firstName, entryDate);
                victims.add(newVictim);
                refreshTable(victims, locations);
                addVictimFrame.dispose(); // Close the window after adding the victim
            });

            // Add components to the frame
            addVictimFrame.add(new JLabel("First Name:"));
            addVictimFrame.add(firstNameField);
            addVictimFrame.add(new JLabel("Entry Date:"));
            addVictimFrame.add(entryDateField);
            addVictimFrame.add(saveButton);

            // Set the frame visible
            addVictimFrame.setLocationRelativeTo(null);
            addVictimFrame.setVisible(true);
        });

        // Button to remove a new DisasterVictim object
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = victimTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Retrieve the DisasterVictim object from the selected row
                    DisasterVictim victimToRemove = victims.get(selectedRow);
                    // Remove the DisasterVictim object from the victims ArrayList
                    victims.remove(victimToRemove);
                    // Hopefully, the garbage collector will handle the removed victim.
                    victimToRemove = null;
                    // Refresh the table with the updated data
                    refreshTable(victims, locations);
                } else {
                    JOptionPane.showMessageDialog(DisasterVictimPage.this, "Please select a row to remove.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton, BorderLayout.NORTH);
        buttonPanel.add(removeButton, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * A Method to get the location name of a victim
     * 
     * @param locations The list of all accessible locations.
     */
    // Method to get the location name of a victim
    private Location getLocationOfVictim(DisasterVictim victim, ArrayList<Location> locations) {
        for (Location location : locations) {
            if (location.getOccupants().contains(victim)) {
                return location;
            }
        }
        return null;
    }

    /**
     * A method to return location Names to populate the table
     * 
     * @param locations The list of all accessible locations.
     */
    // Method to get the names of all locations
    private String[] getLocationNames(ArrayList<Location> locations) {
        String[] locationNames = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locationNames[i] = locations.get(i).getName();
        }
        return locationNames;
    }

    /**
     * Finds a location object using a provided name String.
     * 
     * @param name      A known location name
     * @param locations The list of all accessible locations.
     */
    // Method to find a Location object by its name
    private Location findLocationByName(String name, ArrayList<Location> locations) {
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null; // If no location with the given name is found
    }

    /**
     * Refreshes the table with updated data.
     * 
     * @param victims   The updated list of disaster victims.
     * @param locations The list of locations where the disaster victims are
     *                  located.
     */
    public void refreshTable(ArrayList<DisasterVictim> victims, ArrayList<Location> locations) {
        // Clear the existing rows
        tableModel.setRowCount(0);
        // Populate the table model with updated data
        for (DisasterVictim victim : victims) {
            Object[] rowData = { victim.getFirstName(), victim.getLastName(), victim.getFamilyConnections(),
                    victim.getGender(),
                    victim.getDateOfBirth_Age(), victim.getDescription(), victim.getAssignedSocialID(),
                    victim.getMedicalRecords(), victim.getEntryDate(), victim.getPersonalBelongings(),
                    victim.getDietaryPreference(), getLocationOfVictim(victim, locations).getName() };
            tableModel.addRow(rowData);
        }
        // Notify the table of the changes
        tableModel.fireTableDataChanged();
    }

}
