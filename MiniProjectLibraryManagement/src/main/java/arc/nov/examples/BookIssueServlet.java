package arc.nov.examples;

import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BookIssueServlet")
public class BookIssueServlet extends HttpServlet {

	private static String URL = "jdbc:mysql://localhost:3306/book_db";
	private static String USER = "root";
	private static String PASSWORD = "root";

	@Override
	public void init(ServletConfig config) throws ServletException {

		// load drivers
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("BookIssue Drivers Loaded Successfully");
		} catch (Exception e) {
			throw new ServletException("Unable To Load Drivers");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		System.out.println("In doGet");

		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		out.println("<!Doctype html>");
		out.println("<html>");
		out.println("<head> <title>New User Registration</title>");
		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
//		<!-- Bootstrap CSS -->
		out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css\" rel=\"stylesheet\">");
		   
		out.println("</head>");

		out.println("<body>");

		// display users
		try {

			java.sql.Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM users");

			out.println("<div class='table-wrapper'>");
			
			//------------------- for user table-----------------------------
			out.println("<div class='table-section'>");
			out.println("<h1 class='table-heading'>User Details</h1>");
			out.println("<div class= 'table-container'>");
			out.println("<table border='1'>");
			out.println("<tr> <th>Id</th> <th>Name</th> <th>Email</th> <th>Contact</th></tr>");

			while (rst.next()) {
				out.println("<tr>");
				out.println("<td><a href='javascript:void(0)' onclick='userIdFun( " + rst.getInt("id") + ", \""
						+ rst.getString("name") + "\")' calss='button' > " + rst.getInt("id") + "</a></td>");

				out.println("<td>" + rst.getString("name") + "</td>");
				out.println("<td>" + rst.getString("email") + "</td>");
				out.println("<td>" + rst.getString("contact") + "</td>");
				out.println("</tr>");
			}
			out.println("</table><br><br>");
			out.println("</div>");
			out.println("</div>"); // End User Table Section

			//--------------------- for book table ---------------------------

			ResultSet rs = stmt.executeQuery("SELECT * FROM bookdata");
			
//			------------------ Book Detail Table ----------------------
			out.println("<div class='table-section'>");
			out.println("<h1 class='table-heading'>Book Details</h1>");
			out.println("<div class='table-container'>");
			
			out.println("<table border='1'>");
			out.println("<tr><th>Book Id</th> <th>Book Name</th> <th>Isbn</th> <th>Author</th> <th>Publication</th> <th>Quantity</th> <th>Aval_Quantity</th></tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td><a href='javascript:void(0)' onclick='bookIdFun(" + rs.getInt(1) + ", \""
						+ rs.getString(2) + "\", \"" + rs.getInt(7) + "\")'> " + rs.getInt(1) + "</a></td>");

//				out.println("<td><a href='javascript:void(0)' onclick='bookIdFun("+rs.getInt(1)+ ", \"" +rs.getString(2) + "\", \"" +rs.getInt(7)+ "\")' > " + rs.getInt(1) + "</a></td>");
				out.println("<td>" + rs.getString(2) + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td>" + rs.getString(4) + "</td>");
				out.println("<td>" + rs.getString(5) + "</td>");
				out.println("<td>" + rs.getString(6) + "</td>");
				out.println("<td>" + rs.getInt(7) + "</td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("</div>");
			out.println("</div>"); // End Book Table Section
			
			out.println("</div>");
			
			// for book issue data
			out.println("<div class=\"container\">");
			out.println("<h1 class='table-heading' style='text-align:center'>Book Issue Data</h1>");
			out.print("<br><form action='BookIssueServlet' method='post'>");
			out.println("<label>User Id: </label>");
			out.println("<input type='text' name='userid' id='uid' ><br><br>");
			out.println("<label>User Name: </label>");
			out.println("<input type='text' name='username' id='uname' ><br><br>");
			out.println("<label>Book Id: </label>");
			out.println("<input type='text' name='bookid' id='bkid' ><br><br>");
			out.println("<label>Book Name: </label>");
			out.println("<input type='text' name='bookname' id='bkname' >");
			out.print("<h3 id='result1' style='color:green'></h3>");
			out.print("<h3 id='result2' style='color:red'></h3>");
			
			//submit buttons
			out.println("<div class='submit'>");
			out.println("<input type='submit' value='issue' name='action' >");
			out.println("<input type='submit' value='return' name='action' >");
			out.println("<input type='submit' value='deposite' name='action' >");
			out.println("</div>");
			
			out.println("</form>");
			out.println("<a href='BookServlet' class='button'>Back To Home</a>");

			out.println("<div class='container'>");

			// for userid function
			out.println("<script type='text/javascript'>");
			out.println("function userIdFun(id, name){");
			System.out.println("in function");
//			out.println("alert('Update Started...!!!');");
			out.println("document.getElementById('uid').value=id");
			out.println("document.getElementById('uname').value=name");
			out.println("}");
			out.println("</script>");

			// book id function
			out.println("<script type='text/javascript'>");
			out.println("function bookIdFun(id, name, avlcount){");
			System.out.println("in function");
//			out.println("alert('Update Started...!!!');");
			out.println("document.getElementById('bkid').value=id");
			out.println("document.getElementById('bkname').value=name");
//			out.println("document.getElementById('bkname').value=name");
			out.println("if(avlcount>0){");
			out.println("document.getElementById('result1').innerText='Available';");
			out.println("}");
			out.println("else{");
			out.println("document.getElementById('result2').innerText='NA';");
			out.println("}");
			out.println("}");
			out.println("</script>");

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		out.println("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js\"></script>");
		
		out.println("</body>");
		out.println("</html>");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String uName = request.getParameter("username");
		String bkName = request.getParameter("bookname");
		int bkId = Integer.parseInt(request.getParameter("bookid"));
		int uId = Integer.parseInt(request.getParameter("userid"));
		LocalDate issueDate = LocalDate.now();
		LocalDate returnDate = issueDate.plusDays(8);

		System.out.println("data: " + uName + ", " + bkName + ", " + bkId + ", " + uId);

		String action = request.getParameter("action");
		System.out.println("action is: " + action);

		if (action.equals("issue")) {
			
			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
				
				//to check the number of book already issued by user
				
				String checksql = "SELECT COUNT(*) AS issued_count FROM issuedata WHERE uid = ? AND return_status='Issued' ";
				PreparedStatement checkstmt = conn.prepareStatement(checksql);
				checkstmt.setInt(1, uId);
				ResultSet rs = checkstmt.executeQuery();
				
				//to check deposite
				PreparedStatement checkDeposite = conn.prepareStatement("SELECT deposit FROM users WHERE id = ?");
				checkDeposite.setInt(1, uId);
				ResultSet depositeResult = checkDeposite.executeQuery();
				int deposite = 0;
				if(depositeResult.next()) {
					deposite = depositeResult.getInt("deposit");
					System.out.println("Deposite is: "+deposite);
				}
				
				if(deposite == 0) {
					out.println("<p style='color:red;'>"+"You cannot issue books. Please pay your deposite."+"</p>");
				}
				else
				{
					if(rs.next() && rs.getInt("issued_count") >= 3) {
						//  display a message, User has already issued 3 books
						out.println("<p style='color:red;'>"+"You cannot issue more than 3 books. Please return a book before issuing a new one."+"</p>");
					}
					else {
						isertData(request, response, out);
						displayData(request, response, out);
					}
				}
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if (action.equals("return")) {

			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

				String sql = "UPDATE issuedata SET return_status = 'returned' WHERE uid = ? AND bkid=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
//				java.sql.Statement stmt = conn.createStatement();
				pstmt.setInt(1, uId);
				pstmt.setInt(2, bkId);
				pstmt.executeUpdate();
//				ResultSet rs = pstmt.executeQuery();
				displayData(request, response, out);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(action.equals("deposite")) {
			
			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
				String query = "UPDATE users SET deposit = deposit+100 WHERE id = ?";
				PreparedStatement dstmt = conn.prepareStatement(query);
				dstmt.setInt(1, uId);
				dstmt.executeUpdate();
				out.println("<p style='color:green;'>"+"Deposite Submited Successfully."+"</p>");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void isertData(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		// TODO Auto-generated method stub
		
		System.out.println("In insert data");
		
		String uName = request.getParameter("username");
		String bkName = request.getParameter("bookname");
		int bkId = Integer.parseInt(request.getParameter("bookid"));
		int uId = Integer.parseInt(request.getParameter("userid"));
		LocalDate issueDate = LocalDate.now();
		LocalDate returnDate = issueDate.plusDays(8);
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			java.sql.Statement stmt = conn.createStatement();

			out.println("<!Doctype html>");
			out.println("<html>");
			out.println("<head> <title>Book Issue Details</title>");
			out.println("</head>");
			out.println("<body>");

			PreparedStatement pstmt = null;

			//-------------------------- to reduce book count ---------------------------------
			
			try {
				//to get available book count
				String selectQuery = "SELECT available_quantity FROM bookdata WHERE id = ?";
				PreparedStatement p = conn.prepareStatement(selectQuery);
				p.setInt(1, bkId);
				ResultSet r = p.executeQuery();

				if (r.next()) {
					int temp = r.getInt("available_quantity");
					System.out.println("Temp: " + temp);
					// redusing quentity by 1
					int newQuantity = temp - 1;

					//to update available book count
					String updateQuery = "UPDATE bookdata SET available_quantity = ? WHERE id = ?";
					PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
					updateStatement.setInt(1, newQuantity);
					updateStatement.setInt(2, bkId);
					updateStatement.executeUpdate();
				}
				
				//----------------to update deposite---------------
				// to select user id
				
				String updateDeposite = "UPDATE users SET deposit = deposit-10 WHERE id = ?";
				PreparedStatement updateStmt = conn.prepareStatement(updateDeposite);
				updateStmt.setInt(1, uId);
				updateStmt.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// data insertion
			String query = "INSERT INTO issuedata(uid ,bkid ,issue_date ,return_date) VALUES(?,?,?,?)";

			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, uId);
				pstmt.setInt(2, bkId);
				pstmt.setDate(3, Date.valueOf(issueDate));
				pstmt.setDate(4, Date.valueOf(returnDate));
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}

			out.println("</body>");
			out.println("</html>");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void displayData(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		// TODO Auto-generated method stub
		System.out.println("in display method");

		// display all data
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			java.sql.Statement stmt = conn.createStatement();

			out.println("<!Doctype html>");
			out.println("<html>");
			out.println("<head> <title>Book Issue Details</title>");
			out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
			out.println("</head>");
			out.println("<body>");

			PreparedStatement pstmt = null;

			out.println("<h1 class='table-heading'>Book Issue Data</h1>");
			out.println("<table border='1'>");
			out.println("<tr><th>Id</th> <th>User Id</th> <th>Book Id</th> <th>Issue Date</th> <th>Return Date</th> <th>Return Status</th></tr>");

			ResultSet rs = stmt.executeQuery("SELECT * FROM issuedata");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getString(1) + "</td>");
				out.println("<td>" + rs.getString(2) + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td>" + rs.getString(4) + "</td>");
				out.println("<td>" + rs.getString(5) + "</td>");
				out.println("<td>" + rs.getString(6) + "</td>");
				out.println("</tr>");
			}

			out.println("</table>");
			out.println("</body>");
			out.println("</html>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
