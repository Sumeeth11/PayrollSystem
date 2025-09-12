package main;

import dao.EmployeeDAO;
import dao.UserDAO;
import model.Employee;
import model.User;

import java.util.List;
import java.util.Scanner;

public class PayrollSystem {
    /**
     * Displays the main menu of the Employee Payroll System.
     *
     * For Admin role:
     * 1. Add Employee - Allows admin to add a new employee by entering details.
     * 2. View All Employees - Displays all employee records from the database.
     * 3. Update Employee - Enables admin to update an existing employee's details by Employee ID.
     * 4. Delete Employee - Allows admin to delete an employee from the database by Employee ID.
     * 5. Generate Report (Console) - Generates and displays a report of all employees on the console.
     * 6. Export Report to CSV - Exports the employee report into 'EmployeeReport.csv' file.
     * 7. Search Employee by ID or Name - Allows admin to search for employee(s) by Employee ID or Name.
     * 8. Exit - Exit the payroll application.
     *
     * For Employee role:
     * 1. View My Details - Displays logged-in employee's own details.
     * 2. Generate My Report - Displays a detailed report of the logged-in employee.
     * 3. Exit - Exit the payroll application.
     */

    public static void main(String[] args) {
        UserDAO userDao = new UserDAO();
        Scanner sc = new Scanner(System.in);
        User loggedInUser = null;
        int attempts = 0;

        while (loggedInUser == null && attempts < 3) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            loggedInUser = userDao.login(username, password);

            if (loggedInUser != null) {
                System.out.println("Login successful! Role: " + loggedInUser.getRole() + "\n");
            } else {
                attempts++;
                System.out.println("Invalid credentials. Try again.\n");
                if (attempts == 3) {
                    System.out.println("Too many failed attempts. Exiting.");
                    System.exit(0);
                }
            }
        }

        /**
         * Displays the main menu of the Employee Payroll System.
         * Admin and Employee have different menu options based on role.
         */
        EmployeeDAO dao = new EmployeeDAO();

        while (true) {
            System.out.println("\n===============================");
            System.out.println("    Employee Payroll System     ");
            System.out.println("===============================");
            if (loggedInUser.getRole().equals("admin")) {
                System.out.println("1. Add Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Generate Report (Console)");
                System.out.println("6. Export Report to CSV (EmployeeReport.csv)");
                System.out.println("7. Search Employee by ID or Name");
                System.out.println("8. Exit");
            } else if (loggedInUser.getRole().equals("employee")) {
                System.out.println("1. View My Details");
                System.out.println("2. Generate My Report");
                System.out.println("3. Exit");
            }
            System.out.println("===============================");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                /**
                 * (For Admin) Case 1 - Add Employee:
                 * Allows the admin to enter employee details (name, designation, salary components, bonus, tax).
                 * Then adds the employee record to the database.
                 * (For Employee) Case 1 - View My Details:
                 * Displays the logged-in employee’s personal and salary information in a neat format.
                 */
                case 1:
                    if (loggedInUser.getRole().equals("admin")) {
                        try {
                            System.out.print("Enter name: ");
                            String name = sc.nextLine();
                            while (name.isBlank()) {
                                System.out.println("Name cannot be empty! Enter again:");
                                name = sc.nextLine();
                            }

                            System.out.print("Enter designation: ");
                            String designation = sc.nextLine();
                            while (designation.isBlank()) {
                                System.out.println("Designation cannot be empty! Enter again:");
                                designation = sc.nextLine();
                            }

                            System.out.print("Enter basic salary: ");
                            double basic = sc.nextDouble();
                            while (basic < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                basic = sc.nextDouble();
                            }

                            System.out.print("Enter HRA: ");
                            double hra = sc.nextDouble();
                            while (hra < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                hra = sc.nextDouble();
                            }

                            System.out.print("Enter DA: ");
                            double da = sc.nextDouble();
                            while (da < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                da = sc.nextDouble();
                            }

                            System.out.print("Enter deductions: ");
                            double deductions = sc.nextDouble();
                            while (deductions < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                deductions = sc.nextDouble();
                            }

                            System.out.print("Enter Bonus: ");
                            double bonus = sc.nextDouble();
                            while (bonus < 0) {
                                System.out.println("Bonus cannot be negative! Enter again:");
                                bonus = sc.nextDouble();
                            }

                            System.out.print("Enter Tax: ");
                            double tax = sc.nextDouble();
                            while (tax < 0) {
                                System.out.println("Tax cannot be negative! Enter again:");
                                tax = sc.nextDouble();
                            }

                            sc.nextLine();

                            Employee emp = new Employee(name, designation, basic, hra, da, deductions);
                            emp.setBonus(bonus);
                            emp.setTax(tax);

                            dao.addEmployee(emp);

                            System.out.println("\nEmployee added successfully!");
                            System.out.println("Employee Name: " + emp.getName() + ", ID will be generated automatically.");

                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... Goodbye!");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }

                        } catch (Exception e) {
                            System.out.println("Error adding employee: " + e.getMessage());
                        }
                    } else if (loggedInUser.getRole().equals("employee")) {
                        Employee emp = dao.getEmployeeById(loggedInUser.getEmployeeId());
                        if (emp != null) {
                            System.out.printf("\n%-5s %-20s %-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                                    "ID", "Name", "Designation", "Basic", "HRA", "DA", "Deductions", "Bonus", "Tax", "Net Salary");
                            System.out.printf("%-5d %-20s %-15s %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f\n",
                                    emp.getEmpId(),
                                    emp.getName(),
                                    emp.getDesignation(),
                                    emp.getBasicSalary(),
                                    emp.getHra(),
                                    emp.getDa(),
                                    emp.getDeductions(),
                                    emp.getBonus(),
                                    emp.getTax(),
                                    emp.getNetSalary());

                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... ");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }

                        } else {
                            System.out.println("\nEmployee details not found!");
                        }
                    }
                    break;

                /**
                 * (For Admin) Case 2 - View All Employees:
                 * Displays all employees in a tabular format.
                 * Useful for admin to view the complete employee list.
                 * (For Employee) Case 2 - Generate My Report:
                 * Displays a detailed report of the logged-in employee’s salary, including bonus and tax.
                 */
                case 2:
                    if (loggedInUser.getRole().equals("admin")) {
                        try {
                            List<Employee> allEmployees = dao.getAllEmployees();

                            System.out.println("\n=== All Employees ===");
                            System.out.printf("%-5s %-20s %-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                                    "ID", "Name", "Designation", "Basic", "HRA", "DA", "Deductions", "Bonus", "Tax", "Net Salary");

                            for (Employee e : allEmployees) {
                                System.out.printf("%-5d %-20s %-15s %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f\n",
                                        e.getEmpId(),
                                        e.getName(),
                                        e.getDesignation(),
                                        e.getBasicSalary(),
                                        e.getHra(),
                                        e.getDa(),
                                        e.getDeductions(),
                                        e.getBonus(),
                                        e.getTax(),
                                        e.getNetSalary()
                                );
                            }

                            System.out.println("\nDisplaying all employees...");
                            System.out.println("Total employees: " + allEmployees.size());
                            System.out.println("Report generated successfully!");

                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... ");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }


                        } catch (Exception e) {
                            System.out.println("Error displaying employees: " + e.getMessage());
                        }
                    } else if (loggedInUser.getRole().equals("employee")) {
                        try {
                            dao.generateReportForEmployee(loggedInUser.getEmployeeId());
                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... ");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }

                        } catch (Exception e) {
                            System.out.println("Error generating report: " + e.getMessage());
                        }
                    }
                    break;

                /**
                 * (For Admin) Case 3 - Update Employee:
                 * Allows the admin to update details of an existing employee by entering Employee ID and new data.
                 * Updates the database record with the new values.
                 * (For Employee) Case 3 - Exit:
                 * Confirms with the user if they want to exit.
                 * If confirmed, exits the application.
                 */
                case 3:
                    if (loggedInUser.getRole().equals("admin")) {

                        try {
                            System.out.print("Enter Employee ID to update: ");
                            int updateId = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter new name: ");
                            String newName = sc.nextLine();
                            while (newName.isBlank()) {
                                System.out.println("Name cannot be empty! Enter again:");
                                newName = sc.nextLine();
                            }

                            System.out.print("Enter new designation: ");
                            String newDesignation = sc.nextLine();
                            while (newDesignation.isBlank()) {
                                System.out.println("Designation cannot be empty! Enter again:");
                                newDesignation = sc.nextLine();
                            }

                            System.out.print("Enter new basic salary: ");
                            double newBasic = sc.nextDouble();
                            while (newBasic < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                newBasic = sc.nextDouble();
                            }

                            System.out.print("Enter new HRA: ");
                            double newHra = sc.nextDouble();
                            while (newHra < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                newHra = sc.nextDouble();
                            }

                            System.out.print("Enter new DA: ");
                            double newDa = sc.nextDouble();
                            while (newDa < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                newDa = sc.nextDouble();
                            }

                            System.out.print("Enter new deductions: ");
                            double newDeductions = sc.nextDouble();
                            while (newDeductions < 0) {
                                System.out.println("Value cannot be negative! Enter again:");
                                newDeductions = sc.nextDouble();
                            }

                            System.out.print("Enter new Bonus: ");
                            double newBonus = sc.nextDouble();
                            while (newBonus < 0) {
                                System.out.println("Bonus cannot be negative! Enter again:");
                                newBonus = sc.nextDouble();
                            }

                            System.out.print("Enter new Tax: ");
                            double newTax = sc.nextDouble();
                            while (newTax < 0) {
                                System.out.println("Tax cannot be negative! Enter again:");
                                newTax = sc.nextDouble();
                            }

                            Employee updatedEmp = new Employee(newName, newDesignation, newBasic, newHra, newDa, newDeductions);
                            updatedEmp.setBonus(newBonus);
                            updatedEmp.setTax(newTax);

                            dao.updateEmployee(updateId, updatedEmp);

                            System.out.println("\nEmployee updated successfully!");
                            System.out.println("Employee ID: " + updateId + ", Name: " + updatedEmp.getName());

                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... Goodbye!");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }


                        } catch (Exception e) {
                            System.out.println("Error updating employee: " + e.getMessage());
                        }

                    } else if (loggedInUser.getRole().equals("employee")) {

                        System.out.print("Are you sure you want to exit? (Y/N): ");
                        String confirmExit = sc.nextLine();
                        if (confirmExit.equalsIgnoreCase("Y")) {
                            System.out.println("Exiting program... ");
                            sc.close();
                            System.exit(0);
                        } else {
                            System.out.println("Exit cancelled. Returning to main menu.");
                        }
                    }
                    break;

                /**
                 * Case 4 - Delete Employee:
                 * Allows the admin to delete an employee record from the database by providing Employee ID.
                 * A confirmation prompt is shown before deletion.
                 */
                case 4:
                    try {
                        System.out.print("Enter Employee ID to delete: ");
                        int deleteId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Are you sure you want to delete employee ID " + deleteId + "? (Y/N): ");
                        String confirm = sc.nextLine();

                        if (confirm.equalsIgnoreCase("Y")) {
                            boolean deleted = dao.deleteEmployee(deleteId);

                            if (deleted) {
                                System.out.println("\n Employee deleted successfully!");
                                System.out.println("Deleted Employee ID: " + deleteId);
                            } else {
                                System.out.println("\n⚠️ No employee found with ID: " + deleteId);
                            }


                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... ");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }

                        } else {
                            System.out.println("Deletion cancelled. Returning to main menu.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting employee: " + e.getMessage());
                    }
                    break;


                /**
                 * Case 5 - Generate Report (Console):
                 * Displays a full report of all employees sorted by net salary in descending order.
                 * Prints employee details in tabular form on the console.
                 */
                case 5:
                    if (loggedInUser.getRole().equals("admin")) {
                        try {

                            System.out.print("Enter designation to filter (leave blank for all): ");
                            String designation = sc.nextLine();

                            System.out.print("Enter minimum net salary to filter (enter -1 for no filter): ");
                            double minSalary = sc.nextDouble();

                            System.out.print("Enter maximum net salary to filter (enter -1 for no filter): ");
                            double maxSalary = sc.nextDouble();
                            sc.nextLine();


                            List<Employee> filteredList = dao.getFilteredEmployees(
                                    designation.isBlank() ? null : designation,
                                    minSalary >= 0 ? minSalary : -1,
                                    maxSalary >= 0 ? maxSalary : -1
                            );


                            if(filteredList.isEmpty()) {
                                System.out.println("No employees match the filter criteria!");
                            } else {
                                System.out.println("+-------+----------------------+---------------+------------+------------+------------+------------+------------+------------+------------+");
                                System.out.printf("| %-5s | %-20s | %-13s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s |\n",
                                        "ID", "Name", "Designation", "Basic", "HRA", "DA", "Deductions", "Bonus", "Tax", "Net Salary");
                                System.out.println("+-------+----------------------+---------------+------------+------------+------------+------------+------------+------------+------------+");

                                for (Employee e : filteredList) {
                                    System.out.printf("| %-5d | %-20s | %-13s | %-10.2f | %-10.2f | %-10.2f | %-10.2f | %-10.2f | %-10.2f | %-10.2f |\n",
                                            e.getEmpId(),
                                            e.getName(),
                                            e.getDesignation(),
                                            e.getBasicSalary(),
                                            e.getHra(),
                                            e.getDa(),
                                            e.getDeductions(),
                                            e.getBonus(),
                                            e.getTax(),
                                            e.getNetSalary());
                                }
                                System.out.println("+-------+----------------------+---------------+------------+------------+------------+------------+------------+------------+------------+");


                            }


                            double total = 0, max = Double.MIN_VALUE, min = Double.MAX_VALUE;
                            for(Employee e : filteredList) {
                                double salary = e.getNetSalary();
                                total += salary;
                                if(salary > max) max = salary;
                                if(salary < min) min = salary;
                            }
                            double avg = filteredList.size() > 0 ? total / filteredList.size() : 0;

                            System.out.println("\nSummary:");
                            System.out.println("Total Net Salary: " + total);
                            System.out.println("Average Net Salary: " + avg);
                            System.out.println("Highest Net Salary: " + max);
                            System.out.println("Lowest Net Salary: " + min);


                            System.out.println("\nReport generated successfully!");
                            System.out.println("Filters Applied: " + (designation.isBlank() ? "None" : designation) +
                                    ", Min Salary: " + (minSalary >= 0 ? minSalary : "None") +
                                    ", Max Salary: " + (maxSalary >= 0 ? maxSalary : "None"));
                            System.out.println("Total Employees in report: " + filteredList.size());

                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... ");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }


                        } catch(Exception e) {
                            System.out.println("Error generating report: " + e.getMessage());
                        }

                    } else if (loggedInUser.getRole().equals("employee")) {
                        try {
                            Employee emp = dao.getEmployeeById(loggedInUser.getEmployeeId());
                            if(emp != null) {
                                System.out.printf("\n%-5s %-20s %-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                                        "ID", "Name", "Designation", "Basic", "HRA", "DA", "Deductions", "Bonus", "Tax", "Net Salary");
                                System.out.printf("%-5d %-20s %-15s %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f\n",
                                        emp.getEmpId(), emp.getName(), emp.getDesignation(),
                                        emp.getBasicSalary(), emp.getHra(), emp.getDa(),
                                        emp.getDeductions(), emp.getBonus(), emp.getTax(),
                                        emp.getNetSalary());

                                System.out.println("\nYour report generated successfully!");

                                System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                                System.out.print("Enter your choice: ");
                                int navChoice = sc.nextInt();
                                sc.nextLine();

                                if (navChoice == 8) {
                                    System.out.println("Exiting program... Goodbye!");
                                    sc.close();
                                    System.exit(0);
                                } else if (navChoice == 0) {

                                } else {
                                    System.out.println("Invalid choice. Returning to main menu...");
                                }
                            } else {
                                System.out.println("\nYour employee details not found!");
                            }
                        } catch(Exception e) {
                            System.out.println("Error generating your report: " + e.getMessage());
                        }
                    }
                    break;


                /**
                 * Case 6 - Export Report to CSV:
                 * Exports the employee report to a file named 'EmployeeReport.csv'.
                 * This file includes all employee details for further reference or sharing.
                 */
                case 6:
                    if (loggedInUser.getRole().equals("admin")) {
                        try {
                            List<Employee> employees = dao.getAllEmployees();
                            dao.exportReportToCSV();
                            System.out.println("\nReport exported successfully to EmployeeReport.csv");
                            System.out.println("Total employees exported: " + employees.size());
                            System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                            System.out.print("Enter your choice: ");
                            int navChoice = sc.nextInt();
                            sc.nextLine();

                            if (navChoice == 8) {
                                System.out.println("Exiting program... Goodbye!");
                                sc.close();
                                System.exit(0);
                            } else if (navChoice == 0) {

                            } else {
                                System.out.println("Invalid choice. Returning to main menu...");
                            }

                        } catch (Exception e) {
                            System.out.println("Error exporting report: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                    break;



                /**
                 * Case 7 - Search Employee by ID or Name:
                 * Allows the admin to search for employees by providing either an Employee ID or part of the Name.
                 * Displays matched employee details in a tabular format.
                 */
                case 7:
                    if (loggedInUser.getRole().equals("admin")) {
                        System.out.println("Search by: [1] Employee ID  [2] Name");
                        int searchOption = sc.nextInt();
                        sc.nextLine();

                        if (searchOption == 1) {
                            System.out.print("Enter Employee ID: ");
                            int searchId = sc.nextInt();
                            sc.nextLine();

                            Employee searchedEmp = dao.getEmployeeById(searchId);
                            if (searchedEmp != null) {
                                System.out.printf("\n%-5s %-20s %-15s %-10s\n", "ID", "Name", "Designation", "Net Salary");
                                System.out.printf("%-5d %-20s %-15s %-10.2f\n",
                                        searchedEmp.getEmpId(),
                                        searchedEmp.getName(),
                                        searchedEmp.getDesignation(),
                                        searchedEmp.getNetSalary());
                                System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                                System.out.print("Enter your choice: ");
                                int navChoice = sc.nextInt();
                                sc.nextLine();

                                if (navChoice == 8) {
                                    System.out.println("Exiting program...");
                                    sc.close();
                                    System.exit(0);
                                } else if (navChoice == 0) {

                                } else {
                                    System.out.println("Invalid choice. Returning to main menu...");
                                }

                            } else {
                                System.out.println("\nEmployee not found!");
                            }

                        } else if (searchOption == 2) {
                            System.out.print("Enter Name (partial or full): ");
                            String searchName = sc.nextLine();

                            List<Employee> empList = dao.getEmployeesByName(searchName);
                            if (empList.isEmpty()) {
                                System.out.println("\nNo employees found with this name!");
                            } else {
                                System.out.printf("\n%-5s %-20s %-15s %-10s\n", "ID", "Name", "Designation", "Net Salary");
                                for (Employee e : empList) {
                                    System.out.printf("%-5d %-20s %-15s %-10.2f\n",
                                            e.getEmpId(),
                                            e.getName(),
                                            e.getDesignation(),
                                            e.getNetSalary());
                                }
                                System.out.println("\n[0] Return to Main Menu    [8] Exit Program");
                                System.out.print("Enter your choice: ");
                                int navChoice = sc.nextInt();
                                sc.nextLine();

                                if (navChoice == 8) {
                                    System.out.println("Exiting program... Goodbye!");
                                    sc.close();
                                    System.exit(0);
                                } else if (navChoice == 0) {

                                } else {
                                    System.out.println("Invalid choice. Returning to main menu...");
                                }

                            }

                        } else {
                            System.out.println("Invalid choice!");
                        }
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                    break;

                /**
                 * Case 8 - Exit:
                 * Exits the payroll system application.
                 */
                case 8:
                    System.out.print("Are you sure you want to exit? (Y/N): ");
                    String exitChoice = sc.nextLine();
                    if(exitChoice.equalsIgnoreCase("Y")) {
                        System.out.println("Exiting program... ");
                        sc.close();
                        System.exit(0);
                    } else {
                        System.out.println("Returning to main menu...");
                    }
                    break;


                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}