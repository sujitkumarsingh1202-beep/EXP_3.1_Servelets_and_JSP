import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/company_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password"; // Change this
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String empId = request.getParameter("empId");
        
        // Generate HTML header
        printHTMLHeader(out);
        
        if (empId != null && !empId.trim().isEmpty()) {
            // Search for specific employee
            searchEmployee(out, empId);
        } else {
            // Display all employees
            displayAllEmployees(out);
        }
        
        // Generate HTML footer
        printHTMLFooter(out);
        out.close();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void printHTMLHeader(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Employee Management System</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: #f5f7fa; padding: 20px; }");
        out.println(".container { max-width: 1200px; margin: 0 auto; }");
        out.println("h1 { color: #2c3e50; margin-bottom: 30px; text-align: center; }");
        out.println(".search-box { background: white; padding: 25px; border-radius: 10px;");
        out.println("              box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin-bottom: 30px; }");
        out.println(".search-form { display: flex; gap: 10px; align-items: center; }");
        out.println("input[type='text'] { flex: 1; padding: 12px; border: 2px solid #ddd;");
        out.println("                     border-radius: 5px; font-size: 14px; }");
        out.println("input[type='text']:focus { outline: none; border-color: #3498db; }");
        out.println(".btn { padding: 12px 25px; background: #3498db; color: white; border: none;");
        out.println("       border-radius: 5px; cursor: pointer; font-size: 14px; transition: 0.3s; }");
        out.println(".btn:hover { background: #2980b9; }");
        out.println(".btn-clear { background: #95a5a6; }");
        out.println(".btn-clear:hover { background: #7f8c8d; }");
        out.println("table { width: 100%; background: white; border-radius: 10px; overflow: hidden;");
        out.println("        box-shadow: 0 2px 10px rgba(0,0,0,0.1); border-collapse: collapse; }");
        out.println("th { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("     color: white; padding: 15px; text-align: left; font-weight: 600; }");
        out.println("td { padding: 15px; border-bottom: 1px solid #ecf0f1; }");
        out.println("tr:hover { background: #f8f9fa; }");
        out.println("tr:last-child td { border-bottom: none; }");
        out.println(".no-data { text-align: center; padding: 40px; color: #7f8c8d; }");
        out.println(".error { background: #ffe6e6; color: #c0392b; padding: 15px;");
        out.println("         border-radius: 5px; margin: 20px 0; border-left: 4px solid #c0392b; }");
        out.println(".success { background: #e8f8f5; color: #27ae60; padding: 15px;");
        out.println("           border-radius: 5px; margin: 20px 0; border-left: 4px solid #27ae60; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>🏢 Employee Management System</h1>");
        
        // Search form
        out.println("<div class='search-box'>");
        out.println("<form method='GET' action='EmployeeServlet' class='search-form'>");
        out.println("<input type='text' name='empId' placeholder='Enter Employee ID to search...'>");
        out.println("<button type='submit' class='btn'>Search</button>");
        out.println("<a href='EmployeeServlet' class='btn btn-clear'>View All</a>");
        out.println("</form>");
        out.println("</div>");
    }
    
    private void printHTMLFooter(PrintWriter out) {
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void displayAllEmployees(PrintWriter out) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Create statement
            stmt = conn.createStatement();
            
            // Execute query
            String sql = "SELECT * FROM Employee ORDER BY EmpID";
            rs = stmt.executeQuery(sql);
            
            // Display results in table
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Employee ID</th>");
            out.println("<th>Name</th>");
            out.println("<th>Salary</th>");
            out.println("<th>Department</th>");
            out.println("<th>Join Date</th>");
            out.println("<th>Email</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>$" + String.format("%,.2f", rs.getDouble("Salary")) + "</td>");
                out.println("<td>" + rs.getString("Department") + "</td>");
                out.println("<td>" + rs.getDate("JoinDate") + "</td>");
                out.println("<td>" + rs.getString("Email") + "</td>");
                out.println("</tr>");
            }
            
            if (!hasRecords) {
                out.println("<tr><td colspan='6' class='no-data'>No employee records found</td></tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            
        } catch (ClassNotFoundException e) {
            out.println("<div class='error'><strong>Error:</strong> MySQL JDBC Driver not found. " + e.getMessage() + "</div>");
        } catch (SQLException e) {
            out.println("<div class='error'><strong>Database Error:</strong> " + e.getMessage() + "</div>");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void searchEmployee(PrintWriter out, String empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Prepare statement to prevent SQL injection
            String sql = "SELECT * FROM Employee WHERE EmpID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(empId));
            
            // Execute query
            rs = pstmt.executeQuery();
            
            // Display results
            if (rs.next()) {
                out.println("<div class='success'>Employee found successfully!</div>");
                out.println("<table>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Employee ID</th>");
                out.println("<th>Name</th>");
                out.println("<th>Salary</th>");
                out.println("<th>Department</th>");
                out.println("<th>Join Date</th>");
                out.println("<th>Email</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>$" + String.format("%,.2f", rs.getDouble("Salary")) + "</td>");
                out.println("<td>" + rs.getString("Department") + "</td>");
                out.println("<td>" + rs.getDate("JoinDate") + "</td>");
                out.println("<td>" + rs.getString("Email") + "</td>");
                out.println("</tr>");
                out.println("</tbody>");
                out.println("</table>");
            } else {
                out.println("<div class='error'>No employee found with ID: " + empId + "</div>");
            }
            
        } catch (NumberFormatException e) {
            out.println("<div class='error'><strong>Invalid Input:</strong> Employee ID must be a number</div>");
        } catch (ClassNotFoundException e) {
            out.println("<div class='error'><strong>Error:</strong> MySQL JDBC Driver not found</div>");
        } catch (SQLException e) {
            out.println("<div class='error'><strong>Database Error:</strong> " + e.getMessage() + "</div>");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}