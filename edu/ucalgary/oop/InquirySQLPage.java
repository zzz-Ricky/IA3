package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class InquirySQLPage extends JPanel implements DateManageMent {
    private DefaultTableModel inquiryTableModel;
    private JTable inquiryTable;
    private DefaultTableModel logTableModel;
    private JTable logTable;
    private AccessDatabaseLogs database;
    private JButton saveEditsButton;
    private JButton discardEditsButton;
    private JButton removeButton;

    public InquirySQLPage() {
        database = new AccessDatabaseLogs();

        setLayout(new BorderLayout());

        // Create table model for INQUIRER
        inquiryTableModel = new DefaultTableModel();
        inquiryTableModel.addColumn("First Name");
        inquiryTableModel.addColumn("Last Name");
        inquiryTableModel.addColumn("Phone Number");
        inquiryTable = new JTable(inquiryTableModel);

        // Create table model for INQUIRY_LOG
        logTableModel = new DefaultTableModel();
        logTableModel.addColumn("First Name");
        logTableModel.addColumn("Last Name");
        logTableModel.addColumn("Call Date");
        logTableModel.addColumn("Details");
        logTable = new JTable(logTableModel);

        // Initialize row sorter for inquiry table
        TableRowSorter<DefaultTableModel> inquirySorter = new TableRowSorter<>(inquiryTableModel);
        inquiryTable.setRowSorter(inquirySorter);

        // Initialize row sorter for log table
        TableRowSorter<DefaultTableModel> logSorter = new TableRowSorter<>(logTableModel);
        logTable.setRowSorter(logSorter);

        // Fetch inquiries and logs from database and add to tables
        updateInquiryTable();
        updateLogTable();

        // Create scroll panes for tables
        JScrollPane inquiryScrollPane = new JScrollPane(inquiryTable);
        JScrollPane logScrollPane = new JScrollPane(logTable);

        // Create panel to hold the scroll panes
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(createPanelWithHeading("Inquirers", inquiryScrollPane));
        panel.add(createPanelWithHeading("Inquiry Logs", logScrollPane));
        add(panel, BorderLayout.CENTER);

     // Add button to add new inquirer
        JButton addInquirerButton = new JButton("Add Inquirer");
        addInquirerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a custom dialog to input inquirer details
                JPanel panel = new JPanel(new GridLayout(0, 2));
                JTextField firstNameField = new JTextField();
                JTextField lastNameField = new JTextField();
                JTextField phoneNumberField = new JTextField();
                
                panel.add(new JLabel("First Name:"));
                panel.add(firstNameField);
                panel.add(new JLabel("Last Name:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Phone Number:"));
                panel.add(phoneNumberField);
                
                int result = JOptionPane.showConfirmDialog(InquirySQLPage.this, panel, "Add New Inquirer", JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    String firstName = firstNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String phoneNumber = phoneNumberField.getText().trim();
                    
                    if (!firstName.isEmpty() && !lastName.isEmpty() && !phoneNumber.isEmpty()) {
                        // Add the new inquirer to the database and update the table
                        database.addInquirer(firstName, lastName, phoneNumber);
                        updateInquiryTable();
                    } else {
                        JOptionPane.showMessageDialog(InquirySQLPage.this, "Please fill in all fields.");
                    }
                }
            }
        });


     // Add button to add new inquiry log
        JButton addInquiryLogButton = new JButton("Add Inquiry Log");
        addInquiryLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a custom dialog to input inquiry log details
                JPanel panel = new JPanel(new GridLayout(0, 2));
                JTextField firstNameField = new JTextField();
                JTextField lastNameField = new JTextField();
                JTextField callDateField = new JTextField();
                JTextField detailsField = new JTextField();
                
                panel.add(new JLabel("First Name:"));
                panel.add(firstNameField);
                panel.add(new JLabel("Last Name:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Call Date (YYYY-MM-DD):"));
                panel.add(callDateField);
                panel.add(new JLabel("Details:"));
                panel.add(detailsField);
                
                int result = JOptionPane.showConfirmDialog(InquirySQLPage.this, panel, "Add New Inquiry Log", JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    String firstName = firstNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String callDate = callDateField.getText().trim();
                    String details = detailsField.getText().trim();
                    
                    // Validate call date
                    try {
                        validateDate(callDate);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(InquirySQLPage.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Don't proceed if the date is invalid
                    }
                    
                    if (!firstName.isEmpty() && !lastName.isEmpty() && !callDate.isEmpty() && !details.isEmpty()) {
                        // Check if the provided first and last names relate to any existing inquirer
                        int inquirerId = database.findInquirerId(firstName, lastName, true);
                        if (inquirerId != -1) {
                            // Add the new inquiry log to the database and update the table
                            database.addInquiryLog(firstName, lastName, callDate, details);
                            updateLogTable();
                        } else {
                            JOptionPane.showMessageDialog(InquirySQLPage.this,
                                    "No inquirer found with the provided first and last names.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(InquirySQLPage.this, "Please fill in all fields.");
                    }
                }
            }
        });


        // Add button to remove selected row
        removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = inquiryTable.getSelectedRow();
                if (selectedRow != -1) {
                    // If the inquiry table is selected, remove the inquirer
                    String firstName = inquiryTableModel.getValueAt(selectedRow, 0).toString();
                    String lastName = inquiryTableModel.getValueAt(selectedRow, 1).toString();
                    String phoneNumber = inquiryTableModel.getValueAt(selectedRow, 2).toString();
                    database.removeInquirer(firstName, lastName, phoneNumber);
                    updateInquiryTable();
                } else {
                    // If the log table is selected, remove the inquiry log
                    selectedRow = logTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String firstName = logTableModel.getValueAt(selectedRow, 0).toString();
                        String lastName = logTableModel.getValueAt(selectedRow, 1).toString();
                        String details = logTableModel.getValueAt(selectedRow, 3).toString();
                        database.removeInquiryLog(firstName, lastName, details);
                        updateLogTable();
                    } else {
                        JOptionPane.showMessageDialog(InquirySQLPage.this, "Please select an item to remove.");
                    }
                }
            }
        });

        // Add text field for searching
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 30));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String searchText = searchField.getText().trim();
                // Perform search operation here
                searchInquiryTable(searchText);
                searchLogTable(searchText);
            }
        });

        // Add panel for buttons and search bar
        JPanel buttonSearchPanel = new JPanel(new FlowLayout());
        buttonSearchPanel.add(addInquirerButton);
        buttonSearchPanel.add(addInquiryLogButton);
        buttonSearchPanel.add(removeButton);
        buttonSearchPanel.add(searchField);
        add(buttonSearchPanel, BorderLayout.NORTH);

        // Enable cell selection
        inquiryTable.setCellSelectionEnabled(true);
        logTable.setCellSelectionEnabled(true);

        // Add panel for save/discard edits buttons (initially hidden)
        JPanel saveDiscardPanel = new JPanel(new GridLayout(1, 2));
        saveEditsButton = new JButton("Save Edits");
        discardEditsButton = new JButton("Discard Edits");
        saveEditsButton.setVisible(false);
        discardEditsButton.setVisible(false);
        saveDiscardPanel.add(saveEditsButton);
        saveDiscardPanel.add(discardEditsButton);
        add(saveDiscardPanel, BorderLayout.SOUTH);
    }

    // Method to create a panel with a heading title
    private JPanel createPanelWithHeading(String title, JScrollPane scrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Method to update the inquiry table
    private void updateInquiryTable() {
        inquiryTableModel.setRowCount(0);
        ArrayList<String[]> inquiries = database.getAllInquiries();
        for (String[] inquiry : inquiries) {
            inquiryTableModel.addRow(inquiry);
        }
    }

    // Method to update the log table
    private void updateLogTable() {
        logTableModel.setRowCount(0);
        ArrayList<String[]> logs = database.getAllInquiryLogs();
        for (String[] log : logs) {
            logTableModel.addRow(log);
        }
    }

    // Method to search the inquiry table
    private void searchInquiryTable(String searchText) {
        DefaultRowSorter<TableModel, Integer> sorter = (DefaultRowSorter<TableModel, Integer>) inquiryTable
                .getRowSorter();
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null); // Clear the filter
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    // Method to search the log table
    private void searchLogTable(String searchText) {
        DefaultRowSorter<TableModel, Integer> sorter = (DefaultRowSorter<TableModel, Integer>) logTable.getRowSorter();
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null); // Clear the filter
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inquiry Logs");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create tab pane
            JTabbedPane tabbedPane = new JTabbedPane();

            // Add InquirySQLPage to tab pane
            InquirySQLPage inquiryPage = new InquirySQLPage();
            tabbedPane.addTab("Inquiry Logs", inquiryPage);

            frame.add(tabbedPane);
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


	@Override
    public boolean validateDate(String date) {
		
        JFrame frame = new JFrame("ERROR");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
		
        // Check if the date has the date in the correct format such as "2024-01-18"
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date must be in the format YYYY-MM-DD");
        }

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        // Check if the date numbers are possible real times.
        if (month < 1 || month > 12 || day < 1 || day > 31 ||
                (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) ||
                (month == 2 && (day > 29 || (day > 28 && !(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)))))) {
            JOptionPane.showMessageDialog(frame,
                    "An Invalid date was given, must be a valid year, month, and day", "Error",
                    JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("An Invalid date was given, must be a valid year, month, and day");
        }

        return true;
    }
    
	@Override
	public void setDate(String newDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
