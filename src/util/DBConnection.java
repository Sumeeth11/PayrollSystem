package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class to establish a database connection.
 * Provides a static method to get a reusable connection object.
 */
public class DBConnection {
    private static Connection connection;

    /**
     * Returns a database connection.
     * If connection does not exist, creates one using JDBC.
     *
     * @return Connection object for database interactions.
     */
    public static Connection getConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/payroll_db", "root", "Sumeeth@2003");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}

