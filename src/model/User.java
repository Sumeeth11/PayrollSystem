package model;

/**
 * Represents a user in the payroll system.
 * Can be either an 'admin' or an 'employee'.
 * Contains user credentials and related employee ID if applicable.
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String role;     // "admin" or "employee"
    private int employeeId;

    /**
     * Constructs a User object with given data.
     *
     * @param userId       Unique user ID.
     * @param username     User login username.
     * @param password     User login password.
     * @param role         User role ('admin' or 'employee').
     * @param employeeId   Associated employee ID (for 'employee' role).
     */
    public User(int userId, String username, String password, String role, int employeeId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.employeeId = employeeId;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
