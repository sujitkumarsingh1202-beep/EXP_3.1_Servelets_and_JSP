import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Hardcoded credentials for demonstration
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Retrieve form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // HTML header
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Login Result</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("       background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("       min-height: 100vh; display: flex; justify-content: center;");
        out.println("       align-items: center; padding: 20px; }");
        out.println(".result-container { background: white; padding: 40px; border-radius: 10px;");
        out.println("                    box-shadow: 0 10px 25px rgba(0,0,0,0.2);");
        out.println("                    max-width: 500px; text-align: center; }");
        out.println(".success { color: #10b981; font-size: 48px; margin-bottom: 20px; }");
        out.println(".error { color: #ef4444; font-size: 48px; margin-bottom: 20px; }");
        out.println("h2 { color: #333; margin-bottom: 15px; }");
        out.println("p { color: #666; line-height: 1.6; margin-bottom: 25px; }");
        out.println(".info-box { background: #f0f4ff; padding: 15px; border-radius: 5px;");
        out.println("            border-left: 4px solid #667eea; margin: 20px 0; text-align: left; }");
        out.println(".btn { display: inline-block; padding: 12px 30px; margin: 10px;");
        out.println("       background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("       color: white; text-decoration: none; border-radius: 5px;");
        out.println("       transition: transform 0.2s; }");
        out.println(".btn:hover { transform: translateY(-2px); }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='result-container'>");
        
        // Validate credentials
        if (username != null && password != null && 
            username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            
            // Successful login
            out.println("<div class='success'>✓</div>");
            out.println("<h2>Welcome, " + username + "!</h2>");
            out.println("<p>You have successfully logged into the system.</p>");
            out.println("<div class='info-box'>");
            out.println("<strong>Login Details:</strong><br>");
            out.println("Username: " + username + "<br>");
            out.println("Login Time: " + new java.util.Date() + "<br>");
            out.println("Session Status: Active");
            out.println("</div>");
            out.println("<a href='login.html' class='btn'>Logout</a>");
            
        } else {
            // Failed login
            out.println("<div class='error'>✗</div>");
            out.println("<h2>Login Failed</h2>");
            out.println("<p>Invalid username or password. Please try again.</p>");
            out.println("<div class='info-box'>");
            out.println("<strong>Troubleshooting:</strong><br>");
            out.println("• Check your username and password<br>");
            out.println("• Ensure caps lock is off<br>");
            out.println("• Use the test credentials provided");
            out.println("</div>");
            out.println("<a href='login.html' class='btn'>Try Again</a>");
        }
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.html");
    }
}