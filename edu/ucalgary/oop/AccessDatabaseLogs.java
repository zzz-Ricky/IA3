package edu.ucalgary.oop; /* package declaration */

import java.sql.*;
import java.util.ArrayList;

public class AccessDatabaseLogs implements ExternalFileIO {
    // Database credentials
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DBURL = "jdbc:mysql://localhost/your_database";
    static final String USERNAME = "username";
    static final String PASSWORD = "password";
    static Connection dbConnect = null;

    public void mountFile() {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void dismountFile() {
        try {
            if (dbConnect != null) {
                dbConnect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllInquiries() {
        Statement stmt = null;
        ArrayList<String> inquiries = new ArrayList<>();

        try {
            mountFile(); // Establish database connection

            // Execute a query
            stmt = dbConnect.createStatement();
            String sql = "SELECT INQUIRER.firstName, INQUIRER.lastName, INQUIRY_LOG.callDate, INQUIRY_LOG.details " +
                    "FROM INQUIRER " +
                    "INNER JOIN INQUIRY_LOG ON INQUIRER.id = INQUIRY_LOG.inquirer";
            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String callDate = rs.getString("callDate");
                String details = rs.getString("details");
                String inquiry = firstName + " " + (lastName != null ? lastName : "") + " - " + callDate + " - "
                        + details;
                inquiries.add(inquiry);
            }

            // Clean-up environment
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            dismountFile(); // Close database connection
        }

        return inquiries;
    }

    public static void addInquirer(String firstName, String lastName, String phoneNumber) {
        // Add logic to add an inquirer
    }

    public static void addInquiryLog(int inquirerId, String callDate, String details) {
        // Add logic to add an inquiry log
    }
}
