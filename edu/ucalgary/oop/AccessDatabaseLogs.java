package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;

public class AccessDatabaseLogs implements ExternalFileIO {
    // Database credentials
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DBURL = "jdbc:postgresql://localhost/project";
    static final String USERNAME = "oop";
    static final String PASSWORD = "ucalgary";
    static Connection dbConnect;

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

    public ArrayList<String[]> getAllInquiries() {
        Statement stmt = null;
        ArrayList<String[]> inquiries = new ArrayList<>();

        try {
            mountFile(); // Establish database connection

            // Execute a query
            stmt = dbConnect.createStatement();
            String sql = "SELECT firstName, lastName, phoneNumber FROM INQUIRER";
            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String phoneNumber = rs.getString("phoneNumber");
                String[] inquiry = { firstName, (lastName != null ? lastName : ""), phoneNumber };
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

    public ArrayList<String[]> getAllInquiryLogs() {
        Statement stmt = null;
        ArrayList<String[]> logs = new ArrayList<>();

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
                String[] log = { firstName, (lastName != null ? lastName : ""), callDate, details };
                logs.add(log);
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

        return logs;
    }

    public void addInquirer(String firstName, String lastName, String phoneNumber) {
        PreparedStatement pstmt = null;

        try {
            mountFile(); // Establish database connection

            // Prepare a statement
            String sql = "INSERT INTO INQUIRER (firstName, lastName, phoneNumber) VALUES (?, ?, ?)";
            pstmt = dbConnect.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);

            // Execute the insert statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                dismountFile(); // Close database connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addInquiryLog(String firstName, String lastName, String callDate, String details) {
        PreparedStatement pstmt = null;

        try {
            mountFile(); // Establish database connection

            // Prepare a statement to get the inquirer ID
            String getIdSql = "SELECT id FROM INQUIRER WHERE firstName=? AND lastName=?";
            pstmt = dbConnect.prepareStatement(getIdSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();

            int inquirerId = -1;
            if (rs.next()) {
                inquirerId = rs.getInt("id");
            }
            rs.close();
            pstmt.close();

            if (inquirerId != -1) {
                // Prepare a statement to find the largest ID in the INQUIRY_LOG table
                String maxIdSql = "SELECT MAX(id) AS max_id FROM INQUIRY_LOG";
                pstmt = dbConnect.prepareStatement(maxIdSql);
                rs = pstmt.executeQuery();

                int maxId = 0;
                if (rs.next()) {
                    maxId = rs.getInt("max_id");
                }
                rs.close();
                pstmt.close();

                // Calculate the new ID
                int newId = maxId + 1;

                // Prepare a statement to insert the inquiry log with the new ID
                String sql = "INSERT INTO INQUIRY_LOG (id, inquirer, callDate, details) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
                pstmt = dbConnect.prepareStatement(sql);
                pstmt.setInt(1, newId);
                pstmt.setInt(2, inquirerId);
                pstmt.setString(3, callDate);
                pstmt.setString(4, details);

                // Execute the insert statement
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                dismountFile(); // Close database connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeInquirer(String firstName, String lastName, String phoneNumber) {
        PreparedStatement pstmt = null;

        try {
            mountFile(); // Establish database connection

            // Prepare a statement
            String sql = "DELETE FROM INQUIRER WHERE firstName=? AND lastName=? AND phoneNumber=?";
            pstmt = dbConnect.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);

            // Execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                dismountFile(); // Close database connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeInquiryLog(String firstName, String lastName, String details) {
        PreparedStatement pstmt = null;

        try {
            mountFile(); // Establish database connection

            // Find the inquirer ID based on the provided first and last names
            int inquirerId = findInquirerId(firstName, lastName, false);

            if (inquirerId != -1) { // If inquirer ID is found
                // Prepare a statement
                String sql = "DELETE FROM INQUIRY_LOG WHERE inquirer=? AND details=?";
                pstmt = dbConnect.prepareStatement(sql);
                pstmt.setInt(1, inquirerId);
                pstmt.setString(2, details);

                // Execute the delete statement
                pstmt.executeUpdate();
            } else {
                System.out.println("Inquirer not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                dismountFile(); // Close database connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to find the inquirer ID based on first and last names
    public int findInquirerId(String firstName, String lastName, boolean mountConnection) {
        int inquirerId = -1;
        try {
            if (mountConnection) {
                mountFile(); // Establish database connection
            }
            // Prepare a statement
            String sql = "SELECT id FROM INQUIRER WHERE firstName=? AND lastName=?";
            PreparedStatement pstmt = dbConnect.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // Check if the result set has any rows
            if (rs.next()) {
                // Retrieve the inquirer ID
                inquirerId = rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (mountConnection) {
                dismountFile(); // Close database connection
            }
        }
        return inquirerId;
    }

    public void updateInquirer(int inquirerId, String firstName, String lastName, String phoneNumber) {
        PreparedStatement pstmt = null;

        try {
            mountFile(); // Establish database connection

            // Prepare a statement to update the inquirer
            String sql = "UPDATE INQUIRER SET firstName=?, lastName=?, phoneNumber=? WHERE id=?";
            pstmt = dbConnect.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);
            pstmt.setInt(4, inquirerId);

            // Execute the update statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                dismountFile(); // Close database connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateInquiryLog(int inquirerId, String callDate, String details) {
        PreparedStatement pstmt = null;

        try {
            mountFile(); // Establish database connection

            // Prepare a statement to update the inquiry log
            String sql = "UPDATE INQUIRY_LOG SET callDate=TO_DATE(?, 'YYYY-MM-DD'), details=? WHERE inquirer=?";
            pstmt = dbConnect.prepareStatement(sql);
            pstmt.setString(1, callDate);
            pstmt.setString(2, details);
            pstmt.setInt(3, inquirerId);

            // Execute the update statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                dismountFile(); // Close database connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<String> readFile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeFile() {
        // TODO Auto-generated method stub

    }
}
