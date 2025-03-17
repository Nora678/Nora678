import javax.swing.*;
import java.sql.*;
import java.sql.SQLException;
import java.sql.SQLDataException;

public class connect {
    private static final String URL = "jdbc:mysql://localhost:3306/phonebook";
    private static final String USER = "root";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addContact(String firstName, String lastName, String phone, String email, String address, String gender) {
        String query = "INSERT INTO contacts (firstName, lastName, phone, email, address, gender) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, address);
            pstmt.setString(6, gender);
            pstmt.executeUpdate();
        } catch (SQLDataException e) {
            System.err.println("Error adding contact - Data Truncated: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error adding contact: " + e.getMessage());
        }
    }

    public static void deleteContact(int id) {
        String query = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLDataException e) {
            System.err.println("Error deleting contact - Data Truncated: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error deleting contact: " + e.getMessage());
        }
    }

    public static ResultSet getAllContacts() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM contacts");
    }

    public static void updateContactColumn(int id, String column, String value) {

        if (column.equals("gender") && !value.equals("Male") && !value.equals("Female") && !value.equals("Other")) {

            JOptionPane.showMessageDialog(null, "Invalid gender value! Allowed values: Male, Female, Other", "Validation Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Invalid gender value");
        }

        String query = "UPDATE contacts SET " + column + " = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, value);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLDataException e) {
            // Specific exception for data truncation
            System.err.println("Error updating contact - Data Truncated: " + e.getMessage());
        } catch (SQLException e) {
            // General SQLException for other DB issues
            e.printStackTrace();
            System.err.println("Error updating contact: " + e.getMessage());
        }
    }
}
