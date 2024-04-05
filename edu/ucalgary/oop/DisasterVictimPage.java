package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
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
        
        FamilyRelation relationship1 = new FamilyRelation(samplevictim1, "Neighbors" ,samplevictim2, familyManager);
        samplevictim1.addFamilyConnection(relationship1, familyManager);
        
        ArrayList<DisasterVictim> victims = new ArrayList<DisasterVictim>();
        victims.add(samplevictim1);
        victims.add(samplevictim2);
        
        ArrayList<Location> shelters = new ArrayList<Location>();
        shelters.add(location1);
        shelters.add(location2);

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
                    
                    // Add the button to the frame
                    JButton addButton = new JButton("Add New Family Connection");
                    addButton.addActionListener(f -> {
                        // Open a new window prompting the user to create a new DisasterVictim object
                        JFrame addConnectionFrame = new JFrame("Add New Family Connection");
                        addConnectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        addConnectionFrame.setSize(300, 200);
                        addConnectionFrame.setLayout(new FlowLayout());

                        // Input fields for the new DisasterVictim object
                        JTextField firstIDField = new JTextField(20);
                        JTextField relationField = new JTextField(20);
                        JTextField secondIDField = new JTextField(20);
                        JButton saveButton = new JButton("Save");
                            int selectedRow = victimTable.getSelectedRow();
                            if (selectedRow != -1) {
                                // Retrieve the ID of the person from the corresponding row
                                int personID = (int) tableModel.getValueAt(selectedRow, 6);
                                firstIDField.setText(String.valueOf(personID));
                                firstIDField.setEditable(false); // Set non-editable
                                firstIDField.setBackground(Color.LIGHT_GRAY); // Grey out the field
                            } else {
                                JOptionPane.showMessageDialog(frame, "Please select a row to add a new family connection.", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        // Action listener for the save button
                        saveButton.addActionListener(actionEvent -> {
                            
                            // Create a new DisasterVictim object and add it to the table
                            int id1 = Integer.parseInt(firstIDField.getText());
                            String relation = relationField.getText();
                            int id2 = Integer.parseInt(secondIDField.getText());

                            DisasterVictim person1 = null;
                            DisasterVictim person2 = null;

                            // Find the DisasterVictim objects corresponding to the provided IDs
                            for (DisasterVictim victim : victims) {
                                if (victim.getAssignedSocialID() == (id1)) {
                                    person1 = victim;
                                }
                                if (victim.getAssignedSocialID() == (id2)) {
                                    person2 = victim;
                                }
                                // Break the loop if both persons are found
                                if (person1 != null && person2 != null) {
                                    break;
                                }
                            }

                            // If either person is not found, handle the error appropriately
                            if (person1 == null || person2 == null) {
                                JOptionPane.showMessageDialog(addConnectionFrame, "One or both persons not found.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Create a new FamilyRelation object and add it to the list
                            FamilyRelation newRelation = new FamilyRelation(person1, relation, person2, familyManager);
                            person1.addFamilyConnection(newRelation, familyManager);
                            refreshFamilyRelationTable(person1.getFamilyConnections(),containedTableModel);
                            refreshTable(victims);
                            addConnectionFrame.dispose(); // Close the window after adding the new family relation
                        });

                        // Add components to the frame
                        
                        GridLayout layout = new GridLayout(0, 2);
                        addConnectionFrame.setLayout(layout);
                        
                        addConnectionFrame.add(new JLabel("ID of Person A:"));
                        addConnectionFrame.add(firstIDField);
                        addConnectionFrame.add(new JLabel("Relationship Type:"));
                        addConnectionFrame.add(relationField);
                        addConnectionFrame.add(new JLabel("ID of Person B:"));
                        addConnectionFrame.add(secondIDField);
                        addConnectionFrame.add(saveButton);

                        // Set the frame visible
                        addConnectionFrame.setVisible(true);
                    });
                    frame.add(addButton, BorderLayout.NORTH);
                    frame.add(containedScrollPane);
                    frame.setVisible(true);
                	}
                    else if (columnName.equals("Medical Record") || col == 7) {
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

                        JButton addButton = new JButton("Add New Medical Record");
                        addButton.addActionListener(f -> {
                            // Open a new window prompting the user to create a new DisasterVictim object
                            JFrame addMedicalFrame = new JFrame("Add New Medical Record");
                            addMedicalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            addMedicalFrame.setSize(400, 200);
                            addMedicalFrame.setLayout(new FlowLayout());

                            // Input fields for the new DisasterVictim object
                            JComboBox<String> locationComboBox = new JComboBox<>();
                            for (Location location : shelters) {
                                locationComboBox.addItem(location.getName());
                            }
                            JTextField treatmentField = new JTextField(20);
                            JTextField dateField = new JTextField(20);
                            JButton saveButton = new JButton("Save");

                            // Action listener for the save button
                            saveButton.addActionListener(actionEvent -> {
                                // Create a new MedicalRecord object and add it to the corresponding victim
                                String locationName = (String) locationComboBox.getSelectedItem();
                                String treatment = treatmentField.getText();
                                String date = dateField.getText();

                                Location location = null;
                                for (Location loc : shelters) {
                                    if (loc.getName().equals(locationName)) {
                                        location = loc;
                                        break;
                                    }
                                }

                                if (location != null) {
                                    int selectedRow = victimTable.getSelectedRow();
                                    if (selectedRow != -1) {
                                        DisasterVictim victim = victims.get(selectedRow);
                                        MedicalRecord newMedicalRecord = new MedicalRecord(location, treatment, date);
                                        victim.addMedicalRecord(newMedicalRecord);
                                        refreshMedicalRecordTable(victim.getMedicalRecords(),containedTableModel);
                                        refreshTable(victims);
                                        addMedicalFrame.dispose(); // Close the window after adding the new medical record
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Please select a row to add a new Medical Record Item.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(addMedicalFrame, "Selected location is not valid.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });

                            // Add components to the frame
                            GridLayout layout = new GridLayout(0, 2);
                            addMedicalFrame.setLayout(layout);

                            // Add components
                            addMedicalFrame.add(new JLabel("Location Name:"));
                            addMedicalFrame.add(locationComboBox);
                            addMedicalFrame.add(new JLabel("Treatment Details:"));
                            addMedicalFrame.add(treatmentField);
                            addMedicalFrame.add(new JLabel("Treatment Date:"));
                            addMedicalFrame.add(dateField);
                            addMedicalFrame.add(saveButton);

                            // Set the frame visible
                            addMedicalFrame.setVisible(true);
                        });

                        frame.add(addButton, BorderLayout.NORTH);
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

                        JButton addButton = new JButton("Add New Personal Belonging");
                        addButton.addActionListener(f -> {
                            // Open a new window prompting the user to create a new DisasterVictim object
                            JFrame addSupplyFrame = new JFrame("Add New Personal Belonging");
                            addSupplyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            addSupplyFrame.setSize(400, 200);
                            addSupplyFrame.setLayout(new FlowLayout());

                            // Ensure locationComboBox is defined and initialized properly
                            JComboBox<String> locationComboBox = new JComboBox<>();
                            for (Location location : shelters) {
                                locationComboBox.addItem(location.getName());
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
                                    int selectedRow = victimTable.getSelectedRow();
                                    if (selectedRow != -1) {
                                        DisasterVictim victim = victims.get(selectedRow);
                                        // Add the new supply to the victim's personal belongings
                                        victim.addPersonalBelonging(newSupply, location, supplyManager);
                                        // Refresh the personal belongings table
                                        System.out.println(victim.getPersonalBelongings()); // Prints "Hello, world!" followed by a newline

                                        refreshPersonalBelongingsTable(victim.getPersonalBelongings(), containedTableModel);
                                        // Refresh the main victims table
                                        refreshTable(victims);
                                        // Close the window after adding the new personal belonging
                                        addSupplyFrame.dispose();
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Please select a row to add a new Personal Belonging Item.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(addSupplyFrame, "Selected location is not valid.", "Error", JOptionPane.ERROR_MESSAGE);
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
                            addSupplyFrame.setVisible(true);
                        });
                        // Add the button to the frame
                        frame.add(addButton, BorderLayout.NORTH);
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
                    JButton addButton = new JButton("Add New Dietary Restriction");
                    addButton.addActionListener(f -> {
                        // Open a new window prompting the user to create a new DisasterVictim object
                        JFrame addDietPreferenceFrame = new JFrame("Add New Dietary Preference");
                        addDietPreferenceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        addDietPreferenceFrame.setSize(300, 200);
                        addDietPreferenceFrame.setLayout(new FlowLayout());

                        // Input fields for the new DisasterVictim object
                        JTextField firstIDField = new JTextField(20);
                        JTextField relationField = new JTextField(20);
                        JTextField secondIDField = new JTextField(20);
                        JButton saveButton = new JButton("Save");
                            int selectedRow = victimTable.getSelectedRow();
                            if (selectedRow != -1) {
                                // Retrieve the ID of the person from the corresponding row
                                int personID = (int) tableModel.getValueAt(selectedRow, 6);
                                firstIDField.setText(String.valueOf(personID));
                                firstIDField.setEditable(false); // Set non-editable
                                firstIDField.setBackground(Color.LIGHT_GRAY); // Grey out the field
                            } else {
                                JOptionPane.showMessageDialog(frame, "Please select a row to add a new family connection.", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        // Action listener for the save button
                        saveButton.addActionListener(actionEvent -> {
                            
                            // Create a new DisasterVictim object and add it to the table
                            int id1 = Integer.parseInt(firstIDField.getText());
                            String relation = relationField.getText();
                            int id2 = Integer.parseInt(secondIDField.getText());

                            DisasterVictim person1 = null;
                            DisasterVictim person2 = null;

                            // Find the DisasterVictim objects corresponding to the provided IDs
                            for (DisasterVictim victim : victims) {
                                if (victim.getAssignedSocialID() == (id1)) {
                                    person1 = victim;
                                }
                                if (victim.getAssignedSocialID() == (id2)) {
                                    person2 = victim;
                                }
                                // Break the loop if both persons are found
                                if (person1 != null && person2 != null) {
                                    break;
                                }
                            }

                            // If either person is not found, handle the error appropriately
                            if (person1 == null || person2 == null) {
                                JOptionPane.showMessageDialog(addDietPreferenceFrame, "One or both persons not found.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Create a new FamilyRelation object and add it to the list
                            FamilyRelation newRelation = new FamilyRelation(person1, relation, person2, familyManager);
                            person1.addFamilyConnection(newRelation, familyManager);
                            refreshFamilyRelationTable(person1.getFamilyConnections(),containedTableModel);
                            refreshTable(victims);
                            addDietPreferenceFrame.dispose(); // Close the window after adding the new family relation
                        });

                        // Add components to the frame
                        GridLayout layout = new GridLayout(0, 2);
                        addDietPreferenceFrame.setLayout(layout);
                        
                        addDietPreferenceFrame.add(new JLabel("ID of Person A:"));
                        addDietPreferenceFrame.add(firstIDField);
                        addDietPreferenceFrame.add(new JLabel("Relationship Type:"));
                        addDietPreferenceFrame.add(relationField);
                        addDietPreferenceFrame.add(new JLabel("ID of Person B:"));
                        addDietPreferenceFrame.add(secondIDField);
                        addDietPreferenceFrame.add(saveButton);

                        // Set the frame visible
                        addDietPreferenceFrame.setVisible(true);
                    });
                    frame.add(addButton, BorderLayout.NORTH);
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
    
    private void refreshTable(ArrayList<DisasterVictim> victims) {
        // Clear the existing rows
        tableModel.setRowCount(0);
        // Populate the table model with updated data
        for (DisasterVictim victim : victims) {
            Object[] rowData = {victim.getFirstName(), victim.getLastName(), victim.getFamilyConnections(), victim.getGender(),
                    victim.getDateOfBirth_Age(), victim.getDescription(), victim.getAssignedSocialID(),
                    victim.getMedicalRecords(), victim.getEntryDate(), victim.getPersonalBelongings(),
                    victim.getDietaryPreference()};
            tableModel.addRow(rowData);
        }
        // Notify the table of the changes
        tableModel.fireTableDataChanged();
    }
    
    private void refreshFamilyRelationTable(HashSet<FamilyRelation> connections, DefaultTableModel containedTable) {
        // Clear the existing rows
    	containedTable.setRowCount(0);
        
        // Populate the table model with updated data
        for (FamilyRelation connection : connections) {
        	containedTable.addRow(new Object[]{
                    connection.getPersonOne().getFirstName(),
                    connection.getRelationshipTo(),
                    connection.getPersonTwo().getFirstName()
            });
        }
        // Notify the table of the changes
        containedTable.fireTableDataChanged();
    }
    
    private void refreshMedicalRecordTable(ArrayList<MedicalRecord> records, DefaultTableModel containedTable) {
        // Clear the existing rows
    	containedTable.setRowCount(0);
        
        // Populate the table model with updated data
        for (MedicalRecord record : records) {
        	containedTable.addRow(new Object[]{
                    record.getLocation().getName(),
                    record.getDescription(),
                    record.getDate()
            });
        }
        // Notify the table of the changes
        containedTable.fireTableDataChanged();
    }
    
    private void refreshPersonalBelongingsTable(HashSet<Supply> supplies, DefaultTableModel containedTable) {
        // Clear the existing rows
    	containedTable.setRowCount(0);
        
        // Populate the table model with updated data
        for (Supply supply : supplies) {
        	containedTable.addRow(new Object[]{
        			supply.getDescription(),
        			supply.getQuantity()
            });
        }
        // Notify the table of the changes
        containedTable.fireTableDataChanged();
    }

}
