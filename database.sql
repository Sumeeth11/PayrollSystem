-- Create the database
CREATE DATABASE IF NOT EXISTS payroll_db;

-- Switch to the database
USE payroll_db;

-- Create employee table
CREATE TABLE IF NOT EXISTS employee (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    designation VARCHAR(50) NOT NULL,
    basic_salary DOUBLE NOT NULL,
    hra DOUBLE NOT NULL,
    da DOUBLE NOT NULL,
    deductions DOUBLE NOT NULL,
    bonus DOUBLE DEFAULT 0,
    tax DOUBLE DEFAULT 0,
    net_salary DOUBLE
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'employee') NOT NULL,
    employee_id INT,
    FOREIGN KEY (employee_id) REFERENCES employee(emp_id) ON DELETE SET NULL
);

-- Insert a sample employee (emp_id = 1)
INSERT INTO employee (name, designation, basic_salary, hra, da, deductions, bonus, tax, net_salary)
VALUES ('John Doe', 'Software Engineer', 30000, 5000, 4000, 2000, 3000, 1000, 40000);

-- Insert sample users (Admin + Employee linked to emp_id = 1)
INSERT INTO users (username, password, role, employee_id)
VALUES 
('admin', 'admin123', 'admin', NULL),
('emp', 'emp123', 'employee', 1);
