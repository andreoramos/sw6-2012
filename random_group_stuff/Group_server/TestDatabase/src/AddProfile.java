

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;



/**
 * Servlet implementation class AddProfile
 */
public class AddProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] appList;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddProfile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		ArrayList<Department> departments = new ArrayList<Department>();
		ArrayList<App> apps = new ArrayList<App>();
		ArrayList<App> appLink = new ArrayList<App>();

		String firstname =  request.getParameter("firstname");
		if (firstname == null)
			firstname = "";
		String middlename = request.getParameter("middlename");
		if (middlename == null)
			middlename = "";
		String surname = request.getParameter("surname");
		if (surname == null)
			surname = "";
		String phone = request.getParameter("phone");
		if (phone == null)
			phone = "";
		String role = String.valueOf((request.getParameter("prole")));
		if (role == null)
			role = "";
		String deptField = String.valueOf((request.getParameter("dept")));
		if (deptField == null)
			deptField = "";
		String file = request.getParameter("file");
		if (file == null)
			file = "";
		String username = request.getParameter("username");
		if (username == null)
			username = "";
		String password = request.getParameter("password");
		if (password == null)
			password = "";
		String password2 = request.getParameter("password2");
		if (password2 == null)
			password2 = "";
		String hasTriedToAdd = request.getParameter("tryToAdd");

		if (hasTriedToAdd == null)
			hasTriedToAdd = "0";

		Connection con = null;  
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con =DriverManager.getConnection 
					("jdbc:mysql://172.25.11.65:3306/04","eder","123456");

			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from Department;");
			// displaying records
			while(rs.next()){
				int dId = rs.getInt("idDepartment");
				String dName = rs.getString("name");
				String dAddress = rs.getString("address");
				int dPhone = rs.getInt("phone");
				String dEmail = rs.getString("email");
				Department dept = new Department(dId, dName, dAddress, dPhone, dEmail);
				departments.add(dept);
			}

			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery("select * from Apps;");
			// displaying records
			while(rs1.next()){
				int appIP = rs1.getInt("idApp");
				String appName = rs1.getString("name");
				String appVersion = rs1.getString("version");
				App app = new App(appIP, appName, appVersion);
				apps.add(app);
			}
		} catch (SQLException e) {
			throw new ServletException("Servlet Could not display records.", e);
		} 	catch (ClassNotFoundException e) {
			throw new ServletException("JDBC Driver not found.", e);
		} finally 
		{
			try 
			{
				if(rs != null) 
				{
					rs.close();
					rs = null;
				}
				if(stmt != null) 
				{
					stmt.close();
					stmt = null;
				}
				if(con != null) 
				{
					con.close();
					con = null;
				}
			} 
			catch (SQLException e) 
			{

			}
		}




		if (firstname.equals("")  || surname.equals("") || phone.equals("") || role.equals("") || username.equals("") || password.equals("") || password2.equals(""))
		{
			out.println(""+
					"<html>" +
					"<head>" +
					"<title>Savannah 1.0 - Tilf�j profil</title>" +
					"<link rel='stylesheet' type='text/css' href='CSS/SavannahStyle.css' />" +
					"</head>" +
					"<body>" +
					"<script type='text/javascript'>"+
					"function updateTryToAdd()" +
					"{"+
					"document.addForm1.tryToAdd.value = '1';"+
					"}"+
					"function submitform()"+
					"{"+
					"document.addForm1.submit();"+
					"}"+
					"</script>"+
					"<div id=\"mainBackground\">"+
					"<script>" +
					"fillOutForm();"+
					"</script>" +
					"<center><h2> Tilf�j profil " +deptField+ "</h2><br>");
			String userOutput = (String) session.getAttribute("ADDPROFILERESPONSE");
			if (userOutput != null && !userOutput.equals(""))
				out.print(userOutput+"<br>");
			out.println("</center>" +
					"<br>");

			/*if(appList != null)
			{
				for (int i=0;i<appList.length; i++) {
					out.println("<li>" + appList[i]);
				}
			}
			 */

			out.println("<table border='0'>" +
					"<form method='POST' name='addForm1' action='AddProfile'>");
			if (!departments.isEmpty())
			{
				out.println("<tr>" +
						"<td>Afdeling</td><td>"+
						"<select name='dept'>");
				for (Department d : departments) {
					out.println("<option value='"+d.getID()+"'");
					if(deptField.equals(String.valueOf(d.getID())))
						out.print(" selected='true' " );
					out.print("'>"+d.getName());
				}
				out.println("</select>"+
						"</tr>");
			}
			out.println("<tr>" +
					"<td>Navn:</td> <td><input type='text' name='firstname' value='"+firstname+"'/>");

			if (firstname != null && firstname.equals("") && hasTriedToAdd.equals("1"))
				out.println("<font color='red'>Indtast navn</font>");

			out.println("</td></tr>" +
					"<tr>" +
					"<td>Mellemnavn(e):</td> <td> <input type='text' name='middlename' value='"+middlename+"' /> </td>" +
					"</tr>" +
					"<tr>" +
					"<td>Efternavn: </td><td><input type='text' name='surname'value='"+surname+"' />");

			if (surname != null && surname.equals("") && hasTriedToAdd.equals("1"))
				out.println("<font color='red'>Indtast efternavn</font>");

			out.println("</td></tr>" +
					"<tr>" +
					"<td>Telefon nummer: </td> <td> <input type='text' name='phone' value='"+phone+"' />");

			if (phone != null && phone.equals("") && hasTriedToAdd.equals("1"))
				out.println("<font color='red'>Indtast telefon nummer</font>");

			out.println("</td></tr>" +
					"<tr>" +
					"<td><select name='prole'>" +
					"<option value='0' "); 
			if(role.equals("0"))
				out.print("selected='true'");
			out.print(">Administrator</option>" +
					"<option value='1' ");
			if(role.equals("1"))
				out.print("selected='true'");
			out.print(">Pedagog</option>" +
					"<option value='2' ");
			if(role.equals("2"))
				out.print("selected='true'");
			out.print(">For�ldre</option>" +
					"<option value='3' ");
			if(role.equals("3"))
				out.print("selected='true'");
			out.print(">Barn</option>" +
					"</select></td> " +
					"</tr>" +
					"<tr>" +
					"<td><input type='hidden' name='MAX_FILE_SIZE' value='100' />" +
					"<input name='file' type='file' /></td> " +
					"</tr>" +
					"<tr>" +
					"<td>Brugernavn: </td><td><input type='text' name='username' value='"+username+"' />");
			if (username != null && username.equals("") && hasTriedToAdd.equals("1"))
				out.println("<font color='red'>Indtast brugernavn</font>");
			out.println("</td></tr>" +
					"<tr>" +
					"<td>Kodeord:</td><td> <input type='password' name='password' />");
			if (password != null && password.equals("") && hasTriedToAdd.equals("1"))
				out.println("<font color='red'>Indtast kodeord</font>");
			out.println("</td></tr>" +
					"<tr>" +
					"<td>Gentag kodeord:</td> <td> <input type='password' name='password2' />");
			if (password2 != null && password2.equals("") && hasTriedToAdd.equals("1"))
				out.println("<font color='red'>Indtast kodeord</font>");
			out.println("</td></tr>" +
					"<tr>"+
					"<td><hr></td><td><hr></td>"+
					"</tr>" +
					"<tr>" +
					"<td>Adgang til apps</td><td>");
			for (App app : apps) {
				if (appLink.contains(app.getID()))
				{
					out.println("<input type='checkbox' name=\"appList\" value=\""+app.getID()+"\" checked='true'/>" + app.getName()+"<br>");
				}
				else
				{
					out.println("<input type='checkbox' name=\"appList\" value=\""+app.getID()+"\"/>" + app.getName()+"<br>");
				}

			}
			out.println("</td></tr>" +
					"<tr>" +
					"<td></td><td><input type='button' onClick=\"updateTryToAdd(); submitform();\" value='Tilf�j'>" +
					"<input type='button' value='Fortryd'></td>" +
					"</tr>" +
					"<input type='hidden' name='tryToAdd' value='0'>" +
					"</form>" +
					"</table>" + 
					"</div>"+
					"</body>" +
					"</html>");
		}
		else
		{
			int tempId = -1;
			int temp = -1;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con =DriverManager.getConnection 
						("jdbc:mysql://172.25.11.65:3306/04","eder","123456");

				Statement stmt0 = con.createStatement();
				String tempUsername = null;
				ResultSet rs0 = stmt0.executeQuery("select * from AuthUsers where username = '"+username+"';");
				while(rs0.next())
				{
					tempUsername = rs0.getString("username");
				}

				if (tempUsername != null)
				{
					session.setAttribute("ADDPROFILERESPONSE", "<font color='red'>Brugernavnet "+username + " findes allerede</font>");
					firstname = "";
					middlename = "";
					surname ="";
					role = "";
					file = "";
					username = "";
					password = "";
					password2 = "";
					deptField = "";
					phone = "";
				}
				else
				{
					PreparedStatement pst = con.prepareStatement("insert into AuthUsers values(?,?,?,?,?)");
					pst.setString(1,"" + System.currentTimeMillis() + "");
					pst.setString(2,null);
					pst.setString(3,role);
					pst.setString(4,username);
					pst.setString(5,password);
					int i = pst.executeUpdate();

					con =DriverManager.getConnection 
							("jdbc:mysql://172.25.11.65:3306/04","eder","123456");
					stmt = con.createStatement();
					rs = stmt.executeQuery("select * from AuthUsers where username = '"+username+"';");

					while (rs.next())
					{
						tempId = rs.getInt("idUser");
					}

					PreparedStatement pst1 = con.prepareStatement("insert into Profile values(?,?,?,?,?,?,?,?)");
					if (middlename.equals(""))
						middlename = null;

					pst1.setInt(1,tempId);
					pst1.setString(2,firstname);
					pst1.setString(3,surname);
					pst1.setString(4,middlename);
					pst1.setString(5,role);
					pst1.setString(6,phone);
					pst1.setString(7,file);
					pst1.setString(8,null);

					int i1 = pst1.executeUpdate();


					PreparedStatement pst2 = con.prepareStatement("insert into ListOfApps values(?,?,?,?)");
					for (int appId=0;appId < appList.length;appId++) {
						if (appList[i] != null)
						{
							byte[] byteArray = {1,2,3,4,5};
							Blob myBlob = new SerialBlob(byteArray);
							
							temp = Integer.parseInt(appList[appId]);
							pst2.setInt(1, temp);
							pst2.setInt(2, tempId);
							pst2.setBlob(3, myBlob);
							pst2.setBlob(4, myBlob);

							pst2.executeUpdate();

						}
					}





					session.setAttribute("ADDPROFILERESPONSE", "<font color='green'>"+firstname + " " + surname + " oprettet</font>");
					firstname = "";
					middlename = "";
					surname ="";
					role = "";
					file = "";
					username = "";
					password = "";
					password2 = "";
					deptField = "";
					phone = "";
				}
			} catch (SQLException e) {
				throw new ServletException(e.getMessage() + " Servlet Could not display records. " + temp, e);
			} 	catch (ClassNotFoundException e) {
				throw new ServletException("JDBC Driver not found.", e);
			}
			finally 
			{
				try 
				{
					if(rs != null) 
					{
						rs.close();
						rs = null;
					}
					if(stmt != null) 
					{
						stmt.close();
						stmt = null;
					}
					if(con != null) 
					{
						con.close();
						con = null;
					}
				} 
				catch (SQLException e) 
				{

				}
			}

			response.sendRedirect("AddProfile");
		}

	}


	private String getParameter(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest aRequest, 
			HttpServletResponse aResponse) 
					throws IOException, ServletException { 
		appList =  aRequest.getParameterValues("appList");
		doGet(aRequest, aResponse); 
	} 

}
