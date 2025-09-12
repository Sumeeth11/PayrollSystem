package main;

public class Admin {
    // Main.Admin username and password
    private String username = "admin";
    private String password = "admin123";

    // Method to check login
    public boolean login(String user, String pass) {
        return username.equals(user) && password.equals(pass);
    }
}
