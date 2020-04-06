/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.Controller;

import java.Model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lounis
 */
@WebServlet(name = "Connexion", urlPatterns = {"/Connexion"})
public class Connexion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Vérifier si le login existe
        Set<Integer> keys = UserManager.getUsersTable().keySet();

        //Obtaining iterator over set entries
        Iterator<Integer> itr = keys.iterator();
        int trouve = -1;
        while (itr.hasNext() && trouve < 0) {
            int index = (int) itr.next();
            if (UserManager.getUsersTable().get(index).getLogin().equalsIgnoreCase(request.getParameter("username"))) {
                trouve = index;
                break;
            }
        }
        if (trouve < 0) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet Connexion</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Echec </h1>");
                out.println("</body>");
                out.println("</html>");
            }
        } else if (!UserManager.getUsersTable().get(trouve).getPwd().equals(request.getParameter("password"))) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet Connexion</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Echec :mot de passe érroné </h1>");
                out.println("</body>");
                out.println("</html>");

            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("login", UserManager.getUsersTable().get(trouve).getLogin());
            String role = UserManager.getUsersTable().get(trouve).getRole();
            session.setAttribute("role", role);
            response.setContentType("text/html;charset=UTF-8");
            if ("admin".equals(role)) {
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html><head><title>Navigation Administrateur</title></head>");
                    out.println("<body>");
                    out.println("<h1>Hello " + session.getAttribute("login") + "</h1>");
                    out.println("<nav> <ul>");
                    out.println(" <li>Connected</li>");
                    out.println("<li><a href='NouveauUtilisateur.html'>Créer un nouveau utilisateur</a></li>");
                    out.println(" <li><a href='UserManager'>Afficher la liste des utilisateurs</a></li>");
                    out.println(" <li><a href='Deconnexion'>Déconnecter</a></li>");
                    out.println("</ul>");
                    out.println("</nav>");
                    out.println("</body>");
                    out.println("</html>");
                }

            } else {
                try (PrintWriter out = response.getWriter()) {
                    /* TODO output your page here. You may use following sample code. */
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet Connexion</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Succes : utilisateur non admin </h1>");
                    out.println("</body>");
                    out.println("</html>");
                }

            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        //Créer le premier utilisateur de l'application pour pouvoir connecter et ajouter d'autres utilisateurs
        int id = UserManager.getUsersTable().size();
        UserManager.getUsersTable().put(id, new User("utc", "utc", "lounis@utc", id, "Mr", "lounisah"));
        UserManager.getUsersTable().get(id).setRole("admin");
    }

}
