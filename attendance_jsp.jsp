<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Attendance Portal</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 900px;
            margin: 0 auto;
        }
        
        h1 {
            color: white;
            text-align: center;
            margin-bottom: 30px;
            font-size: 36px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
        }
        
        .card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            margin-bottom: 20px;
        }
        
        h2 {
            color: #333;
            margin-bottom: 20px;
            border-bottom: 3px solid #667eea;
            padding-bottom: 10px;
        }
        
        .form-grid {
            display: grid;
            gap: 20px;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
        }
        
        label {
            margin-bottom: 8px;
            color: #555;
            font-weight: 600;
        }
        
        select, input[type="date"], textarea {
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        
        select:focus, input[type="date"]:focus, textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        
        .radio-group {
            display: flex;
            gap: 20px;
            padding: 10px 0;
        }
        
        .radio-option {
            display: flex;
            align-items: center;
            gap: 8px;
            cursor: pointer;
        }
        
        .radio-option input[type="radio"] {
            width: 18px;
            height: 18px;
            cursor: pointer;
        }
        
        .btn-submit {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        th {
            background: #667eea;
            color: white;
            padding: 12px;
            text-align: left;
            font-weight: 600;
        }
        
        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }
        
        tr:hover {
            background: #f8f9fa;
        }
        
        .status-present {
            color: #10b981;
            font-weight: 600;
        }
        
        .status-absent {
            color: #ef4444;
            font-weight: 600;
        }
        
        .status-late {
            color: #f59e0b;
            font-weight: 600;
        }
        
        .no-records {
            text-align: center;
            padding: 30px;
            color: #999;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 Student Attendance Portal</h1>
        
        <!-- Attendance Form Card -->
        <div class="card">
            <h2>Mark Attendance</h2>
            <form action="AttendanceServlet" method="POST">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="studentId">Select Student *</label>
                        <select name="studentId" id="studentId" required>
                            <option value="">-- Choose Student --</option>
                            <%
                                Connection conn = null;
                                Statement stmt = null;
                                ResultSet rs = null;
                                
                                try {
                                    Class.forName("com.mysql.cj.jdbc.Driver");
                                    conn = DriverManager.getConnection(
                                        "jdbc:mysql://localhost:3306/school_db",
                                        "root",
                                        "password" // Change this
                                    );
                                    
                                    stmt = conn.createStatement();
                                    rs = stmt.executeQuery("SELECT StudentID, Name, Class FROM Student ORDER BY Name");
                                    
                                    while (rs.next()) {
                                        out.println("<option value='" + rs.getInt("StudentID") + "'>");
                                        out.println(rs.getString("Name") + " (" + rs.getString("Class") + ")");
                                        out.println("</option>");
                                    }
                                } catch (Exception e) {
                                    out.println("<option value=''>Error loading students</option>");
                                } finally {
                                    if (rs != null) rs.close();
                                    if (stmt != null) stmt.close();
                                    if (conn != null) conn.close();
                                }
                            %>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="attendanceDate">Date *</label>
                        <input type="date" name="attendanceDate" id="attendanceDate" 
                               value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" 
                               required>
                    </div>
                    
                    <div class="form-group">
                        <label>Attendance Status *</label>
                        <div class="radio-group">
                            <label class="radio-option">
                                <input type="radio" name="status" value="Present" checked>
                                Present
                            </label>
                            <label class="radio-option">
                                <input type="radio" name="status" value="Absent">
                                Absent
                            </label>
                            <label class="radio-option">
                                <input type="radio" name="status" value="Late">
                                Late
                            </label>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="remarks">Remarks (Optional)</label>
                        <textarea name="remarks" id="remarks" 
                                  placeholder="Add any additional notes..."></textarea>
                    </div>
                </div>
                
                <button type="submit" class="btn-submit">Submit Attendance</button>
            </form>
        </div>
        
        <!-- Recent Attendance Records -->
        <div class="card">
            <h2>Recent Attendance Records</h2>
            <table>
                <thead>
                    <tr>
                        <th>Student Name</th>
                        <th>Class</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>Remarks</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        conn = null;
                        stmt = null;
                        rs = null;
                        
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/school_db",
                                "root",
                                "Sujit1202@" // Change this
                            );
                            
                            stmt = conn.createStatement();
                            String query = "SELECT * FROM AttendanceView ORDER BY AttendanceDate DESC LIMIT 10";
                            rs = stmt.executeQuery(query);
                            
                            boolean hasRecords = false;
                            while (rs.next()) {
                                hasRecords = true;
                                String status = rs.getString("Status");
                                String statusClass = "status-" + status.toLowerCase();
                                
                                out.println("<tr>");
                                out.println("<td>" + rs.getString("StudentName") + "</td>");
                                out.println("<td>" + rs.getString("Class") + "</td>");
                                out.println("<td>" + rs.getDate("AttendanceDate") + "</td>");
                                out.println("<td class='" + statusClass + "'>" + status + "</td>");
                                out.println("<td>" + (rs.getString("Remarks") != null ? rs.getString("Remarks") : "-") + "</td>");
                                out.println("</tr>");
                            }
                            
                            if (!hasRecords) {
                                out.println("<tr><td colspan='5' class='no-records'>No attendance records found</td></tr>");
                            }
                            
                        } catch (Exception e) {
                            out.println("<tr><td colspan='5' class='no-records'>Error: " + e.getMessage() + "</td></tr>");
                        } finally {
                            if (rs != null) rs.close();
                            if (stmt != null) stmt.close();
                            if (conn != null) conn.close();
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
