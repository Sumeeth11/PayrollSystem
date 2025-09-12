package util;

import java.sql.Connection;

/**
 * Test class to verify if the database connection is established successfully.
 */
public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Failed to connect.");
        }
    }
}
