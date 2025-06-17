package arc.nov.examples;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static String URL = "jdbc:mysql://localhost:3306/book_db";
	private static String USER = "root";
	private static String PASSWORD = "root";
	
	@Override
	public void init(ServletConfig config) throws ServletException {

		// load drivers
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Drivers Loaded Successfully");
		} catch (Exception e) {
			throw new ServletException("Unable To Load Drivers");
		}
	}
	
	
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        
	        out.println("<!Doctype html>");
	        out.println("<html>");
	        out.println("<head>");
	        out.println("<title>Login Page</title>");
			out.println("<link rel='stylesheet' type='text/css' href='css/login.css'>");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<h2>User Login</h2>");
	        out.println("<form action='user' method='post'>");
	        out.println("<input type='hidden' name='action' value='login'>");
	        out.println("<label for='username'>Username:</label>");
	        out.println("<input type='text' id='username' name='username' required><br><br>");
	        out.println("<label for='password'>Password:</label>");
	        out.println("<input type='password' id='password' name='password' required><br><br>");
	        out.println("<input type='submit' value='Login'><br><br>");
	        
	        out.println("<div class='pdiv'>");
	        out.println("<p>New User ?</p>");
	        out.println("<a href='UserServlet'> Register Here!</a>");
	        out.println("<div>");
	        
	        out.println("</form>");
	        out.println("</body>");
	        out.println("</html>");
	    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM users WHERE name=? AND password=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                out.println("<p>Login successful! Welcome, " + username + ".</p>");
                response.sendRedirect("BookServlet");
            } else {
                out.println("<p>Invalid username or password.</p>");
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Login failed: " + e.getMessage() + "</p>");
        }

    }
}
