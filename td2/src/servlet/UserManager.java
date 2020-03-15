package servlet;

import components.User;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserManager
 */
@WebServlet("/UserManager")
public class UserManager extends HttpServlet {
	// LISTE DES UTILISATEURS
	private static Hashtable<Integer, User> usersTable = new Hashtable<Integer, User>();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public static Hashtable<Integer, User> getUsersTable() {
		return usersTable;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// build HTML code
		String htmlRep = "<html>";

		// get list of users
		for (Map.Entry<Integer, User> entry : usersTable.entrySet()) {
			int key = entry.getKey();
			User u = entry.getValue();
			htmlRep += "<h3>-------------User " + key + "------------</h3>";
			htmlRep += "<ul>";
			htmlRep += "<li>First Name : " + u.getFirstName() + "</li><br>";
			htmlRep += "<li>Last Name : " + u.getLastName() + "</li><br>";
			htmlRep += "<li>Email : " + u.getEmail() + "</li><br>";
			htmlRep += "<li>Pwd : " + u.getPwd() + "</li><br>";
			htmlRep += "<li>Gender : " + u.getGender() + "</li><br><br><br>";
			htmlRep += "<input type=\"button\" onclick=\"javascript:window.location.href='/td2'\" value=\"back\" >";
			htmlRep += "</ul>";
		}

		// show last user's data
		htmlRep += "</html>";

		// return reponse
		response.getWriter().println(htmlRep);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// get data
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("familyname");
		String email = request.getParameter("email");
		String pwd = request.getParameter("password");
		String gender = request.getParameter("gender");
		String role = request.getParameter("role");
		String contextPath = request.getContextPath();
		System.out.println("UserManager" + contextPath);
		// create an user
		User newUser = new User(firstName, lastName, email, pwd, gender, role);
		
		//add an user
		int count = 0;
		count = usersTable.size();
		usersTable.put(count, newUser);
		
		// show result
		doGet(request, response);

	}

}
