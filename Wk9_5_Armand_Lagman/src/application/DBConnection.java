package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// ==========================================
// DATABASE CONNECTION
// Handles the connection to MySQL
// ==========================================
public class DBConnection {

    // Database connection settings
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "class123";

    // Prevents creating objects of this class
    private DBConnection() {
    }

    // ==========================================
    // GET DATABASE CONNECTION
    // Establishes a connection to MySQL
    // ==========================================
    public static Connection getDBConnection() throws SQLException {

        try {
            // Load the MySQL JDBC driver
            Class.forName(DB_DRIVER);

            // Return the database connection
            return DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );

        } catch (ClassNotFoundException e) {

            // Print an error if the JDBC driver cannot be found
            e.printStackTrace();

            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}