package servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import components.User;

/**
 * Servlet implementation class UserManager
 */
@WebServlet("/UserManager")
public class UserManager extends HttpServlet {
	// LISTE DES UTILISATEURS
	private static Hashtable<Integer, User> usersTable = new Hashtable<Integer, User>();

	private static final long serialVersionUID = 1L;

	public UserManager() {
		super();
		// TODO Auto-generated constructor stub
	}

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
			htmlRep += "</ul>";
		}

		// show last user's data
		htmlRep += "<form action=\"/td2/UserManager\" method=\"post\">";
		htmlRep += "<input type=\"submit\" name=\"Continuer\" value=\"Back to home\" >";
		htmlRep += "</form>";
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

		// Récupérer des données depuis la requête de UserVerificaton
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("familyname");
		String email = request.getParameter("email");
		String pwd = request.getParameter("password");
		String gender = request.getParameter("gender");
		String role = request.getParameter("role");
		String btnSumit = request.getParameter("submit");
		String btnContinue = request.getParameter("continue");
		String btnGoToLogin = request.getParameter("login");
		String contextPath = request.getContextPath();
		System.out.println("UserManager - doPost - ContextPath : " + contextPath);
		System.out.println("UserManager - usersTable size : " + usersTable.size());

		if ("Submit".equals(btnSumit) || "Continue".equals(btnContinue)) {
			System.out.println("UserManager - Bouton cliqué");
			try {
				// Création d'un nouvel utilisation
				// System.out.println("UserManager - Button clicked : "
				// request.getParameter("back"));
				User newUser = new User(firstName, lastName, email, pwd, gender, role);

				// Ajout d'un utilisateur à la list usersTable
				int count = 0;
				count = usersTable.size();
				usersTable.put(count, newUser);
				System.out.println("UserManager - User added. Number of users : " + usersTable.get(count));

				// Afficher la liste des users
				doGet(request, response);
			} catch (Exception e) {
				System.out.println("UserManager - ADD USER FAILED !!! ERROR : " + e);

			}

		} else if ("Se connecter".equals(btnGoToLogin)) {
			RequestDispatcher rd = request.getRequestDispatcher("/connexion.html");
			rd.forward(request, response);
		} else {
			System.out.println("UserManager - Back to home cliqué ");
			System.out.println(request.getParameter("back"));
			System.out.println(request.getParameter("back") != null);
			RequestDispatcher rd = request.getRequestDispatcher("/");
			rd.forward(request, response);
		}

	}

}
