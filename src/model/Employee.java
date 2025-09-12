package model;

/**
 * Employee model class representing an employee in the system.
 */
public class Employee {
    private int empId;
    private String name;
    private String designation;
    private double basicSalary;
    private double hra;
    private double da;
    private double deductions;
    private double bonus;
    private double tax;
    private double netSalary;

    /**
     * Constructor used when reading Employee data from the database.
     */
    public Employee(int empId, String name, String designation, double basicSalary, double hra, double da, double deductions) {
        this.empId = empId;
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.da = da;
        this.deductions = deductions;
        calculateNetSalary();
    }

    /**
     * Constructor used when adding a new Employee.
     */
    public Employee(String name, String designation, double basicSalary, double hra, double da, double deductions) {
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.da = da;
        this.deductions = deductions;
        calculateNetSalary();
    }

    // Getters and setters with JavaDoc comments

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getHra() {
        return hra;
    }

    public void setHra(double hra) {
        this.hra = hra;
    }

    public double getDa() {
        return da;
    }

    public void setDa(double da) {
        this.da = da;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getNetSalary() {
        return netSalary;
    }

    /**
     * Calculates net salary based on fields.
     */
    public void calculateNetSalary() {
        this.netSalary = (basicSalary + hra + da + bonus) - (deductions + tax);
    }
}
