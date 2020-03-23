package servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import components.User;

/**
 * Servlet implementation class Connexion
 */
@WebServlet("/Connexion")
public class Connexion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Connexion() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		HttpSession session = request.getSession();
		Object email = session.getAttribute("email");
		Object pwd = session.getAttribute("password");
		Object role = session.getAttribute("role");
		session.invalidate();
		System.out.println(email + ":" + pwd);
		// Définir la page navigation pour l'admin et l'utilisateur

		// Vérifier si l'utilisateur est un admin
		boolean isAdmin = false;
		if ("admin".equalsIgnoreCase(role.toString())) {
			isAdmin = true;
		}

		// Définir la page navigation pour l'admin et l'utilisateur
		String htmlRep = "<head><title>Navigation</title></head>";
		htmlRep += "<body>";
		// htmlRep += "<h1>Hello " + session.getAttribute("login") + "</h1>";
		htmlRep += "Hello";
		htmlRep += "<br>";
		htmlRep += "Connected";
		if (isAdmin) {
			htmlRep += "<nav> <ul>";
			htmlRep += "<li><a href=\"index.html\">Créer un nouveau utilisateur</a></li>";
			htmlRep += "<li><a href=\"info-users.html\">Afficher la liste des utilisateurs</a></li>";
			htmlRep += "</ul>";
			htmlRep += "</nav>";
		}
		htmlRep += "</body>";
		htmlRep += "</html>";
		response.getWriter().println(htmlRep);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		// récupérer des paramètres
		final String email = request.getParameter("email");
		final String pwd = request.getParameter("password");

		//
		Boolean doUserExist = false;
		Hashtable<Integer, User> usersTable = UserManager.getUsersTable();
		for (Map.Entry<Integer, User> entry : usersTable.entrySet()) {
			User u = entry.getValue();
			if (u.getEmail().equals(email)) {
				doUserExist = true;
				final String loginPwd = u.getPwd();
				// sauvegarder le login d’utilisateur et son rôle dans la session en cas de
				// succès
				if (loginPwd.equals(pwd)) {
					HttpSession session = request.getSession();
					final String role = u.getRole();
					session.setAttribute("email", email);
					session.setAttribute("password", pwd);
					session.setAttribute("role", role);
				}
				break;
			}
		}
		if (doUserExist) {
			doGet(request, response);

		}
	}
}
