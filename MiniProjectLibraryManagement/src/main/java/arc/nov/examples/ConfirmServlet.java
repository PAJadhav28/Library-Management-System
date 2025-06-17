package arc.nov.examples;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ConfirmServlet")
public class ConfirmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	private static String URL = "jdbc:mysql://localhost:3306/admission_db";
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
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve selected student IDs from the form submission
        String[] selectedStudentIds = request.getParameterValues("studentIds");
        
        for(String id : selectedStudentIds) {
        	System.out.println("id: "+id);
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (selectedStudentIds != null) {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                // SQL query to update the document_status
                String updateQuery = "UPDATE degree_data SET document_status = 'Approved' WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    // Update status for each selected student ID
                    for (String studentId : selectedStudentIds) {
                        pstmt.setInt(1, Integer.parseInt(studentId));
                        pstmt.executeUpdate();
                    }
                }

                // Confirmation message
                out.println("<h3 class='text-success'>Status updated successfully for selected students.</h3>");
                out.println("<a href='DocumentVerificationServlet?action=degree' class='btn btn-primary'>Go Back</a>");
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<h3 class='text-danger'>Error updating status. Please try again later.</h3>");
            }
        } else {
            // If no checkboxes were selected
            out.println("<h3 class='text-warning'>No students selected for approval.</h3>");
            out.println("<a href='DocumentVerificationServlet?action=degree' class='btn btn-primary'>Go Back</a>");
        }
    }

}
