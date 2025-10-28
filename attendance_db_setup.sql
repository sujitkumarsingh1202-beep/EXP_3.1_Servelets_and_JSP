-- Create Database
CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

-- Create Student Table
CREATE TABLE IF NOT EXISTS Student (
    StudentID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Class VARCHAR(20),
    Email VARCHAR(100)
);

-- Create Attendance Table
CREATE TABLE IF NOT EXISTS Attendance (
    AttendanceID INT PRIMARY KEY AUTO_INCREMENT,
    StudentID INT NOT NULL,
    AttendanceDate DATE NOT NULL,
    Status ENUM('Present', 'Absent', 'Late') NOT NULL,
    Remarks VARCHAR(255),
    RecordedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID),
    UNIQUE KEY unique_attendance (StudentID, AttendanceDate)
);

-- Insert Sample Students
INSERT INTO Student (Name, Class, Email) VALUES
('Alice Johnson', '10A', 'alice.j@school.com'),
('Bob Smith', '10A', 'bob.s@school.com'),
('Charlie Brown', '10B', 'charlie.b@school.com'),
('Diana Prince', '10A', 'diana.p@school.com'),
('Ethan Hunt', '10B', 'ethan.h@school.com');

-- Insert Sample Attendance Records
INSERT INTO Attendance (StudentID, AttendanceDate, Status, Remarks) VALUES
(1, '2024-10-01', 'Present', 'On time'),
(2, '2024-10-01', 'Absent', 'Sick leave'),
(3, '2024-10-01', 'Present', 'On time'),
(1, '2024-10-02', 'Late', 'Traffic delay'),
(2, '2024-10-02', 'Present', 'On time');

-- View to get student attendance with names
CREATE OR REPLACE VIEW AttendanceView AS
SELECT 
    a.AttendanceID,
    a.StudentID,
    s.Name AS StudentName,
    s.Class,
    a.AttendanceDate,
    a.Status,
    a.Remarks,
    a.RecordedAt
FROM Attendance a
JOIN Student s ON a.StudentID = s.StudentID
ORDER BY a.AttendanceDate DESC, s.Name;

-- Verify Data
SELECT * FROM Student;
SELECT * FROM AttendanceView;