import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password"; // Change this
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Retrieve form parameters
        String studentId = request.getParameter("studentId");
        String attendanceDate = request.getParameter("attendanceDate");
        String status = request.getParameter("status");
        String remarks = request.getParameter("remarks");
        
        // Validate input
        if (studentId == null || attendanceDate == null || status == null) {
            showErrorPage(out, "Missing required fields");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Prepare SQL statement with INSERT ... ON DUPLICATE KEY UPDATE
            // This handles both new entries and updates to existing records
            String sql = "INSERT INTO Attendance (StudentID, AttendanceDate, Status, Remarks) " +
                        "VALUES (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE Status = ?, Remarks = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(studentId));
            pstmt.setDate(2, java.sql.Date.valueOf(attendanceDate));
            pstmt.setString(3, status);
            pstmt.setString(4, remarks);
            pstmt.setString(5, status);
            pstmt.setString(6, remarks);
            
            // Execute update
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get student name for confirmation
                String studentName = getStudentName(conn, studentId);
                showSuccessPage(out, studentName, attendanceDate, status);
            } else {
                showErrorPage(out, "Failed to record attendance");
            }
            
        } catch (NumberFormatException e) {
            showErrorPage(out, "Invalid Student ID format");
        } catch (ClassNotFoundException e) {
            showErrorPage(out, "MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            showErrorPage(out, "Database error: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        out.close();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("attendance.jsp");
    }
    
    private String getStudentName(Connection conn, String studentId) {
        String name = "Student";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT Name FROM Student WHERE StudentID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(studentId));
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                name = rs.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return name;
    }
    
    private void showSuccessPage(PrintWriter out, String studentName, String date, String status) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Attendance Recorded</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("       min-height: 100vh; display: flex; justify-content: center; align-items: center; padding: 20px; }");
        out.println(".success-container { background: white; padding: 50px; border-radius: 10px;");
        out.println("                     box-shadow: 0 10px 25px rgba(0,0,0,0.2); max-width: 500px; text-align: center; }");
        out.println(".success-icon { font-size: 80px; color: #10b981; margin-bottom: 20px; }");
        out.println("h1 { color: #333; margin-bottom: 20px; }");
        out.println(".info-box { background: #f0fdf4; border-left: 4px solid #10b981; padding: 20px;");
        out.println("            margin: 25px 0; text-align: left; border-radius: 5px; }");
        out.println(".info-row { display: flex; justify-content: space-between; padding: 8px 0;");
        out.println("            border-bottom: 1px solid #d1fae5; }");
        out.println(".info-row:last-child { border-bottom: none; }");
        out.println(".info-label { font-weight: 600; color: #555; }");
        out.println(".info-value { color: #333; }");
        out.println(".btn-group { display: flex; gap: 15px; margin-top: 25px; }");
        out.println(".btn { flex: 1; padding: 12px 25px; border: none; border-radius: 5px;");
        out.println("       font-size: 14px; font-weight: 600; cursor: pointer; text-decoration: none;");
        out.println("       display: inline-block; text-align: center; transition: transform 0.2s; }");
        out.println(".btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }");
        out.println(".btn-secondary { background: #e5e7eb; color: #333; }");
        out.println(".btn:hover { transform: translateY(-2px); }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='success-container'>");
        out.println("<div class='success-icon'>✓</div>");
        out.println("<h1>Attendance Recorded Successfully!</h1>");
        out.println("<p>The attendance has been saved to the database.</p>");
        out.println("<div class='info-box'>");
        out.println("<div class='info-row'>");
        out.println("<span class='info-label'>Student:</span>");
        out.println("<span class='info-value'>" + studentName + "</span>");
        out.println("</div>");
        out.println("<div class='info-row'>");
        out.println("<span class='info-label'>Date:</span>");
        out.println("<span class='info-value'>" + date + "</span>");
        out.println("</div>");
        out.println("<div class='info-row'>");
        out.println("<span class='info-label'>Status:</span>");
        out.println("<span class='info-value'>" + status + "</span>");
        out.println("</div>");
        out.println("<div class='info-row'>");
        out.println("<span class='info-label'>Time:</span>");
        out.println("<span class='info-value'>" + new java.util.Date() + "</span>");
        out.println("</div>");
        out.println("</div>");
        out.println("<div class='btn-group'>");
        out.println("<a href='attendance.jsp' class='btn btn-primary'>Mark Another</a>");
        out.println("<a href='ViewAttendanceServlet' class='btn btn-secondary'>View All Records</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void showErrorPage(PrintWriter out, String errorMessage) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Error</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("       min-height: 100vh; display: flex; justify-content: center; align-items: center; padding: 20px; }");
        out.println(".error-container { background: white; padding: 50px; border-radius: 10px;");
        out.println("                   box-shadow: 0 10px 25px rgba(0,0,0,0.2); max-width: 500px; text-align: center; }");
        out.println(".error-icon { font-size: 80px; color: #ef4444; margin-bottom: 20px; }");
        out.println("h1 { color: #333; margin-bottom: 20px; }");
        out.println(".error-box { background: #fef2f2; border-left: 4px solid #ef4444; padding: 20px;");
        out.println("             margin: 25px 0; text-align: left; border-radius: 5px; color: #991b1b; }");
        out.println(".btn { padding: 12px 30px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("       color: white; border: none; border-radius: 5px; font-size: 14px; font-weight: 600;");
        out.println("       cursor: pointer; text-decoration: none; display: inline-block; margin-top: 20px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='error-container'>");
        out.println("<div class='error-icon'>✗</div>");
        out.println("<h1>Error Recording Attendance</h1>");
        out.println("<div class='error-box'>");
        out.println("<strong>Error:</strong> " + errorMessage);
        out.println("</div>");
        out.println("<a href='attendance.jsp' class='btn'>Go Back</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}