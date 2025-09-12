package dao;

import model.Employee;
import util.DBConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Data Access Object (DAO) class to handle Employee-related database operations.
 */
public class EmployeeDAO {
    private final Connection con;

    /**
     * Constructor initializes database connection.
     */
    public EmployeeDAO() {
        con = DBConnection.getConnection();
    }

    /**
     * Adds a new employee to the database.
     *
     * @param emp Employee object containing employee details.
     */
    public void addEmployee(Employee emp) {
        String sql = "INSERT INTO employee (name, designation, basic_salary, hra, da, deductions, bonus, tax, net_salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            emp.calculateNetSalary();

            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getDesignation());
            stmt.setDouble(3, emp.getBasicSalary());
            stmt.setDouble(4, emp.getHra());
            stmt.setDouble(5, emp.getDa());
            stmt.setDouble(6, emp.getDeductions());
            stmt.setDouble(7, emp.getBonus());
            stmt.setDouble(8, emp.getTax());
            stmt.setDouble(9, emp.getNetSalary());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }


    /**
     * Retrieves all employees from the database.
     *
     * @return List of all Employee objects.
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Retrieves a specific employee by their ID.
     *
     * @param empId The ID of the employee to retrieve.
     * @return Employee object if found, otherwise null.
     */
    public Employee getEmployeeById(int empId) {
        String sql = "SELECT * FROM employee WHERE emp_id=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractEmployeeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employee by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a list of employees whose name matches (or partially matches) the given name.
     *
     * @param name Partial or full name to search.
     * @return List of matching Employee objects.
     */
    public List<Employee> getEmployeesByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE name LIKE ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(extractEmployeeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching employees by name: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Updates the details of an existing employee in the database.
     *
     * @param empId The ID of the employee to update.
     * @param emp   The Employee object containing updated details.
     */
    public void updateEmployee(int empId, Employee emp) {
        String sql = "UPDATE employee SET name = ?, designation = ?, basic_salary = ?, hra = ?, da = ?, deductions = ?, bonus = ?, tax = ?, net_salary = ? WHERE emp_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            emp.calculateNetSalary();

            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getDesignation());
            stmt.setDouble(3, emp.getBasicSalary());
            stmt.setDouble(4, emp.getHra());
            stmt.setDouble(5, emp.getDa());
            stmt.setDouble(6, emp.getDeductions());
            stmt.setDouble(7, emp.getBonus());
            stmt.setDouble(8, emp.getTax());
            stmt.setDouble(9, emp.getNetSalary());
            stmt.setInt(10, empId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
    }

    /**
     * Deletes an employee from the database.
     *
     * @param empId The ID of the employee to delete.
     */
    public boolean deleteEmployee(int empId) {
        String sql = "DELETE FROM employee WHERE emp_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;  // deletion failed
        }
    }


    /**
     * Generates a report of all employees printed in the console,
     * sorted by net salary in descending order.
     */
    public void generateReport() {
        List<Employee> employees = getAllEmployees();
        employees.sort((e1, e2) -> Double.compare(e2.getNetSalary(), e1.getNetSalary()));

        System.out.println("\nEmp ID | Name | Designation | Basic | HRA | DA | Deductions | Bonus | Tax | Net Salary");
        for (Employee e : employees) {
            System.out.printf("%d | %s | %s | %.2f | %.2f | %.2f | %.2f | %.2f | %.2f | %.2f%n",
                    e.getEmpId(), e.getName(), e.getDesignation(), e.getBasicSalary(), e.getHra(), e.getDa(),
                    e.getDeductions(), e.getBonus(), e.getTax(), e.getNetSalary());
        }
    }

    /**
     * Exports the employee report to a CSV file named "EmployeeReport.csv".
     */
    public void exportReportToCSV() {
        List<Employee> employees = getAllEmployees();
        try (PrintWriter pw = new PrintWriter(new FileWriter("EmployeeReport.csv"))) {
            pw.println("Emp ID,Name,Designation,Basic Salary,HRA,DA,Deductions,Bonus,Tax,Net Salary");
            for (Employee e : employees) {
                pw.printf("%d,%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",
                        e.getEmpId(), e.getName(), e.getDesignation(),
                        e.getBasicSalary(), e.getHra(), e.getDa(),
                        e.getDeductions(), e.getBonus(), e.getTax(), e.getNetSalary());
            }
        } catch (Exception e) {
            System.err.println("Error exporting report: " + e.getMessage());
        }
    }


    /**
     * Displays a detailed report for a single employee based on their ID.
     *
     * @param empId The employee ID.
     */
    public void generateReportForEmployee(int empId) {
        Employee emp = getEmployeeById(empId);
        if (emp != null) {
            System.out.printf("\n%-5s %-20s %-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s%n",
                    "ID", "Name", "Designation", "Basic", "HRA", "DA", "Deductions", "Bonus", "Tax", "Net Salary");

            System.out.printf("%-5d %-20s %-15s %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f%n",
                    emp.getEmpId(), emp.getName(), emp.getDesignation(), emp.getBasicSalary(), emp.getHra(),
                    emp.getDa(), emp.getDeductions(), emp.getBonus(), emp.getTax(), emp.getNetSalary());

            System.out.println("\nReport generated successfully!");
        } else {
            System.out.println("Model.Employee not found!");
        }
    }


    /**
     * Retrieves employees filtered by designation and salary range.
     *
     * @param designation Filter by designation (can be null or blank for no filter).
     * @param minSalary   Minimum net salary filter (-1 for no minimum filter).
     * @param maxSalary   Maximum net salary filter (-1 for no maximum filter).
     * @return List of filtered Employee objects.
     */
    public List<Employee> getFilteredEmployees(String designation, double minSalary, double maxSalary) {
        List<Employee> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM employee WHERE 1=1";
            if (designation != null && !designation.isBlank()) sql += " AND designation=?";
            if (minSalary >= 0) sql += " AND net_salary >= ?";
            if (maxSalary >= 0) sql += " AND net_salary <= ?";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                int index = 1;
                if (designation != null && !designation.isBlank()) stmt.setString(index++, designation);
                if (minSalary >= 0) stmt.setDouble(index++, minSalary);
                if (maxSalary >= 0) stmt.setDouble(index++, maxSalary);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(extractEmployeeFromResultSet(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching filtered employees: " + e.getMessage());
        }

        return list;
    }


    /**
     * Extracts an Employee object from the current row of the given ResultSet.
     *
     * @param rs The ResultSet positioned at the current row.
     * @return The Employee object.
     * @throws Exception If any SQL error occurs.
     */
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee emp = new Employee(
                rs.getInt("emp_id"),
                rs.getString("name"),
                rs.getString("designation"),
                rs.getDouble("basic_salary"),
                rs.getDouble("hra"),
                rs.getDouble("da"),
                rs.getDouble("deductions")
        );
        emp.setBonus(rs.getDouble("bonus"));
        emp.setTax(rs.getDouble("tax"));
        emp.calculateNetSalary();
        return emp;
    }
}
