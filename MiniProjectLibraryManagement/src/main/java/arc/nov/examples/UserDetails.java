package arc.nov.examples;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/UserDetails")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class UserDetails extends HttpServlet {

	private static final String URL = "jdbc:mysql://localhost:3306/book_db";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Connection Successfully: (To Cheak My Application Is connected With Database)");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<!Doctype html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Book Details</title>");
		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
		out.println("</head>");
		out.println("<body>");
		// ------------- file upload part------------------------
		out.println("<h2 class='table-heading'>Books Details</h2>");

		out.println("<form>");
		out.println("<input type='text' name='id' id='userid' placeholder='Enter ID' value='"
				+ (request.getParameter("id") == null ? "" : request.getParameter("id")) + "'><br><br>");
		out.println("<input type='hidden' name='option' value='id' ><br><br>");
		out.println("</form>");


		out.println("<div class='btn'>");
		out.println("<button onclick='firstData()'>  << </button>");
		out.println("<button onclick='decreaseCount()'>  < </button>");
		out.println("<button onclick='increaseCount()'>  > </button>");
		out.println("<button onclick='lastData()'>  >> </button>");
		out.println("</div>");

		// ---------------- to get last id from table ------------------------

		String lastQuery = "SELECT * FROM users ORDER BY id DESC LIMIT 1 ";

		int lastid = 0;
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			PreparedStatement pstmt = conn.prepareStatement(lastQuery);
			ResultSet rst = pstmt.executeQuery();

			while (rst.next()) {
				lastid = rst.getInt(1); // it will give last id
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("last id is: " + lastid);

		// ------------- to display 1st id record ---------------------
		out.println("<script type='text/javascript'>");
		out.println("function firstData(){");

		out.println("alert('alert first')");
//		out.println("var id = parseInt(document.getElementById('userid').value);");
		out.println("var id = 1;");
		out.println("window.location.href='UserDetails?id=' + encodeURIComponent(id) ;");
		out.println("}");
		out.println("</script>");

		// ------------- to increase id by 1 ---------------------
		out.println("<script type='text/javascript'>");
		out.println("function increaseCount(){");
		out.println("alert('alert incr')");
		out.println("var id = parseInt(document.getElementById('userid').value);");
		out.println(" id = id+1;");
		out.println("window.location.href='UserDetails?id=' + encodeURIComponent(id) ;");
		out.println("}");
		out.println("</script>");

		// ------------- to decrease id by 1 ---------------------
		out.println("<script type='text/javascript'>");
		out.println("function decreaseCount(){");
		out.println("alert('alert dec')");
		out.println("var id = parseInt(document.getElementById('userid').value);");
		out.println(" id = id-1;");
		// it changes id in url
		out.println("window.location.href='UserDetails?id=' + encodeURIComponent(id) ;");
		out.println("}");
		out.println("</script>");

		// ------------- to display last id record ---------------------
		out.println("<script type='text/javascript'>");
		out.println("function lastData(){");
		out.println("alert('alert last')");
//				out.println("var id = parseInt(document.getElementById('userid').value);");
		out.println(" var lastid = '" + lastid + "';");
		out.println("window.location.href='UserDetails?id=' + encodeURIComponent(lastid) ;");
		out.println("}");
		out.println("</script>");

		int id = Integer.parseInt(request.getParameter("id"));
		System.out.println("id is: " + id);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			String sql = "SELECT * FROM users WHERE id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rst = pstmt.executeQuery();

			if (rst.next()) {
				out.println("<div class='details'>");
				out.println("<p>Id: " + rst.getInt(1) + "</p>");
				out.println("<p>Name: " + rst.getString(2) + "</p>");
				out.println("<p>Email: " + rst.getString(3) + "</p>");
				out.println("<p>Contact: " + rst.getString(4) + "</p>");
				out.println("<p>Deposit: " + rst.getInt(5) + "</p>");
				out.println("</div>");
			} else {
				out.println("<p>No record found for ID: " + id + "</p>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.println("<a href='BookServlet' class='btn btn-primary'>Back To Home</a>");
		out.println("</body>");
		out.println("</html>");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
