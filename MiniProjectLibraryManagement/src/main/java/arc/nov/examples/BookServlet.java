package arc.nov.examples;

import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/BookServlet")

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
	    maxFileSize = 1024 * 1024 * 10,      // 10 MB
	    maxRequestSize = 1024 * 1024 * 100    // 100 MB
	) 

public class BookServlet extends HttpServlet {

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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		PreparedStatement pstmt = null;

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM bookdata");

			out.println("<!doctype html>");
			out.println("<html>");
			out.println("<head> <title>Book Data</title>");
			out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
			out.println(
					"<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">");

			out.println("</head>");
			out.println("<body>");

//			 style='display: flex; justify-content: center; align-items: center; gap: 15px; margin-top: 20px;'
			
			// to display data
			// For new user registration
			out.println("<div class='btndiv'>");
			out.println("<a href='UserServlet' class='btn'>Add New User</a>");
			// For book issue
			out.println("<a href='BookIssueServlet' class='btn' >Issue Book</a>");
			
			out.println("<a href='MemberDetails' class='btn' >Book Details</a>");
			out.println("<a href='UserDetails' class='btn' >User Details</a>");
			// To add new book
			out.println("<button type='button' class='btn' onclick='addBookFun()'>Add Book</button>");
			out.println("</div>");

			
			// enter book data
			out.println("<div id='addbook' style='display:none;'>");
			out.println("<h1 class='table-heading' style='text-align:center'>Enter Book Details</h1>");
			
			out.println("<form action='BookServlet' method='post' enctype='multipart/form-data'>");
			out.println("<label>Book Image: </label>");
			out.println("<input type='file' name='image' accept='image/*' required><br><br>");
			out.println("<label>Book Name: </label>");
			out.println("<input type='text' name='name' placeholder='Enter Book Name'><br><br>");
			out.println("<label>Book ISBN: </label>");
			out.println("<input type='text' name='isbn' placeholder='Enter ISBN Number'><br><br>");
			out.println("<label>Author Name: </label>");
			out.println("<input type='text' name='author' placeholder='Enter Book Count'><br><br>");
			out.println("<label>Publication: </label>");
			out.println("<input type='text' name='publication' placeholder='Enter Book Name'><br><br>");
			out.println("<label>Book Quantity: </label>");
			out.println("<input type='text' name='quantity' placeholder='Enter Total Quantity '><br><br>");
			out.println("<label>Available Quantity: </label>");
			out.println("<input type='text' name='available_quantity' placeholder='Enter Total Quantity' ><br><br>");
			out.println("<label>Book Price: </label>");
			out.println("<input type='text' name='price' placeholder='Enter Book Price' ><br><br>");
			out.println("<input class=\"btn btn-secondary\" type='submit' value='Add'><br><br>");
			out.println("<input type='hidden' name='action' value='addbook'>");
			out.println("</form>");

			out.println("</div>");
			
			
			
			out.println("<h1 class='table-heading' style='text-align:center'>All Book Details</h1>");
			out.println("<div class='bkdiv'>");
			out.println("<table border='1'>");
			out.println("<tr> <th>Id</th> <th>Name</th> <th>Isbn</th> <th>Author</th> <th>Publication</th> <th>Quantity</th> <th>Aval_Quantity</th> <th>Price</th> <th>Image</th> </tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getString(1) + "</td>");
				out.println("<td>" + rs.getString(2) + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td>" + rs.getString(4) + "</td>");
				out.println("<td>" + rs.getString(5) + "</td>");
				out.println("<td>" + rs.getString(6) + "</td>");
				out.println("<td>" + rs.getInt(7) + "</td>");
				out.println("<td>" + rs.getString(8) + "</td>");
				out.println("<td><img src='"+rs.getString(9)+"'  height='100'  width='100'>"+"</td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("</div>");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		out.println("<script type='text/javascript'>");
		out.println("function addBookFun(){");
		out.println("document.getElementById('addbook').style.display='block'; ");
//				out.println("alert('Update Started...!!!');");
		out.println("}");
		out.println("</script>");

		// ----------------------to choose publication and price--------------------
		String selectedPublication = request.getParameter("publication");
		String selectedPrice = request.getParameter("bkprice");

		// ----------------------- for publication dropdown ------------------------

		out.println("<form action='BookServlet'>"); // call get method by default

		out.println("<br><br><label for='publishers'>Choose a Publication:</label>");
		out.println("<select id='publication' name='publication' onchange='publicationFun()'>");

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			java.sql.Statement stmt;
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT DISTINCT publication FROM bookdata");

			// default option
			out.print("<option value='" + (selectedPublication == null ? "selected" : "") + "'>publication </option>");

			// drop down with database value
			while (rst.next()) {
				String publication = rst.getString("publication");
				out.print("<option value='" + publication + "' "
						+ (publication.equals(selectedPublication) ? "selected" : "") + ">" + publication
						+ "</option>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("</select>");

		// ---------------------- for price drop down -------------------------------

		out.println("<br><br><label>Choose a Price:</label>");
		out.println("<select name='bkprice' id='bkprice' onchange='publicationFun()'>");

		out.println("<option value='' " + (selectedPrice == null ? "selected" : "") + ">Select</option>");
		out.println("<option value='<=100' " + ("<=100".equals(selectedPrice) ? "selected" : "") + "><= 100</option>");
		out.println("<option value='<=200' " + ("<=200".equals(selectedPrice) ? "selected" : "") + "><= 200</option>");
		out.println("<option value='<=300' " + ("<=300".equals(selectedPrice) ? "selected" : "") + "><= 300</option>");
		out.println("<option value='<=500' " + ("<=500".equals(selectedPrice) ? "selected" : "") + "><= 500</option>");
		out.println("<option value='>=500' " + (">=500".equals(selectedPrice) ? "selected" : "") + ">>= 500</option>");

		out.println("</select>");
		out.println("</form>");

		// Available books
		// Available books
		out.println("<h1 class='table-heading' style='text-align:center'>Available books</h1>");
		try (Connection conn1 = DriverManager.getConnection(URL, USER, PASSWORD)) {

			String sql = "SELECT * FROM bookdata WHERE 1=1";

			if (selectedPublication != null && !selectedPublication.isEmpty()) {
				sql += " AND publication = ?"; // Add condition if publisher selected
			}
			if (selectedPrice != null && !selectedPrice.isEmpty()) {
				sql += " AND price " + selectedPrice; // Add condition for price range if selected
			}

			PreparedStatement pstmt1 = conn1.prepareStatement(sql);
			int paramIndex = 1;

			if (selectedPublication != null && !selectedPublication.isEmpty()) {
				pstmt1.setString(paramIndex++, selectedPublication);
			}

			System.out.println("queryy: " + sql);
			ResultSet rs = pstmt1.executeQuery();
			out.println("<div class='bkdiv'>");
			out.println("<table border='1'>");
			out.println(
					"<tr> <th>ID</th> <th>Name</th> <th>Isbn</th> <th>Author</th> <th>Publication</th> <th>Total Quantity</th> <th>Available Quantity</th> <th>Price</th> <th>Image</th> </tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getInt(1) + "</td>");
				out.println("<td>" + rs.getString(2) + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td>" + rs.getString(4) + "</td>");
				out.println("<td>" + rs.getString(5) + "</td>");
				out.println("<td>" + rs.getInt(6) + "</td>");
				out.println("<td>" + rs.getInt(7) + "</td>");
				out.println("<td>" + rs.getString(8) + "</td>");
				out.println("<td><img src='"+rs.getString(9)+"'  height='100'  width='100'></td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("</div>");

			// JavaScript function to handle dropdown selection
			out.println("<script type='text/javascript'>");
			out.println("function publicationFun() {");
			out.println("var publication = document.getElementById('publication').value; ");
			out.println("var bkprice = document.getElementById('bkprice').value; ");
//			out.println("window.location.href = 'BookServlet?publication=' + encodeURIComponent(publication) + '&price=' + encodeURIComponent(price); "); 
			out.println(
					"window.location.href='BookServlet?publication=' + encodeURIComponent(publication) + '&bkprice=' + encodeURIComponent(bkprice);");
			out.println("}");
			out.println("</script>");
			out.println("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>");
			out.println("</body>");
			out.println("</html>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		// =========================================
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("In post");
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("In dopost");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String name = request.getParameter("name");
		String isbn = request.getParameter("isbn");
		String author = request.getParameter("author");
		String publication = request.getParameter("publication");
		String quantity = request.getParameter("quantity");

//		int available_quantity = Integer.parseInt(request.getParameter("available_quantity"));
		String available_quantity = request.getParameter("available_quantity");
		String price = request.getParameter("price");

		// check which option is selected
		String action = request.getParameter("action");
		System.out.println("action is: " + action);

		// for publication dropdown
//		String publicationName = request.getParameter("publicationDropdown");
//		System.out.println("Publication: "+publicationName);

		if (action.equals("addbook")) {
			addBook(out, request, response);
		} else if (action.equals("hiddenPublication")) {
			findPublication(out, request, response);

		}

	}

	private void findPublication(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {

		PreparedStatement pstmt = null;
		Connection conn = null;

		String publicationName = request.getParameter("publicationDropdown");
		System.out.println("Publication: " + publicationName);

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		out.println("<!doctype html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Data</title>");
		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
		out.println("</head>");
		out.println("<body>");
		try {

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM bookdata WHERE publication = ?");
			ps.setString(1, publicationName);
			ResultSet rs = ps.executeQuery();

			out.println("<h1 class='table-heading'>Data</h1>");
			out.println("<table border='1'>");
			out.println("<tr> <th>Id</th> <th>Name</th> <th>Isbn</th> <th>Author</th> <th>Publication</th> <th>Quantity</th> <th>Aval_Quantity</th> <th>Price</th> <th>Image</th> </tr>");
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getString(1) + "</td>");
				out.println("<td>" + rs.getString(2) + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td>" + rs.getString(4) + "</td>");
				out.println("<td>" + rs.getString(5) + "</td>");
				out.println("<td>" + rs.getString(6) + "</td>");
//				out.println("<td>"+rs.getInt(7)+"</td>");
				out.println("<td>" + rs.getString(7) + "</td>");
				out.println("<td>" + rs.getString(8) + "</td>");
				out.println("<td>" + rs.getString(9) + "</td>");
				out.println("</tr>");
			}

			out.println("</table>");

			out.println("</body>");
			out.println("</html>");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	private void addBook(PrintWriter out, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String name = request.getParameter("name");
		String isbn = request.getParameter("isbn");
		String author = request.getParameter("author");
		String publication = request.getParameter("publication");
		String quantity = request.getParameter("quantity");
//		int available_quantity = Integer.parseInt(request.getParameter("available_quantity"));
		String available_quantity = request.getParameter("available_quantity");
		String price = request.getParameter("price");
		
		String uploadPath = "D:/JavaWorkspace/MiniProjectLibraryManagement/src/main/webapp/bookimages";
		
		Part filePart = request.getPart("image");
		String fileName = filePart.getSubmittedFileName();
		
//		String uploadPath = "/images"; // Adjust the path to your needs
		filePart.write(uploadPath + "/" + fileName);
		String img_url = "bookimages/" + fileName;

		out.println("<!doctype html>");
		out.println("<html>");
		out.println("<head> <title>Data</title>");
		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
		out.println("</head>");
		out.println("<body>");

		// ==================================
		String sqlquery = "INSERT INTO bookdata(name, isbn, author, publication, quantity, available_quantity,price,img) VALUES(?,?,?,?,?,?,?,?)";

		try {
			// data insertion
			pstmt = conn.prepareStatement(sqlquery);
			pstmt.setString(1, name);
			pstmt.setString(2, isbn);
			pstmt.setString(3, author);
			pstmt.setString(4, publication);
			pstmt.setString(5, quantity);
//			pstmt.setInt(6, available_quantity);
			pstmt.setString(6, available_quantity);
			pstmt.setString(7, price);
			pstmt.setString(8, img_url);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("</body>");
		out.println("</html>");
		response.sendRedirect("BookServlet");
	}
}
