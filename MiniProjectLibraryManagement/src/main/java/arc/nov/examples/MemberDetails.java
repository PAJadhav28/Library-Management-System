package arc.nov.examples;

import java.beans.Statement;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/MemberDetails")

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
	    maxFileSize = 1024 * 1024 * 10,      // 10 MB
	    maxRequestSize = 1024 * 1024 * 100    // 100 MB
	) 
public class MemberDetails extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

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
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String uploadPath = "/images";
        
        out.println("<!Doctype html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>File Upload Form</title>");

		out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        
        //------------- file upload part------------------------
       /*
        out.println("<h2>Enter Books Details</h2>");
        
    	out.println("<form action='MemberDetails' method='post' enctype='multipart/form-data'>");
    	out.println("<label for='image'>Select Image:</label>");
    	out.println("<input type='file' name='image' accept='image/*' required><br><br>");
		out.println("Book Name:<input type='text' name='name' placeholder='Enter The Book Name'> <br><br>");
		out.println("<input type='submit' value='Upload' ><br><br>");
		out.println("<input type='hidden' name='action' value='upload' ><br><br>");
        out.println("</form>");
        */
        
      //----------------- for check boxes------------------------
    /*    out.println("<form action='MemberDetails' method='post' >");
        out.println("<label for='id'>Id</label>");
        
        out.println("<br><br><input type='checkbox' name='option' value='id'>Id");
        out.println("<input type='checkbox' name='option' value='img_name'>Name");
        out.println("<input type='checkbox' name='option' value='img'>Img");
        out.println("<input type='hidden' name='action' value='hiddencheck'>");
        out.println("<br><br><input type='submit' name='check' value='check'>");
        out.println("</form>");*/
        
        out.println("<form action='MemberDetails'>");
        out.println("    <br><br><input type='checkbox' id='id' name='option' value='id'>");
        out.println("    <label for='id'>Id</label>");
        out.println("    <input type='checkbox' id='name' name='option' value='name'>");
        out.println("    <label for='name'>Name</label>");
        out.println("    <input type='checkbox' id='img' name='option' value='img'>");
        out.println("    <label for='img'>Img</label>");
        out.println("    <input type='hidden' name='action' value='hiddencheck'>");
        out.println("    <br><br><input type='submit' name='check' value='check'>");
        out.println("</form>");

		out.println("<a href='BookServlet' class='btn btn-primary'>Back To Home</a>");
        
        
        //--------------------Table to  Display bookimage Record-------------------
   /*     try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD))
        {
        	String displayQuery = "select * from bookimages;";
        	PreparedStatement stmt = conn.prepareStatement(displayQuery);
		    ResultSet rs = stmt.executeQuery();
	        out.println("<table border='1'>");
			out.println("<tr> <th>ID</th> <th>Name</th> <th>BookImage</th> </tr>");
			
			while(rs.next())
			{
				out.println("<tr>");
				out.println("<td> " + rs.getInt(1) + "</td>");
				out.println("<td> " + rs.getString(2) + "</td>");
//				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td><img src='"+rs.getString(3)+"'  height='100'  width='100'></td>");
				out.println("</tr>");
				out.println("</tr>");
			}
			out.println("</table>");
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        */
        
        
        String []selectedColumns = request.getParameterValues("option");
		
		if(selectedColumns.length == 0 || selectedColumns == null) {
			out.println("No column selected.");
			return;
		}
		
		//create dynamic sql query
		
		StringBuilder sql = new StringBuilder("SELECT ");
		
		for(int i=0; i<selectedColumns.length ; i++) {
			sql.append(selectedColumns[i]);
			if(i < selectedColumns.length - 1) {
				sql.append(", ");
			}
		}
		sql.append(" FROM bookdata");
		System.out.println("sql query: "+sql.toString());
		
		//execute the query and display the result
		
		try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString())) {
			
			java.sql.Statement s = conn.createStatement();
			ResultSet rst = s.executeQuery("Select id FROM bookdata");
			
			 // Get column names from ResultSet metadata
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int columnCount  = rsMetaData.getColumnCount();
			
			 // Display column headers
			out.println("<table border='1'>");
			out.println("<tr>");
			for(int i=1; i<=columnCount ; i++) {
				out.println("<th>"+rsMetaData.getColumnName(i)+"</th>");
			}
			out.println("</tr>");
			
			//display rows
			while(rs.next() && rst.next()) {
				out.println("<tr>");
				for(int i=1; i<=columnCount ; i++) {
					String columnName = rsMetaData.getColumnName(i);
					int id = rst.getInt("id");
					System.out.println("img id is: "+id);
					if("img".equals(columnName)) {
						System.out.println("====================");
						 out.println("<td><img src='"+rs.getString(i)+"'  height='100' width='100'></td>");
//						out.println("<td><img src='ImageServlet?id="+id+"' alt='"+rs.getString(i)+"' height='100'  width='100'></td>");
					}
					else {
						System.out.println("--------------");
						out.println("<td>"+rs.getString(i)+"</td>");
					}
				}
				out.println("</tr>");
			}
			out.println("</table>");
        
        out.println("</body>");
        out.println("</html>");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
/*		//Get The Data
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String action = request.getParameter("action");
		System.out.println("action: "+action);
		
		String uploadPath = "D:/JavaWorkspace/MiniProjectLibraryManagement/src/main/webapp/bookimages/";
		
		if(action.equals("upload")) {
			
			String name = request.getParameter("name");
			Part filePart = request.getPart("image");
			
			System.out.println("name: "+name+"  file: "+uploadPath);
			
			String fileName = filePart.getSubmittedFileName();
			
//			String uploadPath = "/images"; // Adjust the path to your needs
			filePart.write(uploadPath + "/" + fileName);
			String img_url = "bookimages/" + fileName;
			
			
			try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD))
			{
				String insertQuery ="INSERT INTO bookimages(img_name,img)values(?,?)";
				PreparedStatement stmt = conn.prepareStatement(insertQuery);
				stmt.setString(1, name);
				stmt.setString(2, img_url);

				stmt.executeUpdate();
				System.out.println("Data Inserted Successfully...");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.sendRedirect("MemberDetails");
		}
		
		else if(action.equals("hiddencheck")) {
			
			String []selectedColumns = request.getParameterValues("option");
			
			if(selectedColumns.length == 0 || selectedColumns == null) {
				out.println("No column selected.");
				return;
			}
			
			//create dynamic sql query
			
			StringBuilder sql = new StringBuilder("SELECT ");
			
			for(int i=0; i<selectedColumns.length ; i++) {
				sql.append(selectedColumns[i]);
				if(i < selectedColumns.length - 1) {
					sql.append(", ");
				}
			}
			sql.append(" FROM bookimages");
			System.out.println("sql query: "+sql.toString());
			
			//execute the query and display the result
			
			try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
				java.sql.Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString())) {
				
				java.sql.Statement s = conn.createStatement();
				ResultSet rst = s.executeQuery("Select id FROM bookimages");
				
				 // Get column names from ResultSet metadata
				ResultSetMetaData rsMetaData = rs.getMetaData();
				int columnCount  = rsMetaData.getColumnCount();
				

				
				out.println("<!Doctype html>");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>Book Data<title>");
				out.println("<link rel='stylesheet' type='text/css' href='css/style.css'>");
				out.println("<head>");
				out.println("<body>");
				 // Display column headers
				out.println("<table border='1'>");
				out.println("<tr>");
				for(int i=1; i<=columnCount ; i++) {
					out.println("<th>"+rsMetaData.getColumnName(i)+"</th>");
				}
				out.println("</tr>");
				
				//display rows
				while(rs.next() && rst.next()) {
					out.println("<tr>");
					for(int i=1; i<=columnCount ; i++) {
						String columnName = rsMetaData.getColumnName(i);
						int id = rst.getInt("id");
						System.out.println("img id is: "+id);
						if("img".equals(columnName)) {
							System.out.println("====================");
							 out.println("<td><img src='"+rs.getString("img")+"' alt='Image' height='100' width='100'></td>");
//							out.println("<td><img src='ImageServlet?id="+id+"' alt='"+rs.getString(i)+"' height='100'  width='100'></td>");
						}
						else {
							System.out.println("--------------");
							out.println("<td>"+rs.getString(i)+"</td>");
						}
					}
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("/body");
				out.println("/html");
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		*/
	}
	
}