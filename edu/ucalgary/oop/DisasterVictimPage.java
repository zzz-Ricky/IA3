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

    public DisasterVictimPage() {
        setLayout(new BorderLayout());

        // Create Sample data to populate the initial page
        FamilyRelationManager familyManager = new FamilyRelationManager();
        SupplyManager supplyManager = new SupplyManager();
        
        DisasterVictim samplevictim1 = new DisasterVictim("Freda", "2024-01-18");
        samplevictim1.setDateOfBirth("1987-05-21");
        samplevictim1.setLastName("Smith");
        DisasterVictim samplevictim2 = new DisasterVictim("George", "2024-02-14");
        samplevictim2.setDateOfBirth("1967-03-05");
        samplevictim2.setLastName("Waller");
        FamilyRelation relationship1 = new FamilyRelation(samplevictim1, "Neighbors" ,samplevictim2, familyManager);
        samplevictim1.addFamilyConnection(relationship1, familyManager);
        
        ArrayList<DisasterVictim> victims = getDisasterVictims();
        victims.add(samplevictim1);
        victims.add(samplevictim2);

        // Create table model with column names
        String[] columnNames = {"First Name", "Last Name", "Family Connections", "Gender Pronoun", "Date of Birth / Age", "Description",
                "Social ID", "Medical Records", "Entry Date", "Personal Belongings", "Dietary Preference"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table model with data
        for (DisasterVictim victim : victims) {
            Object[] rowData = {victim.getFirstName(), victim.getLastName(), victim.getFamilyConnections(), victim.getGender(),
                    victim.getDateOfBirth_Age(), victim.getDescription(), victim.getAssignedSocialID(),
                    victim.getMedicalRecords(), victim.getEntryDate(), victim.getPersonalBelongings(),
                    victim.getDietaryPreference()};
            tableModel.addRow(rowData);
        }

        // Create the table
        victimTable = new JTable(tableModel) {
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                int modelColumn = convertColumnIndexToModel(column);
                if (modelColumn == 3) { // Gender Pronoun column
                    JComboBox<String> comboBox = new JComboBox<>(victims.get(row).getGenderOptions().toArray(new String[0]));
                    return new DefaultCellEditor(comboBox);
                }
                if (column == 2||column == 6||column>=8) {
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
                    HashSet<FamilyRelation> connections = (HashSet<FamilyRelation>) value;
                    for (FamilyRelation connection : connections) {
                        containedTableModel.addRow(new Object[]{
                                connection.getPersonOne().getFirstName(),
                                connection.getRelationshipTo(),
                                connection.getPersonTwo().getFirstName()
                        });
                    }
                    // Add the table to the frame
                    frame.add(containedScrollPane);
                    frame.setVisible(true);
                } else if (columnName.equals("Medical Record") || col == 7) {
                    // Handle Medical Record
                	JFrame frame = new JFrame("Medical Records");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(400, 300);

                    // Create table model for contained objects
                    DefaultTableModel containedTableModel = new DefaultTableModel();
                    JTable containedTable = new JTable(containedTableModel);
                    JScrollPane containedScrollPane = new JScrollPane(containedTable);

                    // Add columns to the table model
                    containedTableModel.addColumn("Location");
                    containedTableModel.addColumn("Treatment Details");
                    containedTableModel.addColumn("Date of Treatment");

                    // Populate the table model with data
                    ArrayList<MedicalRecord> records = (ArrayList<MedicalRecord>) value;
                    for (MedicalRecord record : records) {
                        containedTableModel.addRow(new Object[]{
                        		record.getLocation().getName(),
                        		record.getDescription(),
                        		record.getDate()
                        });
                    }
                    // Add the table to the frame
                    frame.add(containedScrollPane);
                    frame.setVisible(true);
                } else if (columnName.equals("Personal Belongings") || col == 9) {
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
                    HashSet<Supply> supplies = (HashSet<Supply>) value;
                    for (Supply supply : supplies) {
                        containedTableModel.addRow(new Object[]{
                        		supply.getDescription(),
                        		supply.getQuantity()
                        });
                    }
                    // Add the table to the frame
                    frame.add(containedScrollPane);
                    frame.setVisible(true);
                } else if (columnName.equals("Dietary Restrictions") || col == 10) {
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
                    ArrayList<DietaryRestrictions> restrictions = (ArrayList<DietaryRestrictions>) value;
                    for (DietaryRestrictions restriction : restrictions) {
                        containedTableModel.addRow(new Object[]{
                        		restriction.getRestriction()
                        });
                    }
                    // Add the table to the frame
                    frame.add(containedScrollPane);
                    frame.setVisible(true);
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
                                JOptionPane.showMessageDialog(DisasterVictimPage.this, "Invalid Date of Birth format. The entry was logged as an approxmiate age", "Warning", JOptionPane.ERROR_MESSAGE);
                                // Optionally, you can revert the cell value to its previous value
                                victim.setApproxAge((String) updatedValue);
                            }
                            break;
                            
                        case 5: // Description column
                            victim.setDescription((String) updatedValue);
                            break;
                            
                        case 6: // Social ID
                        	tableModel.setValueAt(victim.getAssignedSocialID(), row, column);
                        	JOptionPane.showMessageDialog(DisasterVictimPage.this, "Social ID is an immutable value. It cannot be changed. ", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        
                    }
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
                DisasterVictim newVictim = new DisasterVictim(firstName, entryDate);
                victims.add(newVictim);
                Object[] rowData = {newVictim.getFirstName(), newVictim.getLastName(), newVictim.getFamilyConnections(),
                        newVictim.getGender(), newVictim.getDateOfBirth_Age(), newVictim.getDescription(),
                        newVictim.getAssignedSocialID(), newVictim.getMedicalRecords(), newVictim.getEntryDate(),
                        newVictim.getPersonalBelongings(), newVictim.getDietaryPreference()};
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
