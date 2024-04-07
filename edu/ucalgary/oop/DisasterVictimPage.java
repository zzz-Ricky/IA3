package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class DisasterVictimPage extends JPanel {
    private JTable victimTable;
    private DefaultTableModel tableModel;
    // Create a disasterVictim attribute to cache the selected row
    private DisasterVictim lastSelectedRow = null;

    public DisasterVictimPage() {
        setLayout(new BorderLayout());

        // Create Sample data to populate the initial page
        FamilyRelationManager familyManager = new FamilyRelationManager();
        SupplyManager supplyManager = new SupplyManager();

        Location location1 = new Location("Shelter A", "1234 Shelter Ave");
        Location location2 = new Location("Shelter B", "678 Park Street");

        Supply waterBottles = new Supply("Water Bottles", 99);
        Supply bandages = new Supply("Bandages", 99);
        Supply toiletPaper = new Supply("Toilet Paper", 4);
        location1.addSupply(waterBottles);
        location1.addSupply(toiletPaper);
        location2.addSupply(bandages);

        DisasterVictim samplevictim1 = new DisasterVictim("Freda", "2024-01-18");
        samplevictim1.setDateOfBirth("1987-05-21");
        samplevictim1.setLastName("Smith");
        DisasterVictim samplevictim2 = new DisasterVictim("George", "2024-02-14");
        samplevictim2.setDateOfBirth("1967-03-05");
        samplevictim2.setLastName("Waller");

        FamilyRelation relationship1 = new FamilyRelation(samplevictim1, "Father in law", samplevictim2, familyManager);
        samplevictim1.addFamilyConnection(relationship1, familyManager);

        ArrayList<DisasterVictim> victims = new ArrayList<DisasterVictim>();
        victims.add(samplevictim1);
        victims.add(samplevictim2);

        ArrayList<Location> shelters = new ArrayList<Location>();
        shelters.add(location1);
        shelters.add(location2);

        // Create table model with column names
        String[] columnNames = { "First Name", "Last Name", "Family Connections", "Gender Pronoun",
                "Date of Birth / Age", "Description",
                "Social ID", "Medical Records", "Entry Date", "Personal Belongings", "Dietary Preference" };
        tableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table model with data
        for (DisasterVictim victim : victims) {
            Object[] rowData = { victim.getFirstName(), victim.getLastName(), victim.getFamilyConnections(),
                    victim.getGender(),
                    victim.getDateOfBirth_Age(), victim.getDescription(), victim.getAssignedSocialID(),
                    victim.getMedicalRecords(), victim.getEntryDate(), victim.getPersonalBelongings(),
                    victim.getDietaryPreference() };
            tableModel.addRow(rowData);
        }

        // Create the table
        victimTable = new JTable(tableModel) {
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                int modelColumn = convertColumnIndexToModel(column);
                if (modelColumn == 3) { // Gender Pronoun column
                    JComboBox<String> comboBox = new JComboBox<>(
                            victims.get(row).getGenderOptions().toArray(new String[0]));
                    return new DefaultCellEditor(comboBox);
                }
                if (column == 2 || column == 6 || column == 7 || column >= 8) {
                    return null; // Return null to prevent editing of the cell
                }
                return super.getCellEditor(row, column);
            }
        };
        JScrollPane scrollPane = new JScrollPane(victimTable);

        // Add mouse listener to handle cell clicks
        victimTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = victimTable.rowAtPoint(e.getPoint());
                int col = victimTable.columnAtPoint(e.getPoint());
                Object value = victimTable.getValueAt(row, col);
                String columnName = victimTable.getColumnName(col);

                if (columnName.equals("Family Connections") || col == 2) {
                    HashSet<FamilyRelation> familyConnections = victims.get(row).getFamilyConnections();
                    new FamilyConnectionsPopup(familyConnections, victimTable, tableModel, victims, familyManager,
                            DisasterVictimPage.this);
                } else if (columnName.equals("Medical Record") || col == 7) {
                    ArrayList<MedicalRecord> medicalRecords = victims.get(row).getMedicalRecords();
                    new MedicalRecordsPopup(medicalRecords, shelters, victimTable, tableModel, victims,
                            DisasterVictimPage.this);
                } else if (columnName.equals("Personal Belongings") || col == 9) {
                    HashSet<Supply> personalBelongings = victims.get(row).getPersonalBelongings();
                    new PersonalBelongingsPopup(personalBelongings, shelters, victimTable, tableModel, victims,
                            supplyManager, DisasterVictimPage.this);
                } else if (columnName.equals("Dietary Restrictions") || col == 10) {
                    ArrayList<DietaryRestrictions> restrictions = victims.get(row).getDietaryPreference();
                    new DietaryRestrictionsPopup(restrictions, victimTable, tableModel, victims,
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

                    }
                }
            }
        });

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
                refreshTable(victims);
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

    public void refreshTable(ArrayList<DisasterVictim> victims) {
        // Clear the existing rows
        tableModel.setRowCount(0);
        // Populate the table model with updated data
        for (DisasterVictim victim : victims) {
            Object[] rowData = { victim.getFirstName(), victim.getLastName(), victim.getFamilyConnections(),
                    victim.getGender(),
                    victim.getDateOfBirth_Age(), victim.getDescription(), victim.getAssignedSocialID(),
                    victim.getMedicalRecords(), victim.getEntryDate(), victim.getPersonalBelongings(),
                    victim.getDietaryPreference() };
            tableModel.addRow(rowData);
        }
        // Notify the table of the changes
        tableModel.fireTableDataChanged();
    }

}
