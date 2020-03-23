package servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class UserVerification
 */
@WebServlet("/UserVerification")
public class UserVerification extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserVerification() {
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
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher rd = request.getRequestDispatcher("/UserManager");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 *      Récupérer des données depuis index.html pour vérifier s'il y a un
	 *      doublons Sinon, transférer la requête vers UserManager pour créer un
	 *      nouvel utilisateur
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Récupérer des données depuis index.html
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("familyname");
		String email = request.getParameter("email");
		String pwd = request.getParameter("password");
		String gender = request.getParameter("gender");
		String role = request.getParameter("role");
		String contextPath = request.getContextPath();
		System.out.println("UserVerification - doPost - ContextPath : " + contextPath);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// Vérifier le doublon
		Hashtable<Integer, User> usersTable = UserManager.getUsersTable();
		User newUser = new User(firstName, lastName, email, pwd, gender, role);

		if (usersTable.isEmpty()) {//// Création d'un utilisateur si la liste d'utilisateur est vide
			System.out.println("UserVerification - Créer le premier utilisateur");
			doGet(request, response);
		} else { //// Sinon, vérifier le doublon
			boolean userExiste = false;
			for (Map.Entry<Integer, User> entry : usersTable.entrySet()) {
				User u = entry.getValue();
				if (u.equals(newUser)) { // si l'utilisateur était dans la liste, envoyer ses données vers UserManager
											// pour s'afficher
					System.out.println("Cette utilisateur existe déjà.111");
					// response.getWriter().println("<script>alert(\"Cette utilisateur existe
					// déjà.\");</script>");

					String htmlRep = "<html>";
					htmlRep += "<h3>-------------doublon------------</h3>";
					htmlRep += "L'utilisateur " + firstName + " " + lastName + " était dans la base.";
					htmlRep += "<form action=\"/td2/UserManager\" method=\"post\">";
					htmlRep += "<br>";
					htmlRep += "<input type=\"submit\" name=\"continue\" value=\"Continue\" >";
					htmlRep += "<br>";
					htmlRep += "<input type=\"submit\" name=\"back\" value=\"Back to home\" >";
					htmlRep += "<input type=\"hidden\" id=\"frname\" name=\"firstname\" value=\"" + firstName + "\" >";
					htmlRep += "<input type=\"hidden\" id=\"faname\" name=\"familyname\" value=\"" + lastName + "\"  >";
					htmlRep += "<input type=\"hidden\" id=\"email\" name=\"email\"  value=\"" + email + "\" >";
					htmlRep += "<input type=\"hidden\" id=\"psw\" name=\"password\" value=\"" + pwd + "\" >";
					htmlRep += "<input type=\"hidden\" name=\"gender\" value=\"" + gender + "\" >";
					htmlRep += "</form>";
					htmlRep += "</html>";
					response.getWriter().println(htmlRep);

					userExiste = true;
					break;
				}
			}
			if (userExiste == false) { // nouvel utilisateur
				System.out.println("UserVerification - Créer d'un utilisateur");
				doGet(request, response);
			}
		}
	}

}
