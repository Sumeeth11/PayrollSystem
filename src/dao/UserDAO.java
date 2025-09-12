package dao;

import model.User;
import util.DBConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data Access Object (DAO) class to handle User-related database operations.
 */
public class UserDAO {
    private Connection con;
    /**
     * Constructor initializes database connection.
     */
    public UserDAO() {

        con = DBConnection.getConnection();
    }
    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username input by the user.
     * @param password The password input by the user.
     * @return User object if login is successful, null otherwise.
     */
    public User login(String username, String password) {
        User user = null;

        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("employee_id")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
