Web Applications Using Servlets and JSP for User Input Handling and Database Integration
Part a: User Login Using Servlet and HTML Form
Objective:
To develop a Java Servlet that accepts user credentials through an HTML form and displays a personalized welcome message upon successful login.

Explanation:
This task focuses on handling user input from a web form using Servlets:

Create an HTML form to capture username and password.

On submission, the form sends data to a Servlet using the POST method.

The Servlet:

Retrieves the parameters using request.getParameter().
Validates the credentials (hardcoded or from a database).
If valid, displays a personalized welcome message using the PrintWriter.
If invalid, shows an error message.
This program demonstrates:

Form submission using HTML.
Request handling in Servlets.
Response generation using dynamic content.
Part b: Display Employee Records with JDBC and Servlet Integration
Objective:
To create a Java Servlet that integrates with a database using JDBC and displays a list of employees. The servlet should also include a search feature to fetch employee details by ID.

Explanation:
This task focuses on dynamic data retrieval using Servlets and JDBC:

Set up a Employee table in the database with fields like EmpID, Name, and Salary.

Create a Servlet that:

Connects to the database using JDBC.
Executes a query to fetch all employee records.
Displays them in an HTML table.
Include an HTML search form where users can enter an Employee ID.

When submitted, the Servlet should fetch and display the matching employee's details.
This task demonstrates:

Servlet and JDBC integration.
Dynamic HTML generation using Java.
Handling both list and filtered views.
Part c: Student Attendance Portal Using JSP and Servlet
Objective:
To develop a simple student attendance portal using JSP for the frontend and a Servlet for handling form submissions and saving attendance data to a database.

Explanation:
This task highlights form-based data collection and backend processing using JSP and Servlets:

Use a JSP page to:

Display a form to collect attendance details (e.g., Student ID, Date, Status).
Submit the form to a Servlet using POST.
The Servlet:

Reads the attendance data from the request.
Connects to the database using JDBC.
Inserts the data into an Attendance table.
Sends a confirmation response or redirects to a success page.
This program demonstrates:

Separation of view (JSP) and controller (Servlet).
End-to-end data flow from UI to database.
Real-time web form submission and storage.
