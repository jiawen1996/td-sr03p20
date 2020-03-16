package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Hashtable;
import java.util.Map;

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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// récupérer des attributs dans la session
		HttpSession session = request.getSession();
		Object email = session.getAttribute("email");
		Object pwd = session.getAttribute("password");
		System.out.println(email + ":" + pwd);

		// fermer la session
		session.invalidate();
		String htmlRep = "<html>";
		htmlRep += "<h3>-------------User: " + email + "------------</h3>";
		htmlRep += "<h3>-------------Password: " + pwd + "------------</h3>";
		htmlRep += "</form>";
		htmlRep += "</html>";
		response.getWriter().println(htmlRep);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		// récupérer des paramètres
		final String email = request.getParameter("email");
		final String pwd = request.getParameter("password");
		final String role = request.getParameter("role");

		//
		Boolean doUserExist = false;
		Hashtable<Integer, User> usersTable = UserManager.getUsersTable();
		for (Map.Entry<Integer, User> entry : usersTable.entrySet()) {
			int key = entry.getKey();
			User u = entry.getValue();
			if (u.getEmail().equals(email)) {
				doUserExist = true;
				final String loginPwd = u.getPwd();
				// sauvegarder le login d’utilisateur et son rôle dans la session en cas de
				// succès
				if (loginPwd.equals(pwd)) {
					HttpSession session = request.getSession();
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
