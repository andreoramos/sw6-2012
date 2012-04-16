/** 
 * This program is an example from the book "Internet 
 * programming with Java" by Svetlin Nakov. It is freeware. 
 * For more information: http://www.nakov.com/books/inetjava/ 
 */ 
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.catalina.connector.Request;
import org.apache.tomcat.dbcp.dbcp.DbcpException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabase extends HttpServlet { 
	/**
	 * 
	 */
	HttpSession session;
	private static final long serialVersionUID = 1L;
	String nextLocation = "";
	String openVar;

	public void doGet(HttpServletRequest aRequest, 
			HttpServletResponse aResponse) 
					throws IOException, ServletException {
		session = aRequest.getSession();
		String username = aRequest.getParameter("username"); 
		String password = aRequest.getParameter("password");
		nextLocation = aRequest.getParameter("next");
		String DBpassword = null;
		String DBusername = null;

		openVar = aRequest.getParameter("jsOpenVar");
		if (openVar == null)
			openVar = "0";


		String DBID = "-1";


		PrintWriter out = aResponse.getWriter();

		// connecting to database
		Connection con = null;  
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con =DriverManager.getConnection 
					("jdbc:mysql://172.25.11.65:3306/04","eder","123456");
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT password, idProfile, username from Profile where username = '" + username +"'");
			// displaying records
			while(rs.next()){
				//out.print("\t\t\t");
				DBpassword = rs.getString("password");
				DBID = rs.getString("idProfile");
				DBusername = rs.getString("username");
				//out.print("<br>");
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


		if ((username==null) || (password==null))
		{ 
			session.setAttribute("TriedLOGIN", "0");
			showLoginForm("Brugerlogin p�kr�vet: ", out); 
		}
		else if (password.equals(DBpassword) && username.equals(DBusername)) 
		{ 

			session.setAttribute("USER", username);
			session.setAttribute("ID", DBID);
			//session.removeAttribute("TriedLOGIN");
			//session.setAttribute("ID", DBID);
			//out.println("<h1>GRATZ!</h1> " + username + " " + DBID);

			if (nextLocation == null)
			{
				out.println("<html>ERROR</html>");
			}
			else if (nextLocation.equals("profile"))
				aResponse.sendRedirect("main"); 
		} 
		else 
		{ 
			session.setAttribute("TriedLOGIN", "1");
			showLoginForm("Ugyldigt login, pr�v venligst igen:", out); 
		} 
	}


	public void doPost(HttpServletRequest aRequest, 
			HttpServletResponse aResponse) 
					throws IOException, ServletException { 
		doGet(aRequest, aResponse); 
	} 

	private void showLoginForm( 
			String aCaptionText, PrintWriter aOutput) {
		String triedLogin =  (String) session.getAttribute("TriedLOGIN");
		aOutput.println( 
				"<html>" +
						"<head>" +
						"<title>Savannah 1.0</title>" +
						"<script src=\"javascript/popup.js\">"+
						"</script>" + 
						"<link rel='stylesheet' type='text/css' href='CSS/SavannahStyle.css' />" +
				" </head>");
		if (triedLogin.equals("1"))
		{
			aOutput.println("<body onLoad=\"setOpen("+openVar+"); testThis('profile');popup('popUpDiv');getFocus();\">");
		}
		else
		{
			aOutput.println("<body>");

		}		
		aOutput.println("<div id=\"mainBackground\">");
		aOutput.println("<center><h2> Velkommen!</h2>");
		aOutput.println("<br>");
		aOutput.println("<SCRIPT language = JavaScript> "+
				"var open = 0;"+
				"function setOpen(number)"+
				"{"+
				"open = number;"+
				"document.DasForm.jsOpenVar.value = number;"+
				"}"+
				"function testThis(link)"+
				"{"+
				"document.DasForm.next.value = link"+
				"}"+

				"function getFocus()" +
				"{"+
				"document.DasForm.username.focus();" +
				"}"+
				"function clearForm()" +
				"{"+
				"document.DasForm.username.value =\"\";"+
				"document.DasForm.password.value= \"\";"+
				"}"+
				"document.onkeydown = function(e) {"+
				" e = e || window.event;" +
				"var keyCode = e.keyCode || e.which;" +
				//If in login and esc is pressed
				"if(keyCode == 27 && open == 1) {setOpen(0); testThis('profile');popup('popUpDiv');  clearForm(); }" +
				//If in add and esc is pressed
				"if(keyCode == 27 && open == 2) {setOpen(0); popup('popUpPick'); }" +
				
				//MainScreen Key presses
				"if(keyCode == 80 && open == 0) {window.location = \"SelectProfile\"}" + // P = redirect to SelectProfile
				"if(keyCode == 84 && open == 0) {setOpen(2); popup('popUpPick')}" + //T = show add window											//Nice little feature for waiting 25 ms		
				" if(keyCode == 76 && open == 0) {setOpen(1); clearForm(); testThis('profile');popup('popUpDiv'); document.DasForm.username.value =\"\"; setTimeout(function(){getFocus();clearForm();}, 25); }" + // L = Show login
				"}"+
				"</SCRIPT>");
		aOutput.println("<hr>");
		aOutput.println("V�lg handling");
		aOutput.println("<p>");
		aOutput.println("<a href=\"SelectProfile\"><img src=\"images/i.jpg\" ALT=\"test\"></a>");
		aOutput.println("<br>");
		aOutput.println("<a href=\"#\" onclick=\"testThis('profile');popup('popUpDiv');getFocus();\"><b>P</b>rofiler</a>");
		aOutput.println("<p>");
		aOutput.println("<a href=\"#\" onclick=\"popup('popUpPick');\"><b>T</b>ilf�j</a>  -  Rediger  -  Slet");
		aOutput.println("</center>");
		aOutput.println("<p>");
		aOutput.println("<a href=\"#\" onclick=\"testThis('profile');popup('popUpDiv');getFocus();\">Hurtig <b>L</b>ogin</a>");
		aOutput.println("<hr>");
		aOutput.println("<footer> Savannah v. 1.0.0 (C)opyright me!</footer> </div>");
		//out.println("<form method='POST' action='main'>\n" +
		//"<input type='hidden' name='Logout'>"+
		//"<input type='submit' value='Logout'>\n" + "</form>"); 
		aOutput.println("" +
				"<div id=\"blanket\" style=\"display:none;\"></div>"+
				"<div id=\"popUpDiv\" style=\"display:none;\">"+
				"<P align=\"right\"><a href=\"#\" onclick=\"setOpen(0); popup('popUpDiv')\" ALIGN=RIGHT>[X]</a></p>"+
				"<form method='POST' action='TestDatabase' name='DasForm'>\n" + 
				"<center><h3>"+
				aCaptionText +
				"</h3>"+
				"<br>\n" + 
				"<table border=\"0\">"+
				"<tr>"+
				"<td>Brugernavn:<td><input type='text' name='username' autocomplete='off'><br>\n" +
				"</tr>"+
				"<tr>"+
				"<td>Kodeord:<td><input type='password' name='password'><br>\n" +
				"</tr>"+
				"<tr>" +
				"<td><input type='hidden' name='next'><input type='hidden' name='jsOpenVar'><br>\n"+
				"</tr>"+
				"</table>"+
				//"<tr>"+
				"<td><input type='submit' value='Login'><td><input type='button' value='Fortryd' onClick=\"setOpen(0); clearForm();popup('popUpDiv')\">\n" +
				//"</tr>"+
				
				"</center>"+
				"</div>");
		aOutput.println("<div id=\"blanket\" style=\"display:none;\"></div>"+
		"<div id=\"popUpPick\" style=\"display:none;\">"+
		"<P align=\"right\"><a href=\"#\" onclick=\"setOpen(2);popup('popUpPick')\" ALIGN=RIGHT>[X]</a></p>"+
		"<form method='POST' action='TestDatabase' name='DasForm'>\n" + 
		"<center><h3>"+
		"Tilf�j:"+
		"</h3>"+
		"Profil  -  Billede  -  Lyd  -  Animation/film"+
		"</center>"+
		"</div>" +
				""+
				"</body></form></html>" 
				); 
	}

}