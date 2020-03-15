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
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher rd = request.getRequestDispatcher("/UserManager");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("familyname");
		String email = request.getParameter("email");
		String pwd = request.getParameter("password");
		String gender = request.getParameter("gender");
		String role = request.getParameter("role");
		String contextPath = request.getContextPath();
		System.out.println(contextPath);
		Hashtable<Integer, User> usersTable = UserManager.getUsersTable();
		User newUser = new User(firstName, lastName, email, pwd, gender, role);
		if (usersTable.isEmpty()) {
			doGet(request, response);
		} else {
			for (Map.Entry<Integer, User> entry : usersTable.entrySet()) {
				int key = entry.getKey();
				User u = entry.getValue();
				if (u.equals(newUser)) {
					System.out.println("Cette utilisateur existe déjà.111");
//						response.getWriter().println("<script>alert(\"Cette utilisateur existe déjà.\");</script>");

					String htmlRep = "<html>";
					htmlRep += "<h3>-------------doublon------------</h3>";
					htmlRep += "<form action=\"/td2/UserManager\" method=\"post\">";
					htmlRep += "<br>";
					htmlRep += "<input type=\"submit\" value=\"continue\" >";
					htmlRep += "<input type=\"button\" name=\"back\" onclick=\"javascript:window.location.href='/td2'\" value=\"back\" >";
					htmlRep += "<input type=\"hidden\" id=\"frname\" name=\"firstname\" value=\"" + firstName + "\" >";
					htmlRep += "<input type=\"hidden\" id=\"faname\" name=\"familyname\" value=\"" + lastName + "\"  >";
					htmlRep += "<input type=\"hidden\" id=\"email\" name=\"email\"  value=\"" + email + "\" >";
					htmlRep += "<input type=\"hidden\" id=\"psw\" name=\"password\" value=\"" + pwd + "\" >";
					htmlRep += "<input type=\"hidden\" name=\"gender\" value=\"" + gender + "\" >";
					htmlRep += "</form>";
					htmlRep += "</html>";
					response.getWriter().println(htmlRep);

				}
			}
		}

		// doGet(request, response);

	}

}
