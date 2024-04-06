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

public class InquirySQLPage extends JPanel {
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
                String firstName = JOptionPane.showInputDialog(InquirySQLPage.this,
                        "Enter the new Inquirer's first name:");
                String lastName = JOptionPane.showInputDialog(InquirySQLPage.this,
                        "Enter the new Inquirer's last name:");
                String phoneNumber = JOptionPane.showInputDialog(InquirySQLPage.this,
                        "Enter the new Inquirer's phone number:");
                if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()
                        && phoneNumber != null && !phoneNumber.isEmpty()) {
                    // Add the new inquirer to the database and update the table
                    database.addInquirer(firstName, lastName, phoneNumber);
                    updateInquiryTable();
                } else {
                    JOptionPane.showMessageDialog(InquirySQLPage.this, "Please fill in all fields.");
                }
            }
        });

        // Add button to add new inquiry log
        JButton addInquiryLogButton = new JButton("Add Inquiry Log");
        addInquiryLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = JOptionPane.showInputDialog(InquirySQLPage.this, "Enter the Inquirer's first name:");
                String lastName = JOptionPane.showInputDialog(InquirySQLPage.this, "Enter the Inquirer's last name:");
                String callDate = JOptionPane.showInputDialog(InquirySQLPage.this, "Enter the call date (YYYY-MM-DD):");
                String details = JOptionPane.showInputDialog(InquirySQLPage.this,
                        "Enter details: (Name of Person to search for)");

                if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty() &&
                        callDate != null && !callDate.isEmpty() && details != null && !details.isEmpty()) {

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
}
