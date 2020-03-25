package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
		htmlRep += "<br>";
		if (isAdmin) {
			PrintWriter out = response.getWriter();
			out.print("Dernière connexion:");
			//Obtenir des cookies 
			Cookie[] cookies = request.getCookies();
			//Si c'est la première connexion
			for (int i = 0; cookies != null && i < cookies.length; i++) {
				if (cookies[i].getName().equals("lastAccessTime")) {
					long cookieValue = Long.parseLong(cookies[i].getValue());
					Date date = new Date(cookieValue);
					out.print(date.toString());
				}
			}
			
			//Mise à jour la date de la connexion
			Cookie c = new Cookie("lastAccessTime", System.currentTimeMillis() + "");
			response.addCookie(c);
			htmlRep += "<nav> <ul>";
			htmlRep += "<li><a href=\"index.html\">Créer un nouveau utilisateur</a></li>";
			htmlRep += "<li><a href=\"info-users.html\">Afficher la liste des utilisateurs</a></li>";
			htmlRep += "</ul>";
			htmlRep += "</nav>";
		}
		htmlRep += "<form action=\"/td2/Deconnexion\" method=\"post\">";
		htmlRep += "<br>";
		htmlRep += "<input type=\"submit\" name=\"deconnexion\" value=\"Log out\" >";
		htmlRep += "<br>";
		htmlRep += "</form>";
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// récupérer des paramètres
		final String email = request.getParameter("email");
		final String pwd = request.getParameter("password");
		final String btnGoToLogin = request.getParameter("login");
		if ("Se connecter".equals(btnGoToLogin)) {
			RequestDispatcher rd = request.getRequestDispatcher("/connexion.html");
			rd.forward(request, response);
		} else {

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
					} else {
						out.print("Désolé mot de passe incorrect");
						RequestDispatcher rd = request.getRequestDispatcher("/connexion.html");
						rd.include(request, response);
					}
					break;
				}
			}
			if (doUserExist) {
				doGet(request, response);
			}
		}
	}
}
