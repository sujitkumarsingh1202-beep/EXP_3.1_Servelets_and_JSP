import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ViewAttendanceServlet")
public class ViewAttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password"; // Change this
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // HTML Header
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>View Attendance Records</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: #f5f7fa; padding: 20px; }");
        out.println(".container { max-width: 1200px; margin: 0 auto; }");
        out.println("h1 { color: #2c3e50; margin-bottom: 30px; text-align: center; }");
        out.println(".controls { background: white; padding: 20px; border-radius: 10px;");
        out.println("            box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin-bottom: 20px;");
        out.println("            display: flex; justify-content: space-between; align-items: center; }");
        out.println(".btn { padding: 12px 25px; background: #667eea; color: white; text-decoration: none;");
        out.println("       border-radius: 5px; transition: 0.3s; display: inline-block; }");
        out.println(".btn:hover { background: #5568d3; }");
        out.println(".filter-form { display: flex; gap: 10px; }");
        out.println("select, input[type='date'] { padding: 10px; border: 2px solid #ddd; border-radius: 5px; }");
        out.println("table { width: 100%; background: white; border-radius: 10px; overflow: hidden;");
        out.println("        box-shadow: 0 2px 10px rgba(0,0,0,0.1); border-collapse: collapse; }");
        out.println("th { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("     color: white; padding: 15px; text-align: left; font-weight: 600; }");
        out.println("td { padding: 15px; border-bottom: 1px solid #ecf0f1; }");
        out.println("tr:hover { background: #f8f9fa; }");
        out.println(".status-present { color: #10b981; font-weight: 600; }");
        out.println(".status-absent { color: #ef4444; font-weight: 600; }");
        out.println(".status-late { color: #f59e0b; font-weight: 600; }");
        out.println(".stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));");
        out.println("         gap: 20px; margin-bottom: 20px; }");
        out.println(".stat-card { background: white; padding: 20px; border-radius: 10px;");
        out.println("             box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }");
        out.println(".stat-value { font-size: 32px; font-weight: bold; color: #667eea; }");
        out.println(".stat-label { color: #666; margin-top: 8px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>📊 Attendance Records</h1>");
        
        // Controls
        out.println("<div class='controls'>");
        out.println("<a href='attendance.jsp' class='btn'>← Back to Portal</a>");
        out.println("<form method='GET' class='filter-form'>");
        out.println("<input type='date' name='filterDate' placeholder='Filter by date'>");
        out.println("<button type='submit' class='btn'>Filter</button>");
        out.println("<a href='ViewAttendanceServlet' class='btn'>Clear Filter</a>");
        out.println("</form>");
        out.println("</div>");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Display statistics
            displayStatistics(out, conn);
            
            // Get filter parameter
            String filterDate = request.getParameter("filterDate");
            
            // Prepare query
            String sql;
            if (filterDate != null && !filterDate.trim().isEmpty()) {
                sql = "SELECT * FROM AttendanceView WHERE AttendanceDate = ? ORDER BY AttendanceDate DESC, StudentName";
                pstmt = conn.prepareStatement(sql);
                pstmt.setDate(1, java.sql.Date.valueOf(filterDate));
            } else {
                sql = "SELECT * FROM AttendanceView ORDER BY AttendanceDate DESC, StudentName LIMIT 50";
                pstmt = conn.prepareStatement(sql);
            }
            
            rs = pstmt.executeQuery();
            
            // Display table
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Student Name</th>");
            out.println("<th>Class</th>");
            out.println("<th>Date</th>");
            out.println("<th>Status</th>");
            out.println("<th>Remarks</th>");
            out.println("<th>Recorded At</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                String status = rs.getString("Status");
                String statusClass = "status-" + status.toLowerCase();
                
                out.println("<tr>");
                out.println("<td>" + rs.getInt("AttendanceID") + "</td>");
                out.println("<td>" + rs.getString("StudentName") + "</td>");
                out.println("<td>" + rs.getString("Class") + "</td>");
                out.println("<td>" + rs.getDate("AttendanceDate") + "</td>");
                out.println("<td class='" + statusClass + "'>" + status + "</td>");
                out.println("<td>" + (rs.getString("Remarks") != null ? rs.getString("Remarks") : "-") + "</td>");
                out.println("<td>" + rs.getTimestamp("RecordedAt") + "</td>");
                out.println("</tr>");
            }
            
            if (!hasRecords) {
                out.println("<tr><td colspan='7' style='text-align: center; padding: 30px; color: #999;'>");
                out.println("No attendance records found");
                out.println("</td></tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            
        } catch (Exception e) {
            out.println("<div style='background: #fef2f2; color: #991b1b; padding: 20px; border-radius: 5px;'>");
            out.println("<strong>Error:</strong> " + e.getMessage());
            out.println("</div>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
    
    private void displayStatistics(PrintWriter out, Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            
            out.println("<div class='stats'>");
            
            // Total records
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Attendance");
            if (rs.next()) {
                out.println("<div class='stat-card'>");
                out.println("<div class='stat-value'>" + rs.getInt("total") + "</div>");
                out.println("<div class='stat-label'>Total Records</div>");
                out.println("</div>");
            }
            rs.close();
            
            // Present count
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Attendance WHERE Status = 'Present'");
            if (rs.next()) {
                out.println("<div class='stat-card'>");
                out.println("<div class='stat-value' style='color: #10b981;'>" + rs.getInt("count") + "</div>");
                out.println("<div class='stat-label'>Present</div>");
                out.println("</div>");
            }
            rs.close();
            
            // Absent count
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Attendance WHERE Status = 'Absent'");
            if (rs.next()) {
                out.println("<div class='stat-card'>");
                out.println("<div class='stat-value' style='color: #ef4444;'>" + rs.getInt("count") + "</div>");
                out.println("<div class='stat-label'>Absent</div>");
                out.println("</div>");
            }
            rs.close();
            