-- Create Database
CREATE DATABASE IF NOT EXISTS company_db;
USE company_db;

-- Create Employee Table
CREATE TABLE IF NOT EXISTS Employee (
    EmpID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Salary DECIMAL(10, 2) NOT NULL,
    Department VARCHAR(50),
    JoinDate DATE,
    Email VARCHAR(100)
);

-- Insert Sample Data
INSERT INTO Employee (Name, Salary, Department, JoinDate, Email) VALUES
('John Doe', 55000.00, 'IT', '2020-01-15', 'john.doe@company.com'),
('Jane Smith', 62000.00, 'HR', '2019-03-22', 'jane.smith@company.com'),
('Mike Johnson', 58000.00, 'IT', '2021-06-10', 'mike.johnson@company.com'),
('Sarah Williams', 70000.00, 'Finance', '2018-11-05', 'sarah.williams@company.com'),
('David Brown', 52000.00, 'Marketing', '2022-02-28', 'david.brown@company.com'),
('Emily Davis', 65000.00, 'IT', '2020-08-17', 'emily.davis@company.com'),
('Robert Taylor', 60000.00, 'HR', '2019-09-30', 'robert.taylor@company.com'),
('Lisa Anderson', 75000.00, 'Finance', '2017-05-12', 'lisa.anderson@company.com');

-- Verify Data
SELECT * FROM Employee;